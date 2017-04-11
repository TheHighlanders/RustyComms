package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveStraightForTime extends Command {


	double m_targetPower;
	double m_startTime;
	double m_desiredTime;
	
	/**
	 * 
	 * @param arg_targetPower +/- motor power [-1.0, +1.0]
	 * @param arg_distance Time is in seconds
	 */
	public DriveStraightForTime(double arg_targetSpeed, double timeInSeconds) {
		//@SSM Mar1 - PLEASE don't require any subsystems!
		
		m_desiredTime = timeInSeconds;
		m_targetPower = arg_targetSpeed;

		
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		m_startTime = Timer.getFPGATimestamp();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.drive.hDrive(m_targetPower, 0, 0, false, false);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		double diff = Timer.getFPGATimestamp() - m_startTime;
		diff = Math.abs(diff);
		return (diff >= m_desiredTime);
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.drive.stop();    	
	}
	
	protected void interrupted() {
		Robot.drive.stop();
	}
}
