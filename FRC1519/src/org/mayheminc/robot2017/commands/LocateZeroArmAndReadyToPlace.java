package org.mayheminc.robot2017.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.mayheminc.robot2017.commands.*;

/**
 *
 */
public class LocateZeroArmAndReadyToPlace extends CommandGroup {

    public LocateZeroArmAndReadyToPlace() {
    	addSequential(new LocateZeroArmEncoder());
        addSequential(new ArmReadyToPlaceState());
    }
}
