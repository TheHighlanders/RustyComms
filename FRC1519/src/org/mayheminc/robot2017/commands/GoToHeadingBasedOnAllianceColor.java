package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GoToHeadingBasedOnAllianceColor extends Command {

	public double m_heading;
	public double m_tolerance = 4;
	public boolean m_isRed;
	public boolean m_hasStarted;
	public Command m_command;
    public GoToHeadingBasedOnAllianceColor(double heading) {
        m_heading = heading;
    }
    public GoToHeadingBasedOnAllianceColor(double heading, double tolerance) {
        m_heading = heading;
        m_tolerance = tolerance;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	
    	m_isRed = Robot.autonomous.isRed();
    	DriverStation.reportError("Are we Red? : " + m_isRed, false);
    	if(!m_isRed){
    		m_heading *= -1;    		
    	}
    	m_hasStarted = false;
    	m_command = new GoToHeading(m_heading, m_tolerance);
    	m_command.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(m_command.isRunning()){
    		m_hasStarted = true;
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return (m_hasStarted && !m_command.isRunning());
    }

    // Called once after isFinished returns true
    protected void end() {
    	DriverStation.reportError("Finished GoToHeadingBasedOnAllianceColor", false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	DriverStation.reportError("Finished GoToHeadingBasedOnAllianceColor", false);
    }
}
