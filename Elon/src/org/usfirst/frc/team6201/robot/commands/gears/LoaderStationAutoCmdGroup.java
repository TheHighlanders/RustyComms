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
public class LoaderStationAutoCmdGroup extends CommandGroup {

    public LoaderStationAutoCmdGroup() {
    	
    	addSequential(new LoaderStationAutoPos());
    	addSequential(new DriveTimeCmd(2,0.15));
    	
    }
}