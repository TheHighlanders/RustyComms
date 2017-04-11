package org.mayheminc.robot2017.commands;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class Print extends InstantCommand {
	String m_str;
    public Print(String str) {
        super();       
        m_str = str;
    }
    
    protected void initialize() {
    	DriverStation.reportError(m_str, false);
    }
}
