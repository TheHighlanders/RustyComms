package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;
import org.mayheminc.robot2017.subsystems.Drive;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveStraight extends Command {


	double m_targetPower;
	double m_desiredDisplacement;
//	public int m_startPos = 0;
	public enum DistanceUnits { ENCODER_TICKS, INCHES };


	/**
	 * 
	 * @param arg_targetPower +/- motor power [-1.0, +1.0]
	 * @param arg_distance Distance in encoder counts
	 */
	public DriveStraight(double arg_targetSpeed, DistanceUnits units, double arg_distance) {
		//requires(Robot.drive);
		
		if (units == DistanceUnits.INCHES) {
			arg_distance = arg_distance / Drive.DISTANCE_PER_PULSE;
		}
		m_targetPower = arg_targetSpeed;
		m_desiredDisplacement = Math.abs(arg_distance);   	
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		
//		m_startPos = Robot.drive.getRightEncoder();
		Robot.drive.saveInitialWheelDistance();
		System.out.println("Starting Routine: Drive Straight Forward");

	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.drive.hDrive(m_targetPower, 0, 0, false, false);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
//		int displacement = Robot.drive.getRightEncoder() - m_startPos;
		int displacement = (int) Robot.drive.getWheelDistance();
		displacement = Math.abs(displacement);
		return (displacement >= m_desiredDisplacement);
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.drive.stop();    	
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		Robot.drive.stop();

	}

}

