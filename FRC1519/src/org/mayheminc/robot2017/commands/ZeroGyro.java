package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *DOES NOT fully calibrate the navx
 *this command will simply reset the navx
 */
public class ZeroGyro extends InstantCommand {

    public ZeroGyro() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	//requires(Robot.drive);
    	setRunWhenDisabled(true);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drive.zeroHeadingGyro();
    	Robot.drive.setHeadingOffset(0.0);
    	
    }
}
