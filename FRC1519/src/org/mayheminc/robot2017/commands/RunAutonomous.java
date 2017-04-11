package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Team1519
 */
public class RunAutonomous extends Command {
    private long startTime;
    Command command;
    
    public RunAutonomous() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
//        requires(Robot.autonomous);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        startTime = System.currentTimeMillis() + Robot.autonomous.getDelay() * 1000;
        command = Robot.autonomous.getSelectedProgram();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (System.currentTimeMillis() >= startTime) {
        	SmartDashboard.putString("robot trace", "RunAuto execute start");
            command.start();
            SmartDashboard.putString("robot trace", "RunAuto execute done");
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return System.currentTimeMillis() >= startTime;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
