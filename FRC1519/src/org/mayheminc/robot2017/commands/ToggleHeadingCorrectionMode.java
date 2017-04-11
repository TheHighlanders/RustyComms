package org.mayheminc.robot2017.commands;
import edu.wpi.first.wpilibj.command.InstantCommand;
import org.mayheminc.robot2017.Robot;

public class ToggleHeadingCorrectionMode extends InstantCommand {

    public ToggleHeadingCorrectionMode() {
        super();       
    }

    // Called once when the command executes
    protected void initialize() {
    	Robot.drive.setHeadingCorrectionMode(!Robot.drive.getHeadingCorrectionMode());
    }
}