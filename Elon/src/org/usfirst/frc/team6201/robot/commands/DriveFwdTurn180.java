package org.usfirst.frc.team6201.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DriveFwdTurn180 extends CommandGroup {

    public DriveFwdTurn180() {
   
    	DriveTimeCmd df = new DriveTimeCmd(1, 0.25);
    	TurnAngleCmd turn180 = new TurnAngleCmd(180);
    	
    	addSequential(df);
    	addSequential(turn180);
  

    }
}
