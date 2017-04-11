package org.usfirst.frc.team6201.robot.commands.rgb;

import org.usfirst.frc.team6201.robot.Robot;
import org.usfirst.frc.team6201.robot.gearVision.GearVisionCollator;
import org.usfirst.frc.team6201.robot.subsystems.RobotRGB;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoRGB extends Command {

	private double[] target;
	private RobotRGB rgb;
	
    public AutoRGB() {
    	
    	rgb = Robot.rgb;
    	
    	requires(rgb);
    	
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	target = GearVisionCollator.getTarget();
    	
    	if(target == null) {
    		
    		rgb.setColour(1, 0, 0);
    		
    	} else {
    		
    		rgb.setColour(0, target[0], 1 - target[0]);
    		
    	}
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
