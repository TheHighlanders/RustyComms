package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class SetDesiredHeading extends InstantCommand {
	double m_desiredHeading;
	
    public SetDesiredHeading(double desiredHeading) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	m_desiredHeading = desiredHeading;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drive.setDesiredHeading(m_desiredHeading);
    }

}
