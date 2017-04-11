package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class SetHeadingOffset180 extends InstantCommand {

    public SetHeadingOffset180() {
        super();
    }

    // Called once when the command executes
    protected void initialize() {    	
    	Robot.drive.setHeadingOffset(180.0);
    }
}
