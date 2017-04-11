package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;
import org.mayheminc.robot2017.subsystems.GearFloorPickerUpper;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class OperateGearRollers extends Command {

//	double m_startTime;
//	double m_desiredTime;
	GearFloorPickerUpper.RollerAction m_rollerAction;
	
	public OperateGearRollers(GearFloorPickerUpper.RollerAction rollerAction) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
//		m_desiredTime = 0.45;
		m_rollerAction = rollerAction;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
//    	Robot.gearFloorPickerUpper.operateRollers(m_rollerAction);
//    	m_startTime = Timer.getFPGATimestamp();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
//    	Robot.gearFloorPickerUpper.operateRollers(m_rollerAction);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
//		double diff = Timer.getFPGATimestamp() - m_startTime;
//		diff = Math.abs(diff);
//		return (diff >= m_desiredTime); 
		return false;
	}

    // Called once after isFinished returns true
    protected void end() {
    	// if this action was "IN", then the "resting state" is "RETAIN"; otherwise, "STOP"
    	if (m_rollerAction == GearFloorPickerUpper.RollerAction.IN) {
//    		Robot.gearFloorPickerUpper.operateRollers(GearFloorPickerUpper.RollerAction.RETAIN);
    	} else {
//    		Robot.gearFloorPickerUpper.operateRollers(GearFloorPickerUpper.RollerAction.STOP);
    	}
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	// if this action was "IN", then the "resting state" is "RETAIN"; otherwise, "STOP"
    	if (m_rollerAction == GearFloorPickerUpper.RollerAction.IN) {
//    		Robot.gearFloorPickerUpper.operateRollers(GearFloorPickerUpper.RollerAction.RETAIN);
    	} else {
//    		Robot.gearFloorPickerUpper.operateRollers(GearFloorPickerUpper.RollerAction.STOP);
    	}
    }
}
