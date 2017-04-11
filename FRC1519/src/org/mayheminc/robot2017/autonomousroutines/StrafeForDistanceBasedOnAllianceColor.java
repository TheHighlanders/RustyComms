package org.mayheminc.robot2017.autonomousroutines;

import org.mayheminc.robot2017.Robot;
import org.mayheminc.robot2017.commands.DriveStraight.DistanceUnits;
import org.mayheminc.robot2017.commands.StrafeForDistance;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class StrafeForDistanceBasedOnAllianceColor extends Command {
	boolean m_isRed;
	StrafeForDistance.StrafeDistanceUnits m_units;
	int m_driveAmount;
	double m_targetSpeed;
	boolean m_hasStarted;
	Command m_command;
	
    public StrafeForDistanceBasedOnAllianceColor(StrafeForDistance.StrafeDistanceUnits arg_units, int arg_driveAmount, double arg_targetSpeed) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
//    	requires(Robot.drive);
    	
    	m_units = arg_units;
    	m_driveAmount = arg_driveAmount;
    	m_targetSpeed = arg_targetSpeed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	int driveAmount = m_driveAmount;
    	double targetSpeed = m_targetSpeed;
    	m_isRed = Robot.autonomous.isRed();
    	DriverStation.reportError("Are we Red? : " + m_isRed, false);
    	if(!m_isRed){
    		driveAmount *= -1;
    		targetSpeed *= -1;
    	}
    	m_hasStarted = false;
    	m_command = new StrafeForDistance(targetSpeed, m_units, driveAmount);
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
    	DriverStation.reportError("interrupted StrafeBasedOnAllianceColor", false);
    }
}
