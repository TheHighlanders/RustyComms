package org.usfirst.frc.team6201.robot.commands.gears;

import org.usfirst.frc.team6201.robot.Robot;
import org.usfirst.frc.team6201.robot.gearVision.GearVisionCollator;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Drives the robot towards the peg for the remainder of the gear delivery Cmd.
 */
public class AutoPosRobotGearDeliveryCmd extends Command {
	private boolean closeToPeg = false;
	private double [] target;
	
	
    public AutoPosRobotGearDeliveryCmd() {
        requires(Robot.dt);
    }
    
    protected void initialize() {
    }

    
    protected void execute() {
    	if (closeToPeg){
    		Robot.dt.driveLR(0.2, 0.2);
    	}
    	else {
        	target = GearVisionCollator.getTarget();
        	
        	if (target == null) {
        		Robot.dt.driveLR(0.2, -0.2);
        	}
        	
        	if (target[0] < 0.45){
        		Robot.dt.driveLR(0.2, -0.2);
        	}
        	else if (target[0] > 0.55){
        		Robot.dt.driveLR(-0.2, 0.2);
        	}
        	else {
        		Robot.dt.driveLR(0.2, 0.2);
        	}
        	//TODO: use an ultrasonic sensor maybe? not nessicarly needed
        	if (target[3] > 0.5){
        		closeToPeg = true;
        	}
    	}
    	
    	
    
    }

    // use an ultrasonic sensor to determine if this command no longer needs to run?
    // or maybe use accelormeter for hitting wall?
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.dt.driveLR(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
