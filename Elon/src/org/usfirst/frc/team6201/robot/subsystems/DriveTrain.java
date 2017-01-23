package org.usfirst.frc.team6201.robot.subsystems;

import org.usfirst.frc.team6201.robot.RobotMap;
import org.usfirst.frc.team6201.robot.commands.ArcadeDriveCmd;
import org.usfirst.frc.team6201.robot.dataLogger.DataCollator;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * @author Baxter Ellard
 */
public class DriveTrain extends Subsystem {

	private CANTalon left1 = new CANTalon(RobotMap.left1);
	private CANTalon left2 = new CANTalon(RobotMap.left2);
	private CANTalon right1 = new CANTalon(RobotMap.right1);
	private CANTalon right2 = new CANTalon(RobotMap.right2);
	
	public DriveTrain() {
		
		right1.setInverted(true);
		right2.setInverted(true);
		
		right1.setSafetyEnabled(true);
		right2.setSafetyEnabled(true);
		left1.setSafetyEnabled(true);
		left2.setSafetyEnabled(true);
		
		right1.setVoltageRampRate(24);
		right2.setVoltageRampRate(24);
		left1.setVoltageRampRate(24);
		left2.setVoltageRampRate(24);
	}
	
	
    public void initDefaultCommand() {
    	
    	setDefaultCommand(new ArcadeDriveCmd());
    	
    }
    
    public void driveLR(double leftPower, double rightPower) {
    	
    	left1.set(leftPower);
    	left2.set(leftPower);
    	right1.set(rightPower);
    	right2.set(rightPower);
    	
    	DataCollator.motorSpeedLeft.setVal(leftPower);
    	DataCollator.motorSpeedRight.setVal(rightPower);

    }
    
    public void stop() {
    	
    	this.driveLR(0, 0);
   
    }
}

