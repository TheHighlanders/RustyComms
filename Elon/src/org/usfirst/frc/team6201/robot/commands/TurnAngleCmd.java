package org.usfirst.frc.team6201.robot.commands;

import org.usfirst.frc.team6201.robot.Robot;
import org.usfirst.frc.team6201.robot.dataLogger.DataCollator;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

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
	 * targetRotation is how far we want to turn the robot from the inital conditions 
	 */
	private double targetRotation;
	/**
	 *  acceptedAngleOffset is the difference between desired and current positions of our robot at which point this command will stop running.
	 */
	private double acceptedAngleOffset;
	/**
	 * currentAngleOffset the difference between targetRotion and current angle of the robot
	 */
	private double currentAngleOffset;
	/**
	 * MAXSPEEDTHRESH is the angleOffSet where you rotate full speed  
	 */
	private final double MAXSPEEDTHRESH = 84;
	
	private boolean needReInit = true;
	
	
	/**
	 * Constructor
	 * 
	 * @param targetRotation			Degrees to turn the robot (pos = clockwise, neg = counterclockwise)
	 * @param acceptedAngleOffset		The difference between desired and current positions of our robot at which point this command will stop running.
	 */
	public TurnAngleCmd(double targetRotation, double acceptedAngleOffset) {
	
		requires(Robot.dt);
		
		this.targetRotation = targetRotation;
		this.acceptedAngleOffset = acceptedAngleOffset;
	
	}
	
	protected void initialize() {
		
		Robot.dt.resetGyro();
		currentAngleOffset = targetRotation - Robot.dt.getGyroAngle();
		DataCollator.state.setVal("TurnAngleCmdInit");
		needReInit = false;
	
	}
	
	/**
	 * This method calculate the speed of the motor based off of currentAngleOffset
	 */
	protected void execute() {
		if (needReInit){
			initialize();
		}
		DataCollator.state.setVal("TurnAngleCmdExecute");
		currentAngleOffset = targetRotation - Robot.dt.getGyroAngle();
		
		if (currentAngleOffset >= MAXSPEEDTHRESH){
			Robot.dt.driveLR(1,-1);
		}
		
		else if (currentAngleOffset <= -MAXSPEEDTHRESH){
			Robot.dt.driveLR(-1,1);
		}
	
		else { 
			turnSpeed = Math.pow(Math.abs(currentAngleOffset), 0.4) / 12;
			DriverStation.reportWarning("turnSpeed: " +turnSpeed, false);
			Robot.dt.driveLR(turnSpeed,-turnSpeed);
				
		}
		
	}

	protected boolean isFinished() {
		DriverStation.reportWarning("\n\nGyro Angle is, in isFinish, right now: " + Robot.dt.getGyroAngle() + "\nGyroRate is: " + Robot.dt.getGyroRate(), false);
		return ((Math.abs(currentAngleOffset) < acceptedAngleOffset) && (Math.abs(Robot.dt.getGyroRate()) <= 10));
	
	}
	
	protected void end() {
		needReInit = true;
		DataCollator.state.setVal("TurnAngleCmdEnd");
		
		Robot.dt.stop();
	
	}
	
	protected void interrupted() {
	
		end();
		
	}
}
