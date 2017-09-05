package org.usfirst.frc.team6201.robot.commands.rgb;

import org.usfirst.frc.team6201.robot.Robot;
import org.usfirst.frc.team6201.robot.subsystems.RobotRGB;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Final30RGB extends Command {

	private RobotRGB rgb;
	
    public Final30RGB() {
        
    //	rgb = Robot.rgb;
    	
    	//requires(rgb);
    	
    }
    
    public double getIntensity(double time) {
    	
    	if(time <= 1) {
    		
    		return Math.pow(time, 2);
    		
    	} else if(time <= 2) {
    		
    		return 1;
    		
    	} else if(time <= 3) {
    	
    		double scaledInput = -1 * (3 - time);
    		
    		return Math.pow(scaledInput, 2);
    		
    	} else {
    		
    		return 0;
    		
    	}
    	
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	double matchTime = Timer.getMatchTime();
    	double matchRemainder = matchTime % 3;
    	
    	if(matchRemainder < 1) {
    		
    		rgb.setColour(getIntensity(matchRemainder), 0, 0);
    		
    	} else if(matchRemainder < 2) {
    		
    		rgb.setColour(0, getIntensity(matchRemainder), 0);
    		
    	} else if(matchRemainder < 3) {
    		
    		rgb.setColour(0, 0, getIntensity(matchRemainder));
    		
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
