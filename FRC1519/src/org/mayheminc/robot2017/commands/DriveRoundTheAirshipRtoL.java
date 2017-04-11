package org.mayheminc.robot2017.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;;

/**
 *
 */
public class DriveRoundTheAirshipRtoL extends CommandGroup {

    public DriveRoundTheAirshipRtoL() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

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
        
        // drive backwards across the far side of the field, right to left
        addSequential(new DriveStraightOnHeading(-0.65, DriveStraightOnHeading.DistanceUnits.INCHES, 11.0*12, 90.0));
        
        // drive backwards down the field
        addSequential(new DriveStraightOnHeading(-1.0, DriveStraightOnHeading.DistanceUnits.INCHES, 42.0*12, 0.0));
        
        // wait 10 seconds (to prevent the command from repeating itself)
        addSequential(new Delay (10000));
    }
}
