package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class PushGearOut extends Command {

	double m_startTime;
	double m_desiredTime;
	
	public PushGearOut() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
		m_desiredTime = 0.45;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.gearManipulator.PushGearOut();
    	m_startTime = Timer.getFPGATimestamp();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
		double diff = Timer.getFPGATimestamp() - m_startTime;
		diff = Math.abs(diff);
		return (diff >= m_desiredTime);    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.gearManipulator.Stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.gearManipulator.Stop();
    }
}
