package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.*;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class DisableShooter extends InstantCommand {

    public DisableShooter() {
    	requires(Robot.shooter);        
    }

    // Called once when the command executes
    protected void initialize() {
    	Robot.shooter.turnOff();
    }
}
