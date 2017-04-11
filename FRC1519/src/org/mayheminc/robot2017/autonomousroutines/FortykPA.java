package org.mayheminc.robot2017.autonomousroutines;

import org.mayheminc.robot2017.Robot;
import org.mayheminc.robot2017.commands.DriveStraight.DistanceUnits;
import org.mayheminc.robot2017.commands.DriveStraightWhileStrafing.DriveDistanceUnits;
import org.mayheminc.robot2017.commands.DriveStraightWhileStrafing.StrafeDistanceUnits;
import org.mayheminc.robot2017.commands.*;
import org.mayheminc.robot2017.autonomousroutines.*;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * This autonomous program assumes the robot starts next to the boiler.
 * It drives forward, then forward and towards the hopper.  Then it
 * aligns to the 0.0 heading to straighten out any wiggles.  Then
 * it strafes into the hopper and drives forward to collect all the fuel.
 * Align to the boiler and then fire!
 * 
 * This program uses ...BasedOnAllianceColor() to be used for both Red or Blue 
 * alliance. 
 */
public class FortykPA extends CommandGroup {

    public FortykPA() {
    	addParallel(new EnableShooter());
    	
    	// below is the content of a modified InitRobotSubsystems.java to ensure that
    	// ZeroGyro finishes before SetHeadingOffset180() is called.
    	addSequential( new ZeroGyro() );
    	addParallel( new LocateZeroArmEncoder() );
    	addParallel( new ReleaseHopperDeployers() );
    	
    	addSequential(new SetHeadingOffset180());
    	
    	// below was -36 inches at start of Pine Tree; changed to 30 at event based on video
    	addSequential(new DriveStraightOnHeading(-0.6, DriveStraightOnHeading.DistanceUnits.INCHES, -30, 180.0));
    	    	
    	// below was -36 inches at start of Pine Tree; changed to 24 after event based on video
    	addSequential(new DriveStraightWhileStrafingBasedOnAllianceColor(-0.4, DriveDistanceUnits.INCHES, -24,
    												-0.7, StrafeDistanceUnits.INCHES, 45));    	
    	addSequential(new GoToHeading(180.0));
    	
    	addSequential(new StrafeForTimeBasedOnAllianceColor(-0.85, 0.65));
    	
//    	addSequential(new EnableThumper());
    	addSequential(new ReverseThumper());
    	
    	// below was 12 inches at start of Pine Tree; changed to 6 inches based on video; then back to 10 inches
    	addSequential(new DriveStraight(0.4, DriveStraight.DistanceUnits.INCHES, 10));

    	// KBS NOTE:  Consider replacing above command with the one below
    	// (Did this after last Pine Tree practice match, before first Qual match.)
    	// NOTE:  Didn't work in Qual02!  Stalled out while trying to strafe (for entire auto period!)
//    	addSequential(new DriveStraightWhileStrafingBasedOnAllianceColor(0.4, DriveDistanceUnits.INCHES, 6,
//				-0.7, StrafeDistanceUnits.INCHES, 4));   
    	
    	//Do not pull away from the hopper
    	//addSequential(new Delay(750);
    	//addSequential(new StrafeForDistanceBasedOnAllianceColor(StrafeForDistance.StrafeDistanceUnits.INCHES, 3, 0.6));
    	
    	addSequential(new AlignToBoiler());
    	addSequential(new EnableThumper());
    	
    	addParallel(new ContinueAligningToBoiler());
    	
    	addSequential(new EnableShooterLoader());    	
    }
}
