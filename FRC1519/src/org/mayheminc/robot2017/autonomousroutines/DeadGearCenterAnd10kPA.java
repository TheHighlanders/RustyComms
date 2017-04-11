package org.mayheminc.robot2017.autonomousroutines;

import org.mayheminc.robot2017.commands.AlignToBoiler;
import org.mayheminc.robot2017.commands.ArmCarryState;
import org.mayheminc.robot2017.commands.CloseGearDoor;
import org.mayheminc.robot2017.commands.ContinueAligningToBoiler;
import org.mayheminc.robot2017.commands.DriveStraight;
import org.mayheminc.robot2017.commands.DriveStraightForTime;
import org.mayheminc.robot2017.commands.DriveStraightOnHeading;
import org.mayheminc.robot2017.commands.DriveStraightOnHeadingBasedOnAllianceColor;
import org.mayheminc.robot2017.commands.EnableShooter;
import org.mayheminc.robot2017.commands.EnableShooterLoader;
import org.mayheminc.robot2017.commands.GoToHeading;
import org.mayheminc.robot2017.commands.GoToHeadingBasedOnAllianceColor;
import org.mayheminc.robot2017.commands.InitRobotSubsystems;
import org.mayheminc.robot2017.commands.PlaceGear;
import org.mayheminc.robot2017.commands.PushGearOut;
import org.mayheminc.robot2017.commands.ReleaseHopperDeployers;
import org.mayheminc.robot2017.commands.ZeroGyro;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DeadGearCenterAnd10kPA extends CommandGroup {

	public DeadGearCenterAnd10kPA() {
		addParallel(new InitRobotSubsystems() );
		addParallel(new EnableShooter());
		addSequential(new DriveStraight(0.4,DriveStraight.DistanceUnits.INCHES, 40));

		addSequential(new DriveStraightForTime(0.15, 1.8));  // approx 30 inches
		addSequential(new PlaceGear());

		addSequential(new DriveStraight(-0.3, DriveStraight.DistanceUnits.INCHES, -8));

		addParallel(new ArmCarryState());
		addSequential(new GoToHeadingBasedOnAllianceColor(90.0, 20.0));

		addSequential(new DriveStraightOnHeadingBasedOnAllianceColor(0.40, DriveStraightOnHeading.DistanceUnits.INCHES, 11.0*5.0, 110.0));
		addSequential(new AlignToBoiler());

		addParallel(new ContinueAligningToBoiler());
		addSequential(new EnableShooterLoader());
	}
}
