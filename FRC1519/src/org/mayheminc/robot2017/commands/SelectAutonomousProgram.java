package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class SelectAutonomousProgram extends InstantCommand {

	int m_delta;
	
    public SelectAutonomousProgram(int delta) {
        super();
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.autonomous);
        setRunWhenDisabled(true);
        
        m_delta = delta;
    }

    // Called once when the command executes
    protected void initialize() {
    	Robot.autonomous.adjustProgramNumber(m_delta);
    }

}
