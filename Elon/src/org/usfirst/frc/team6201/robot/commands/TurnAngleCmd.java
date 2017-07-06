package org.usfirst.frc.team6201.robot.commands;

import org.usfirst.frc.team6201.robot.Robot;
import org.usfirst.frc.team6201.robot.dataLogger.DataCollator;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 * Turns the robot from its current angle X degrees.
 * TODO: this way over turns!, maybe increase MAXSPEEDTHRESH, or maybe implment momentum correction through either encoders for Robot speed, or tracking the robot a few 100ms after the cmd thinks it's done.
 * 
 *
 * @author Baxter Ellard
 * @author Adriana Massie
 * @author David Matthews 
 *
 */

public class TurnAngleCmd extends Command {
	
	/**
	 * turnSpeed is the speed that the robot turns at depends on currentAngleOffset 
	 */
	private double turnSpeed;
	/**
	 * targetRotation is how far we want to turn the robot from the initial conditions 
	 */
	private double targetRotation;
	/**
	 * currentAngle is the current angle the robot is at
	 */
	private double currentAngle;
	/**
	 * initialAngle the initial angle the robot starts at
	 */
	private double initialAngle;
	/**
	 * absoluteTargetAngle is the angle that the robot will be at after turning
	 * initialAngle + targetRotation = absoluteTargetAngle
	 */
	private double absoluteTargetAngle;
	/**
	 * Checks if the robot is done turning
	 */
	private double[] turnSpeedSlowing = {0.25, 0.5, 0.75, 1};
	
	private double lowSpeed = turnSpeedSlowing[(int) Math.floor(targetRotation)];
	private double highSpeed = turnSpeedSlowing[(int) Math.ceil(targetRotation)];
	
	private boolean isFinished = false;
	/**
	 * Checks if you still need to run initialize()
	 */
	private boolean runInitialize = true;
	
	
	public TurnAngleCmd(double targetRotation) {
		
		this.targetRotation = targetRotation;
		this.turnSpeed = SmartDashboard.getNumber("Turning Speed", 0.5);
		
	}
	
	protected void initialize() {
		
		this.initialAngle = Robot.dt.getGyroAngle();
		
		absoluteTargetAngle = initialAngle + targetRotation;
		
		runInitialize = false;
		
	}
	
	/**
	 * This method calculate the speed of the motor based off of currentAngleOffset
	 */
	protected void execute() {
		
		if(runInitialize) {
			
			initialize();
			
		}
		
		this.currentAngle = Robot.dt.getGyroAngle();
		
		if(absoluteTargetAngle <= currentAngle) {
			
			isFinished = true;
			
		} else {
			
			for(int i = 0; i < targetRotation; i++) {
				
				turnSpeed = lowSpeed + (highSpeed - lowSpeed) * (targetRotation - Math.floor(targetRotation));
				
			}
			
		}
		
	}

	protected boolean isFinished() {

		return isFinished;
	
	}
	
	protected void end() {
		
		runInitialize = true;
		
	}
	
	protected void interrupted() {
	
		end();
		
	}
}
