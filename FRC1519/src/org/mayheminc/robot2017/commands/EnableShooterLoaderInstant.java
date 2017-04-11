package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class EnableShooterLoaderInstant extends InstantCommand {

    public EnableShooterLoaderInstant() {
        super();
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.shooterLoader);
    }

    // Called once when the command executes
    protected void initialize() {
    	Robot.shooterLoader.turnOn();
    }

}
