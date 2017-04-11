package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 * Turn on the Shooter wheel.
 * @author User
 *
 */
public class EnableShooter extends InstantCommand {

    public EnableShooter() {        
    	requires(Robot.shooter);
    	
    }

    // Called once when the command executes
    protected void initialize() {
    	Robot.shooter.turnOn();
    	DriverStation.reportError("Enabling Shooter", false);
    }

}
