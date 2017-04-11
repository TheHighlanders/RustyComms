package org.mayheminc.robot2017.autonomousroutines;

import org.mayheminc.robot2017.commands.*;
import org.mayheminc.robot2017.autonomousroutines.*;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class TEST extends CommandGroup {

    public TEST() {
        
//    	addParallel(new InitRobotSubsystems() );
    	addSequential( new DriveStraightForDistanceWhileStrafingForTimeBasedOnAllianceColor(0.4,  DriveStraightForDistanceWhileStrafingForTime.DriveDistanceUnits.INCHES, 12, -0.6, 1.0));
    	
    	//addSequential(new LocateZeroArmEncoder());
    	
//    	addSequential(new ZeroGyro());
//    	addSequential(new EnableShooter());
//    	addSequential(new Print("Checkpoint"));
//    	addSequential(new AlignToBoiler());
//    	
//    	addSequential(new Delay(1000));
//    	addSequential(new EnableShooterLoader());
    	
    	//addSequential(new DriveStraightWhileStrafingBasedOnAllianceColor(0.2, DriveStraightWhileStrafing.DriveDistanceUnits.INCHES, 6, 
    	//											-0.3, DriveStraightWhileStrafing.StrafeDistanceUnits.ENCODER_TICKS, 100000));
    	
    }
}
