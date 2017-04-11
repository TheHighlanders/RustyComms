package org.mayheminc.robot2017.autonomousroutines;

import org.mayheminc.robot2017.Robot;
import org.mayheminc.robot2017.commands.DriveStraightWhileStrafing;
import org.mayheminc.robot2017.commands.DriveStraightWhileStrafing.DriveDistanceUnits;
import org.mayheminc.robot2017.commands.DriveStraightWhileStrafing.StrafeDistanceUnits;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveStraightWhileStrafingBasedOnAllianceColor extends Command {

	boolean m_isRed;
	DriveStraightWhileStrafing.StrafeDistanceUnits m_strafeUnits;
	DriveStraightWhileStrafing.DriveDistanceUnits m_driveUnits;
	int m_driveAmount;
	int m_strafeAmount;
	double m_targetDriveSpeed;
	double m_targetStrafeSpeed;
	
	boolean m_hasStarted;
	Command m_command;
	
    public DriveStraightWhileStrafingBasedOnAllianceColor(
    		double arg_driveSpeed, DriveStraightWhileStrafing.DriveDistanceUnits arg_driveUnits, int arg_driveAmount, 
    		double arg_strafeSpeed, DriveStraightWhileStrafing.StrafeDistanceUnits arg_strafeUnits, int arg_strafeAmount) {
        //@SSM Mar1 - PLEASE don't require any subsystems!
    	
    	m_strafeUnits = arg_strafeUnits;
    	m_driveUnits = arg_driveUnits;
    	
    	m_driveAmount = arg_driveAmount;
    	m_strafeAmount = arg_strafeAmount;
    	
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
    	m_command = new DriveStraightWhileStrafing(m_targetDriveSpeed, m_driveUnits, m_driveAmount,
    						m_targetStrafeSpeed * modifier, m_strafeUnits, m_strafeAmount * modifier);
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
    	DriverStation.reportError("Finished StrafeBasedOnAllianceColor", false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	DriverStation.reportError("interrupted DriveStraightWhileStrafingBasedOnAllianceColor", false);
    }
}
