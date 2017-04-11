package org.mayheminc.robot2017.autonomousroutines;

import org.mayheminc.robot2017.Robot;
import org.mayheminc.robot2017.commands.StrafeForTime;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class StrafeForTimeBasedOnAllianceColor extends Command {
	boolean m_isRed;

	double m_driveTime;
	double m_targetSpeed;
	boolean m_hasStarted;
	Command m_command;
	
    public StrafeForTimeBasedOnAllianceColor(double arg_targetSpeed, double arg_driveTime) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
//    	requires(Robot.drive);
    	    	
    	m_driveTime = arg_driveTime;
    	m_targetSpeed = arg_targetSpeed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	m_isRed = Robot.autonomous.isRed();
    	DriverStation.reportError("Are we Red? : " + m_isRed, false);
    	int modifier = 1;
    	if(!m_isRed){
    		modifier = -1;
    	}
    	m_hasStarted = false;
    	m_command = new StrafeForTime(m_targetSpeed * modifier, m_driveTime);
    	DriverStation.reportError("About to start() m_command", false);
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
    	Robot.drive.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.drive.stop();
    	DriverStation.reportError("interrupted StrafeBasedOnAllianceColor", false);
    }
}
