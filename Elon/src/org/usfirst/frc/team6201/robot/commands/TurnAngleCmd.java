package org.usfirst.frc.team6201.robot.commands;

import org.usfirst.frc.team6201.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 * 
 * @author Baxter Ellard
 * 
 * Turns the robot from its current angle X degrees.
 *
 */

public class TurnAngleCmd extends Command {
	
	private double turn;
	private double scalarOnTurn = 1/3;
	private double rotation;
	private double acceptedError;
	
	public TurnAngleCmd(double rotation, double acceptedError) {
	
		requires(Robot.dt);
		
		this.rotation = rotation;
		this.acceptedError = acceptedError;
	
	}
	
	protected void initialize() {
		
		Robot.dt.resetGyro();
	
	}
	
	protected void execute() {
		
		while(!(Math.abs(Robot.dt.getGyroAngle() - rotation) <= acceptedError)) {
			
			turn = scalarOnTurn*(Robot.dt.getGyroAngle() - rotation);
			Robot.dt.driveLR(turn, -turn);
			
		}
		
	}

	protected boolean isFinished() {
	
		return true;
	
	}
	
	protected void end() {
		
		Robot.dt.stop();
	
	}
	
	protected void interrupted() {
	
		end();
		
	}
}
