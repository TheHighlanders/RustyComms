package org.usfirst.frc.team6201.robot.commands;

import org.usfirst.frc.team6201.robot.Robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.TimedCommand;

/**
 * 
 * Command for activating pneumatics.
 * 
 * @author Owen Chiu
 *
 */
public class SolenoidStuff extends TimedCommand {

	Solenoid s;
	
    public SolenoidStuff(double timeout,Solenoid s) {
        super(timeout);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.pn);
        
        this.s = s;
    }

    // Called just before this Command runs the first time
    protected void initialize(){ 
    
    }
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//sets solenoid on
    	s.set(true);
    
    }

    // Called once after timeout
    protected void end() {
    	//sets solenoid off
    	s.set(false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	this.end();
    }
}
