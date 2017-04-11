package org.mayheminc.robot2017.autonomousroutines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.mayheminc.robot2017.commands.*;

/**
 *
 */
public class HangGearCenter extends CommandGroup {

    public HangGearCenter() {
    	addParallel(new InitRobotSubsystems() );
        addSequential(new DriveStraight(0.4,DriveStraight.DistanceUnits.INCHES, 40));
        
        //addSequential(new AlignToLift(5));
        
        addSequential(new DriveStraightForTime(0.15, 1.8));  // approx 30 inches
        addSequential(new PlaceGear());
        
        addSequential(new DriveStraight(-0.3, DriveStraight.DistanceUnits.INCHES, -8));
        addParallel(new ArmCarryState());
        addSequential(new DriveStraight(-0.4, DriveStraight.DistanceUnits.INCHES, -10));
        
    }
}
