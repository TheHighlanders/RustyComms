package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;


public class ReleaseHopperDeployers extends Command {

	
	private double m_startTime;
	private double m_desiredRuntime;
    public ReleaseHopperDeployers() {
        m_desiredRuntime = 1;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.shooterLoader.releaseHopperDeployers();
    	m_startTime = Timer.getFPGATimestamp();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return ((Timer.getFPGATimestamp() - m_startTime) >= m_desiredRuntime);
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.shooterLoader.turnOff();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.shooterLoader.turnOff();
    }
}
