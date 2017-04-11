package org.mayheminc.robot2017.autonomousroutines;

import org.mayheminc.robot2017.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class SelectAutonomousDelay extends InstantCommand {
	int m_delta = 0;
    public SelectAutonomousDelay(int delta) {
        super();
        setRunWhenDisabled(true);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        m_delta = delta;
    }

    // Called once when the command executes
    protected void initialize() {
    	Robot.autonomous.adjustDelay(m_delta);
    	//DriverStation.reportError("Adjusting delay to " + Robot.autonomous.getDelay(), false);
    }

}
