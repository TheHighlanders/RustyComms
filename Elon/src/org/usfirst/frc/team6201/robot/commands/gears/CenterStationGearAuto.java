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
public class CenterStationGearAuto extends CommandGroup {

	/**
	 * The routine of commands to run if our robot is going to deliver the gear to the center lift during auto.
	 */
    public CenterStationGearAuto() {
    	//TODO: experimentally determine what the correct value of time for here.
    	addSequential(new DriveTimeCmd(0));
    	
        addSequential(new AutoPosRobotGearDeliveryCmd());
      //  addSequential(new DeliverGearCmd());
        //addSequential(new DeliverGearCmd());
        
        
        
        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    }
}
