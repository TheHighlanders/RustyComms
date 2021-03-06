package org.usfirst.frc.team6201.robot.commands.gears;

import org.usfirst.frc.team6201.robot.commands.DriveTimeCmd;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * The routine to autonomously deliver the gear to the peg during auto if our robot is going for the center lift.
 * 
 * TODO: FINISH
 * 
 * @author Adriana Massie
 * @author David Matthews
 */
public class CenterStationAutoCmdGroup extends CommandGroup {

	/**
	 * The routine of commands to run if our robot is going to deliver the gear to the center lift during auto.
	 */
    public CenterStationAutoCmdGroup() {
    	//TODO: experimentally determine what the correct value of time for here.
    	
    	
        addSequential(new CenterStationAutoPos());
   	addSequential(new DriveTimeCmd(5,0.2));
        
    }
}