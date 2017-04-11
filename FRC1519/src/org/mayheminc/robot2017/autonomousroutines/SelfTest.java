package org.mayheminc.robot2017.autonomousroutines;

import org.mayheminc.robot2017.commands.Delay;
import org.mayheminc.robot2017.commands.DisableShooter;
import org.mayheminc.robot2017.commands.DisableShooterLoader;
import org.mayheminc.robot2017.commands.DriveStraight;
import org.mayheminc.robot2017.commands.DriveStraight.DistanceUnits;
import org.mayheminc.robot2017.commands.EnableShooter;
import org.mayheminc.robot2017.commands.EnableShooterLoader;
import org.mayheminc.robot2017.commands.StrafeForDistance;
import org.mayheminc.robot2017.commands.StrafeForDistance.StrafeDistanceUnits;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class SelfTest extends CommandGroup {

    public SelfTest() {
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
    	
    	// test the 4 corner wheels
    	addSequential(new DriveStraight(0.2, DistanceUnits.INCHES, 2*12.0));
    	addSequential(new DriveStraight(-0.2, DistanceUnits.INCHES, 2*12.0));
    	
    	// test the H pod
    	addSequential(new StrafeForDistance(0.2, StrafeDistanceUnits.INCHES, 2*12.0));
    	addSequential(new StrafeForDistance(-0.2, StrafeDistanceUnits.INCHES, 2*12.0));
    	
    	// test the shooter wheel
    	addSequential(new EnableShooter());
    	addSequential(new Delay(2000));
    	addSequential(new DisableShooter());
    	addSequential(new Delay(2000));
    	
    	// test the shooter loader
    	addSequential(new EnableShooterLoader());
    	addSequential(new Delay(2000));
    	addSequential(new DisableShooterLoader());
    }
}
