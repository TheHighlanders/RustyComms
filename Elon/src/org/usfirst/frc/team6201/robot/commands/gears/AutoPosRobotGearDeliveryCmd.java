package org.usfirst.frc.team6201.robot.commands.gears;

import org.usfirst.frc.team6201.robot.Robot;
import org.usfirst.frc.team6201.robot.gearVision.GearVisionCollator;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Drives the robot towards the peg for the remainder of the gear delivery command.
 * TODO: How fast can we drive before motion blur / camera lag becomes an issue? test this!
 * 
 * @author David Matthews
 * @auothor Adriana Massie
 */
public class AutoPosRobotGearDeliveryCmd extends Command {
	/**
	 * If we close to the peg, we may loose tracking of the target.
	 * This boolean is a flag so that when we loose tracking of the peg we know to drive forward if we are close.
	 */
	private boolean closeToPeg = false;
	
	/**
	 * An array of doubles with length of 4.  
	 * 
	 * target [0] holds the x position of the center of the target. 0 is left of frame, 1 is right  
	 * 
	 * target [1] holds the y position of the center of the target. 0 is top of frame, 1 is bottom  
	 * 
	 * target [2] holds the width of the target. 0 is no width, 1 is full frame.  
	 * 
	 * target [3] holds the height of the target. 0 is no height, 1 is full frame.  
	 *   
	 * target is null if we do not currently have tracking of the target.
	 * The cameras we are using are Axis M1045LW cameras.  
	 */
	private double [] target;
	
	
	/**
	 * Constructor for this command, requires use of the drive train.
	 */
    public AutoPosRobotGearDeliveryCmd() {
        requires(Robot.dt);
    }
    
    
    protected void initialize() {
    }

    /**
     * Attempts to get an updated vision target and center the target, and drive towards it.
     * if no target is available and we are close to the peg, then we drive straight forward,
     * else we hunt for the target by turning in a circle
     */
    protected void execute() {
    	

    	target = GearVisionCollator.getTarget();
    	
    	// case: lost tracking of target
    	if (target == null) {
    		
    		// if we loose tracking of the peg, but the robot is close, drive forward.
    		if(closeToPeg){
        		//TODO: stop when hit peg.
        		Robot.dt.driveLR(0.1, 0.1);
        	}
    		else {
    			Robot.dt.driveLR(0.1, -0.1);
    		}
    		
    		
    	}
    	// case: have tracking of target.
    	else {
    		// center the target in the frame.
        	if (target[3] > 0.5){
        		closeToPeg = true;
        	}
        	
        	if (target[0] < 0.45){
        		Robot.dt.driveLR(0.1, -0.1);
        	}
        	else if (target[0] > 0.55){
        		Robot.dt.driveLR(-0.1, 0.1);
        	}
        	// drive towards target.
        	else {
        		Robot.dt.driveLR(0.1, 0.1);
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
