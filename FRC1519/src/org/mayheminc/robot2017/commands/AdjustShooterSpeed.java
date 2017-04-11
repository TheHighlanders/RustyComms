package org.mayheminc.robot2017.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;

import org.mayheminc.robot2017.*;


public class AdjustShooterSpeed extends InstantCommand {
	double m_Amount;
	
    public AdjustShooterSpeed(double Amount) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.shooter);
    	m_Amount = Amount;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.shooter.adjustShooterSpeed(m_Amount);
    }

}
