package org.mayheminc.robot2017.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.InstantCommand;
import org.mayheminc.robot2017.MayhemIterativeRobot;
import org.mayheminc.robot2017.Robot;

/**
 *
 */
public class CheckInWithFMS extends InstantCommand {

    public CheckInWithFMS() {
        super();
        setRunWhenDisabled(true);
        DriverStation.reportError("CheckInWithFMS.CheckInWithFMS()", false);
        
    }

    // Called once when the command executes
    protected void initialize() {
    	DriverStation.reportError("Checking in with FMS", false);
    	MayhemIterativeRobot.checkInFMS();
    }

}
