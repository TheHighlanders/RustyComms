package org.mayheminc.robot2017.autonomousroutines;

import org.mayheminc.robot2017.commands.AlignToBoiler;
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
public class DeadGearLeftAnd10kPA extends CommandGroup {

    public DeadGearLeftAnd10kPA() {
    	addParallel(new InitRobotSubsystems() );
    	addParallel(new EnableShooter());
    	addSequential(new DriveStraight(0.4, DriveStraight.DistanceUnits.INCHES, 4.4*12));
    	
    	addSequential(new GoToHeading(60.0, 4.0));
    	
    	addSequential(new DriveStraightOnHeading(0.4, DriveStraightOnHeading.DistanceUnits.INCHES, 12.0, 60.0));

    	addSequential(new PlaceGear());
    	
        addSequential(new DriveStraight(-0.5, DriveStraight.DistanceUnits.INCHES, -8.0));
        
        addParallel(new ArmCarryState());
        
        addSequential(new GoToHeading(-135.0));
        addSequential(new DriveStraightOnHeading(0.40, DriveStraightOnHeading.DistanceUnits.INCHES, 20.0, -135.0));
        addSequential(new AlignToBoiler());
        addParallel(new ContinueAligningToBoiler());
        addSequential(new EnableShooterLoader()); 
    }
}
