package org.usfirst.frc.team6201.robot.commands.gears;

import org.usfirst.frc.team6201.robot.commands.DriveTimeCmd;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * 
 * TODO: FINISH
 * 
 * @author David Matthews
 * @author Adriana Massie
 *
 */
public class OuterStationGearAuto extends CommandGroup {

    public OuterStationGearAuto() {
    	
    	addSequential(new DriveTimeCmd(1));
    	addSequential(new AutoPosRobotGearDeliveryCmd());
    	addSequential(new DriveTimeCmd(0.17));
    	
    }
}
