package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class ZeroArmEncoder extends InstantCommand {

    public ZeroArmEncoder() {
        super();        
        setRunWhenDisabled(true);
    }

    // Called once when the command executes
    protected void initialize() {
    	Robot.gearFloorPickerUpper.zeroArmEncoder();
    	DriverStation.reportError("In the command 'ZeroArmEncoder'", false);
    }

}
