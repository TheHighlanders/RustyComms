package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;
import org.mayheminc.robot2017.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 * Turn the Shooter Loader On or Off.
 */
public class EnableShooterLoader extends Command {
	
    public EnableShooterLoader() {
        super();
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        
        requires(Robot.shooterLoader);
    }
    
    protected void initialize() {
    	Robot.shooterLoader.turnOn();
    }
    
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (Robot.shooter.isUpToSpeed()) {
    		Robot.shooterLoader.turnOn();
    	}
//    	else { don't stop shooting once we start shooting.
//    		Robot.shooterLoader.turnOff();
//    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.shooterLoader.turnOff();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.shooterLoader.turnOff();
    }
}

