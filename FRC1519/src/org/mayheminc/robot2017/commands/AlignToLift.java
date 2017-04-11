package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AlignToLift extends Command {
	double m_Timeout;
	double m_StartTime;
    public AlignToLift(double timeout) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	m_Timeout = timeout;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	m_StartTime = Timer.getFPGATimestamp();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drive.autoTargetToLift();
    	if((Timer.getFPGATimestamp() - m_StartTime) >= m_Timeout){
    		Robot.drive.forceAlignQuit();
    	}
    }
    protected boolean isFinished() {

    	double elapsedTime = Timer.getFPGATimestamp() - m_StartTime;
    	
        return Robot.drive.isDonePlacingGear() || (elapsedTime >= m_Timeout);
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drive.stoppedAligningToLift(); //reset the state machine
    	Robot.drive.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.drive.stoppedAligningToLift(); //reset the state machine
    	Robot.drive.stop();
	}
}
