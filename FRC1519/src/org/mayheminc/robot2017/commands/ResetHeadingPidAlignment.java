package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class ResetHeadingPidAlignment extends InstantCommand {

    public ResetHeadingPidAlignment() {
        super();
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called once when the command executes
    protected void initialize() {
    	DriverStation.reportError("ResetHeadingPidAlignment initializing", false);
    	Robot.drive.resetHeadingPidAlignment();
    }

}
