package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AlignToBoiler extends Command {
//	double m_maxTime;
//	double m_initTime;
	
    public AlignToBoiler() {
        // Use requires() here to declare subsystem dependencies
//        requires(Robot.drive);
//        m_maxTime = timeout;  
//        m_initTime = Timer.getFPGATimestamp();
    }
    
    /*
     * returns timer's time in seconds
     */

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drive.resetHeadingPidAlignment();
//    	DriverStation.reportError("initializing", false);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drive.autoTargetToBoiler();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return (Robot.drive.isAlignedToBoiler(Robot.drive.AIM_REQUIRED_LOOPS_ON_TARGET_TELEOP));
        		//|| ((Timer.getFPGATimestamp() - m_initTime) > m_maxTime));
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drive.stop();
//    	new Print("Finished Aligning to Boiler");
//    	DriverStation.reportError("Done!", false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.drive.stop();
//    	new Print("Interrupted AlignToBoiler()");
//    	DriverStation.reportError("interrupted....", false);
    }
}
