package org.usfirst.frc.team6201.robot.commands;

import org.usfirst.frc.team6201.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 * 
 * Turns the robot from its current angle X degrees.
 *
 * @author Baxter Ellard
 *
 */

public class TurnAngleCmd extends Command {
	
	private double turn;
	private double scalarOnTurn = 1/3;
	private double targetRotation;
	private double acceptedError;
	private double currentError;
	
	/**
	 * Constructor
	 * 
	 * @param targetRotation			Degrees to turn the robot (pos = clockwise, neg = counterclockwise)
	 * @param acceptedError		The difference between desired and current positions of our robot at which point this command will stop running.
	 */
	public TurnAngleCmd(double targetRotation, double acceptedError) {
	
		requires(Robot.dt);
		
		this.targetRotation = targetRotation;
		this.acceptedError = acceptedError;
	
	}
	
	protected void initialize() {
		
		Robot.dt.resetGyro();
	
	}
	
	protected void execute() {
		
		while(!(Math.abs(Robot.dt.getGyroAngle() - targetRotation) <= acceptedError)) {
			
			turn = scalarOnTurn*(Robot.dt.getGyroAngle() - targetRotation);
			Robot.dt.driveLR(turn, -turn);
			
		}
		
	}

	protected boolean isFinished() {
	
		currentError = targetRotation - Robot.dt.getGyroAngle();
		
		return Math.abs(currentError) < acceptedError;
	
	}
	
	protected void end() {
		
		Robot.dt.stop();
	
	}
	
	protected void interrupted() {
	
		end();
		
	}
}
