package org.usfirst.frc.team6201.robot.commands;

import org.usfirst.frc.team6201.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Continuously started when the button is being held down
 */
public class ClimbRopeCmd extends Command {

	public ClimbRopeCmd() {
		requires(Robot.rc);
	}

	/**
	 * Sets the climber to spool the rope in order to climb the rope.
	 */
	protected void execute() {
		Robot.rc.climb();
	}

	/**
	 * this command is constantly re-started whenever the button is held.
	 */
	protected boolean isFinished() {
		return true;
	}

	/**
	 * Stops climbing to avoid any mishaps.
	 */
	protected void end() {
		Robot.rc.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
