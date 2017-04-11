package org.mayheminc.robot2017.autonomousroutines;

import org.mayheminc.robot2017.commands.DriveStraight;
import org.mayheminc.robot2017.commands.GoToHeading;
import org.mayheminc.robot2017.commands.GoToHeadingBasedOnAllianceColor;
import org.mayheminc.robot2017.commands.InitRobotSubsystems;
import org.mayheminc.robot2017.commands.ReleaseHopperDeployers;
import org.mayheminc.robot2017.commands.ZeroGyro;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class CrossBaseline extends CommandGroup {

    public CrossBaseline() {
        
    	addParallel( new InitRobotSubsystems() );
    	addSequential(new DriveStraight(0.3, DriveStraight.DistanceUnits.INCHES, 12.0*12));
    	addSequential(new DriveStraight(-0.3, DriveStraight.DistanceUnits.INCHES, 6.0*12));
    	addSequential(new GoToHeadingBasedOnAllianceColor(60.0));
    }
}
