package org.mayheminc.robot2017.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class InitRobotSubsystems extends CommandGroup {

    public InitRobotSubsystems() {
    	addSequential( new ZeroGyro() );
    	addParallel( new LocateZeroArmAndReadyToPlace() );
    	addParallel( new ReleaseHopperDeployers() );
    }
}
