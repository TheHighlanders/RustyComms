package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class LocateZeroArmEncoder extends Command {

	private double m_lastPos;
	private int m_counter;
	
	private final double UNSATISFACTORY_PROGRESS = 2.0;
	private final int MAX_UNSATISFACTORY_PROGRESS_COUNTS = 10;
    public LocateZeroArmEncoder() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	m_lastPos = Robot.gearFloorPickerUpper.getArmEncoder(); 
    	m_counter = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.gearFloorPickerUpper.setZeroingState();
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	double delta = Math.abs(Robot.gearFloorPickerUpper.getArmEncoder() - m_lastPos);
    	
    	if (delta < UNSATISFACTORY_PROGRESS) {
    		m_counter++;
    	} else {
    		m_counter = 0;
    	} 	
    	m_lastPos = Robot.gearFloorPickerUpper.getArmEncoder();
        return (m_counter >= MAX_UNSATISFACTORY_PROGRESS_COUNTS);
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.gearFloorPickerUpper.zeroArmEncoder();
    	Robot.gearFloorPickerUpper.setCarryState();
    	DriverStation.reportWarning("Arm Encoder successfully zeroed", false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	DriverStation.reportError("LocateZeroArmEncoder encountered unexpected interruption", true);
    }
}
