package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class PlaceGear extends Command {
//	private double m_startPos;
	private final double DESIRED_DRIVE_DISPLACEMENT = 16.0 / Robot.drive.DISTANCE_PER_PULSE;	
    private final int PLACE_DELAY_LOOPS = 10;  // how many loops to start placing before driving away
    private int m_placeDelayLoops = 0;
    
    public PlaceGear() {
        
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	m_placeDelayLoops = 0;
//    	m_startPos = Robot.drive.getRightEncoder();
    	Robot.drive.saveInitialWheelDistance();
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	m_placeDelayLoops++;
//    	if (m_placeDelayLoops > PLACE_DELAY_LOOPS) {
    		Robot.drive.placeGear();
//    	} else {
//    		Robot.gearFloorPickerUpper.placeGear();
//    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
//        return (Math.abs(m_startPos - Robot.drive.getRightEncoder()) > DESIRED_DRIVE_DISPLACEMENT);
    	return (Robot.drive.getWheelDistance() > DESIRED_DRIVE_DISPLACEMENT);
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drive.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.drive.stop();
    }
}
