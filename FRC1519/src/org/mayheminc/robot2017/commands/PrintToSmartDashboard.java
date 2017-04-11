package org.mayheminc.robot2017.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PrintToSmartDashboard extends InstantCommand {
	String m_String;
	
    public PrintToSmartDashboard(String Value) {
        super();
        setRunWhenDisabled(true);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        m_String = Value;
        //DriverStation.reportError("Create Print to Daschboard",  false);
        
        DriverStation.reportError("Command Update " + m_String + " const", false);
    }

    // Called once when the command executes
    protected void initialize() {
    	SmartDashboard.putString("Command Update", m_String);
       // DriverStation.reportError(" Print to Daschboard Init",  false);
    }

}
