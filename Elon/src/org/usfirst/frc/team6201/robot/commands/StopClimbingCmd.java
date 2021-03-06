package org.usfirst.frc.team6201.robot.commands;

import org.usfirst.frc.team6201.robot.Robot;
import org.usfirst.frc.team6201.robot.dataLogger.DataCollator;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class StopClimbingCmd extends Command {

    public StopClimbingCmd() {
      requires(Robot.rc);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
		DataCollator.state.setVal("StopClimbingCmdExe");
    	Robot.rc.stop();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
