package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AlignToLift_COMPLEX extends Command {

	private final int TOLERANCE = 7;
	private double m_desiredHeading;
	
	private enum States { HEADING_ALIGN, STRAFE_ALIGN };
	private States m_state;
    public AlignToLift_COMPLEX() {
            super();  
            DriverStation.reportError("constructing aligntolift", false);
    }
    
    /*
     * returns timer's time in seconds
     */
    

    // Called just before this Command runs the first time
    protected void initialize() {
    	m_state = States.HEADING_ALIGN;
    	double currentHeading = Robot.drive.getHeading();
    	
    	//find nearest preset heading
    	if(currentHeading > (Robot.drive.LEFT_HEADING / 2)){
    		m_desiredHeading = Robot.drive.LEFT_HEADING;
    	}else if (currentHeading < (Robot.drive.RIGHT_HEADING / 2 )){
    		m_desiredHeading = Robot.drive.RIGHT_HEADING;
    	}else{
    		m_desiredHeading = Robot.drive.STRAIGHT_HEADING;
    	}
    	
    	DriverStation.reportError("initializing with desired heading " + m_desiredHeading, false);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(m_state == States.HEADING_ALIGN){
    		Robot.drive.goToHeading(m_desiredHeading);
    		DriverStation.reportError("Alining to heading " + m_desiredHeading, false);
    	}else{
    		Robot.drive.autoTargetToLift();
    		DriverStation.reportError("Aligning via strafing", false);
    	}   	   	
    	
    	if(Math.abs(Robot.drive.getHeading() - m_desiredHeading) <= TOLERANCE){
    		m_state = States.STRAFE_ALIGN;
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	//finish if we are aligned AND the heading is right
    	return false;
        //return (Robot.drive.isAlignedToLift() && (m_state == States.STRAFE_ALIGN));
        		//|| ((Timer.getFPGATimestamp() - m_initTime) > m_maxTime));
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drive.stop();
    	new Print("Finished Aligning to Lift");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.drive.stop();
    	new Print("Interrupted AlignToLift()");
    }
}
