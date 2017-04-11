package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GoToHeading extends Command {
	double m_desiredHeading;
	double m_tolerance;
	final double DEFAULT_TOLERANCE = 10.0;
	final int LOOPS_REQUIRED_WITHIN_TOLERANCE = 4;
	int m_loopsWithinTolerance;
	
	public GoToHeading ( double heading ) {
        m_desiredHeading = heading;
        m_tolerance = DEFAULT_TOLERANCE;
    }
	
	public GoToHeading ( double heading, double tolerance ) {
        m_desiredHeading = heading;
        m_tolerance = tolerance;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	m_loopsWithinTolerance = 0;
    	Robot.drive.resetGoToHeading(m_desiredHeading);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drive.goToHeading(m_desiredHeading);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	
//        return ( m_tolerance >= (Math.abs(Robot.drive.getHeading() - m_desiredHeading)));
        
        if (m_tolerance >= (Math.abs(Robot.drive.getHeading() - m_desiredHeading))) {
        	m_loopsWithinTolerance++;
        }
        return m_loopsWithinTolerance >= LOOPS_REQUIRED_WITHIN_TOLERANCE;
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
