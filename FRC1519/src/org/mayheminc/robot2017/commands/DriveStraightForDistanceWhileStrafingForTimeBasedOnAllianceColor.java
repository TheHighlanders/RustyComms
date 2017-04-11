package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveStraightForDistanceWhileStrafingForTimeBasedOnAllianceColor extends Command {

	boolean m_isRed;
	double m_desiredStrafeDuration;
	DriveStraightForDistanceWhileStrafingForTime.DriveDistanceUnits m_driveUnits;
	double m_driveAmount;

	double m_targetDriveSpeed;
	double m_targetStrafeSpeed;
	
	boolean m_hasStarted;
	Command m_command;
	
    public DriveStraightForDistanceWhileStrafingForTimeBasedOnAllianceColor(
    		double arg_driveSpeed, DriveStraightForDistanceWhileStrafingForTime.DriveDistanceUnits arg_driveUnits, double arg_driveAmount, 
    		double arg_strafeSpeed, double arg_strafeDuration) {
        //@SSM Mar1 - PLEASE don't require any subsystems!
    	
    	m_driveUnits = arg_driveUnits;
    	
    	m_driveAmount = arg_driveAmount;
    	m_desiredStrafeDuration = arg_strafeDuration;
    	
    	m_targetDriveSpeed = arg_driveSpeed;
    	m_targetStrafeSpeed = arg_strafeSpeed;
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
    	m_command = new DriveStraightForDistanceWhileStrafingForTime(m_targetDriveSpeed, m_driveUnits, m_driveAmount,
    						m_targetStrafeSpeed * modifier, m_desiredStrafeDuration);
    	m_command.start();
    	//DriverStation.reportError("Beginning StafeBasedOnAllianceColor with m_targetSpeed of " + m_targetSpeed, false);
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
    	DriverStation.reportError("Finished DriveStraightForDistanceWhileStrafingForTimeBasedOnAllianceColor(...)", false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	DriverStation.reportError("interrupted DriveStraightWhileStrafingBasedOnAllianceColor", false);
    }
}
