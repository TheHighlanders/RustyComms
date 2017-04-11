package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class EnableThumper extends InstantCommand {

    public EnableThumper() {
        super();
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called once when the command executes
    protected void initialize() {
    	Robot.shooterLoader.turnThumperOn();
    }

}
