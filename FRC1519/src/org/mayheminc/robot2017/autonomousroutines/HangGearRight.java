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
import org.mayheminc.robot2017.commands.PushGearOut;
import org.mayheminc.robot2017.commands.ReleaseHopperDeployers;
import org.mayheminc.robot2017.commands.ZeroGyro;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class HangGearRight extends CommandGroup {

    public HangGearRight() {
	   	
	addParallel(new InitRobotSubsystems() );
//	addParallel(new EnableShooter());
	addSequential(new DriveStraight(0.4, DriveStraight.DistanceUnits.INCHES, 4.4*12.0));
	
	addSequential(new GoToHeading(-60.0, 4.0));
	
	addSequential(new DriveStraightOnHeading(0.4, DriveStraightOnHeading.DistanceUnits.INCHES, 12.0, -60.0));
	
	addSequential(new AlignToLift(5));
	addSequential(new PlaceGear());

	
    addSequential(new DriveStraight(-0.5, DriveStraight.DistanceUnits.INCHES, -8.0));
    
    addParallel(new ArmCarryState());
    
    addSequential(new DriveStraight(-0.5, DriveStraight.DistanceUnits.INCHES, -12.0 * 2));
    
    }
}
