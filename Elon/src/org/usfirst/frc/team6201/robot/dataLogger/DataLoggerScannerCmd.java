package org.usfirst.frc.team6201.robot.dataLogger;


import org.usfirst.frc.team6201.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Uses the DataLoggerFetcher subsystem to update the values stored in the DataCollator class.
 * 
 *@author David Matthews
 */
public class DataLoggerScannerCmd extends Command {

    public DataLoggerScannerCmd() {

    	requires(Robot.dlf);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    /**
     * Calls a bunch of methods in the Robot.dlf subsystem in order to update the DataCollator's DataField object's values.
     */
    protected void execute() {
    	Robot.dlf.setAccelX();
    	Robot.dlf.setAccelY();
    	Robot.dlf.setAccelZ();
    	Robot.dlf.setCurrent1();
    	Robot.dlf.setCurrent14();
    	Robot.dlf.setCurrent2();
    	Robot.dlf.setCurrent3();
    	Robot.dlf.setCurrent15();
    	Robot.dlf.setRate();
    	Robot.dlf.setTemp();
    	Robot.dlf.setTemp();
    	Robot.dlf.setVoltage();
       }

    /**
     * This command should always run
     */
    protected boolean isFinished() {
        return false;
    }

    /**
     * No cleanup required
     */
    protected void end() {
    }

    /**
     * May occur if the robot tells the data logger recorder to stop logging
     */
    protected void interrupted() {
    }
}
