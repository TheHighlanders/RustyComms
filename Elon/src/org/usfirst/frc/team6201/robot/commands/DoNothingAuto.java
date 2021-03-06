package org.usfirst.frc.team6201.robot.commands;

import org.usfirst.frc.team6201.robot.dataLogger.DataCollator;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Does nothing for the auto period.
 *
 * @author David Matthews
 * @author Adriana Massie
 * @version Feb 4, 2017
 *
 */
public class DoNothingAuto extends Command {

	public DoNothingAuto() {

	}

	// Called just before this Command runs the first time
	protected void initialize() {
		DataCollator.state.setVal("DoNothingAutoCmd");
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return true;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
