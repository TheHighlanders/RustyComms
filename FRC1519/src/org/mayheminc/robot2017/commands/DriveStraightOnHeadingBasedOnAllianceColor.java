package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;
import org.mayheminc.robot2017.commands.DriveStraightOnHeading.DistanceUnits;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class DriveStraightOnHeadingBasedOnAllianceColor extends Command {

	public double m_heading;
	public double m_targetSpeed;
	public DriveStraightOnHeading.DistanceUnits m_units;
	public double m_distance;
	public boolean m_isRed;
	public boolean m_hasStarted;
	public Command m_command;
    
    public DriveStraightOnHeadingBasedOnAllianceColor(double arg_targetSpeed, DistanceUnits units, double arg_distance, double heading) {
        m_heading = heading;
        m_targetSpeed = arg_targetSpeed;
        m_units = units;
        m_distance = arg_distance;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	
    	m_isRed = Robot.autonomous.isRed();
    	DriverStation.reportError("Are we Red? : " + m_isRed, false);
    	if(!m_isRed){
    		m_heading *= -1;    		
    	}
    	m_hasStarted = false;
    	m_command = new DriveStraightOnHeading(m_targetSpeed, m_units, m_distance, m_heading);
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
    	DriverStation.reportError("Finished DriveStraightOnHeadingBasedOnAllianceColor", false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	DriverStation.reportError("Finished DriveStraightOnHeadingBasedOnAllianceColor", false);
    }
}
