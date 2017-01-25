package org.usfirst.frc.team6201.robot.dataLogger;

import org.usfirst.frc.team6201.robot.Robot;
import org.usfirst.frc.team6201.robot.subsystems.DataLoggerFetcher;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Instructs the DataLoggerPublisherThread to send a message over UDP to stop logging.
 * 
 * @author David Matthews
 */
public class StopLoggingRecorderCmd extends Command {
	
	/**
	 * Requires the DataLoggerFetcher subsystem
	 */
	public StopLoggingRecorderCmd() {
		requires(Robot.dlf);
	}

	/**
	 * Tells DataLoggerPublisherThread to send a "stop logging message"
	 */
	protected void initialize() {
		Robot.dlf.stopLoggingRecorder();
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return true;
	}

	protected void end() {
	}

	protected void interrupted() {
	}
}
