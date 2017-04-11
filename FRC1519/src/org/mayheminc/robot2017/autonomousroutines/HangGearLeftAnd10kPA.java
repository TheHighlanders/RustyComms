package org.mayheminc.robot2017.autonomousroutines;

import org.mayheminc.robot2017.commands.AlignToBoiler;
import org.mayheminc.robot2017.commands.AlignToLift;
import org.mayheminc.robot2017.commands.ArmCarryState;
import org.mayheminc.robot2017.commands.CloseGearDoor;
import org.mayheminc.robot2017.commands.ContinueAligningToBoiler;
import org.mayheminc.robot2017.commands.Delay;
import org.mayheminc.robot2017.commands.DriveStraight;
import org.mayheminc.robot2017.commands.DriveStraightForTime;
import org.mayheminc.robot2017.commands.DriveStraightOnHeading;
import org.mayheminc.robot2017.commands.EnableShooter;
import org.mayheminc.robot2017.commands.EnableShooterLoader;
import org.mayheminc.robot2017.commands.GoToHeading;
import org.mayheminc.robot2017.commands.InitRobotSubsystems;
import org.mayheminc.robot2017.commands.PlaceGear;
import org.mayheminc.robot2017.commands.Print;
import org.mayheminc.robot2017.commands.PushGearOut;
import org.mayheminc.robot2017.commands.ReleaseHopperDeployers;
import org.mayheminc.robot2017.commands.ZeroGyro;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class HangGearLeftAnd10kPA extends CommandGroup {

    public HangGearLeftAnd10kPA() {
    	addParallel(new InitRobotSubsystems() );
    	addParallel(new EnableShooter());
    	addSequential(new DriveStraight(0.4, DriveStraight.DistanceUnits.INCHES, 4.4*12));
    	
    	addSequential(new GoToHeading(60.0, 4.0));
    	
    	addSequential(new DriveStraightOnHeading(0.4, DriveStraightOnHeading.DistanceUnits.INCHES, 12.0, 60.0));
    	
    	addSequential(new AlignToLift(5.0));  // was 2.3 at WPI
//    	addSequential(new Print("Aligned and ready to go!"));
//    	
//    	addSequential(new DriveStraightForTime(0.2, 1.5));

    	addSequential(new PlaceGear());
//        addSequential(new Delay(500));
    	
        addSequential(new DriveStraight(-0.5, DriveStraight.DistanceUnits.INCHES, -8.0));
        
        addParallel(new ArmCarryState());
        
        addSequential(new GoToHeading(-135.0));
        addSequential(new DriveStraightOnHeading(0.40, DriveStraightOnHeading.DistanceUnits.INCHES, 20.0, -135.0));
        addSequential(new AlignToBoiler());
        addParallel(new ContinueAligningToBoiler());
        addSequential(new EnableShooterLoader());      

    }
    
    
//   @SSM: OLD ROUTINE
//	//@SSM 11:46AM 20170309: UNTESTED
//	//@SSM intended to be run in WPI Qual#11
//	//@SSM 03:00PM 20170309: Our test in Qual#11 failed due to camera networking weirdness
//	//@SSM this code should still be tested
//	addParallel(new ZeroGyro());
//	addParallel(new ReleaseHopperDeployers());
//	addSequential(new DriveStraight(0.6, DriveStraight.DistanceUnits.INCHES, 5.5*12));
//	
//	addSequential(new GoToHeading(60.0));
//	
//	addSequential(new DriveStraight(0.5, DriveStraight.DistanceUnits.INCHES, 6));
//	
//	addSequential(new AlignToLift(3));
//	
//	addSequential(new DriveStraightForTime(0.2, 1.5));
//	addSequential(new EnableShooter());
//	addSequential(new PushGearOut());
//    addSequential(new Delay(500));
//	
//    addSequential(new DriveStraight(-0.5, DriveStraight.DistanceUnits.INCHES, -2 * 12.0));
//    
//    addParallel(new CloseGearDoor());
//    addSequential(new GoToHeading(-135.0));
//    addSequential(new DriveStraight(0.45, DriveStraight.DistanceUnits.INCHES, 12.0*2));
//    addSequential(new AlignToBoiler());
//    addSequential(new EnableShooterLoader());
}
