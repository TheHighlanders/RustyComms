package org.usfirst.frc.team6201.robot.commands.rgb;

import org.usfirst.frc.team6201.robot.Robot;
import org.usfirst.frc.team6201.robot.subsystems.RobotRGB;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TeleOpRGB extends Command {

	private RobotRGB rgb;
	
	private double time;
	
    public TeleOpRGB() {

    	rgb = Robot.rgb;
    	
    	requires(rgb);
    
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	time = Timer.getFPGATimestamp();
    	
    	rgb.setColour((Math.sin(time / 3) + 1) * 0.5, (Math.sin(time / 7) + 1) * 0.5, (Math.sin(time / 5) + 1) * 0.5);
    	
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
