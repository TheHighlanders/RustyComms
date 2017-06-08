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
	
	private double currentAngle;
	
	private double initialAngle;
	
	private double absoluteTargetAngle;
	
	private boolean isFinished = false;
	
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
			
			Robot.dt.driveLR(turnSpeed, -turnSpeed);
			
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
