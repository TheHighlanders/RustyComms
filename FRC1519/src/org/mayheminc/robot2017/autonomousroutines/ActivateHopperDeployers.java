package org.mayheminc.robot2017.autonomousroutines;

import org.mayheminc.robot2017.commands.ReleaseHopperDeployers;
import org.mayheminc.robot2017.commands.ZeroGyro;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ActivateHopperDeployers extends CommandGroup {

    public ActivateHopperDeployers() {
    	addParallel(new ZeroGyro());
    	addSequential(new ReleaseHopperDeployers());
        
    }
}
