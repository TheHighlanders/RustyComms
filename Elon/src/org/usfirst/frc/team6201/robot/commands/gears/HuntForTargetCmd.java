package org.usfirst.frc.team6201.robot.commands.gears;

import org.usfirst.frc.team6201.robot.Robot;
import org.usfirst.frc.team6201.robot.gearVision.GearVisionCollator;

import edu.wpi.first.wpilibj.command.Command;

/**
 * rotates the robot to attempt to regain tracking of the vision
 */
public class HuntForTargetCmd extends Command {

	private boolean targetFound = false;
    public HuntForTargetCmd() {
       requires(Robot.dt);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (!targetFound){
    		Robot.dt.driveLR(0.2, -0.2);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        targetFound = GearVisionCollator.isTargetLocationKnown();
    	return targetFound;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
