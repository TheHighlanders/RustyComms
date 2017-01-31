package org.usfirst.frc.team6201.robot.commands;

import org.usfirst.frc.team6201.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 * 
 * Turns the robot from its current angle X degrees.
 *
 * @author Baxter Ellard
 * @author Adriana Massie 
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
	private final double MAXSPEEDTHRESH = 42;
	
	
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
	
	}
	
	/**
	 * This method calculate the speed of the motor based off of currentAngleOffset
	 */
	protected void execute() {
		
		currentAngleOffset = targetRotation - Robot.dt.getGyroAngle();
		
		if (currentAngleOffset >= MAXSPEEDTHRESH){
			Robot.dt.driveLR(1,-1);
		}
		
		else if (currentAngleOffset <= -MAXSPEEDTHRESH){
			Robot.dt.driveLR(-1,1);
		}
	
		else { 
			turnSpeed = 1.0 / MAXSPEEDTHRESH * currentAngleOffset;
			Robot.dt.driveLR(turnSpeed,-turnSpeed);
				
		}
		
	}

	protected boolean isFinished() {
	
		currentAngleOffset = targetRotation - Robot.dt.getGyroAngle();
		
		return Math.abs(currentAngleOffset) < acceptedAngleOffset;
	
	}
	
	protected void end() {
		
		Robot.dt.stop();
	
	}
	
	protected void interrupted() {
	
		end();
		
	}
}
