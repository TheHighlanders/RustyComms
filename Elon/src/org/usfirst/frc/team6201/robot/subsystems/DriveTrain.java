package org.usfirst.frc.team6201.robot.subsystems;

import org.usfirst.frc.team6201.robot.RobotMap;
import org.usfirst.frc.team6201.robot.commands.ArcadeDriveCmd;
import org.usfirst.frc.team6201.robot.dataLogger.DataCollator;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * The interface between the robot code and the actuators and sensors involved in moving the robot. 
 * Right now this is just the motors and gyro, but this will probably grow to include encoders. 
 * 
 * TODO: add getter and setter methods for the other method of the CANTalon classes.
 * 
 * @author Baxter Ellard
 * @author David Matthews
 */
public class DriveTrain extends Subsystem {
	
	//CANTalon refers to a motor controller, so it means a motor for all intents & purposes.
	private CANTalon left1;
	private CANTalon left2;
	private CANTalon right1;
	private CANTalon right2;
	
	//whether this is public or not is TBD
	public static int fowardOrReverse = 1;
	
	// Sensors
	private ADXRS450_Gyro gyro = new ADXRS450_Gyro();
	
	/**
	 * Constructor, sets up motors, prevents brownouts and minimizes pedestrian casualties. 
	 */
	public DriveTrain() {
		left1 = new CANTalon(RobotMap.LEFT_DRIVE1);
		left2 = new CANTalon(RobotMap.LEFT_DRIVE2);
		right1 = new CANTalon(RobotMap.RIGHT_DRIVE1);
		right2 = new CANTalon(RobotMap.RIGHT_DRIVE2);
		
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
	
	/**
	 * ArcadeDriveCmd will always run when other commands are not busy.
	 * This will allow operator control when the robot is not driving itself around.
	 */
    public void initDefaultCommand() {
    	
    	setDefaultCommand(new ArcadeDriveCmd());
    	
    }
    
    /**
     * Updates the motors with what speed to drive at.
     * TODO: what is the robot "Front", and what value is that? 1 or -1?
     * TODO: Use the custom joystick that Owen made
     * 
     * @param leftPower		Double speed of left motors. Range -1 to 1
     * @param rightPower	Double speed of right motors. Range -1 to 1
     */
    public void driveLR(double leftPower, double rightPower) {
    	
    	left1.set(leftPower);
    	left2.set(leftPower);
    	right1.set(rightPower);
    	right2.set(rightPower);
    	
    	DataCollator.motorSpeedLeft.setVal(leftPower);
    	DataCollator.motorSpeedRight.setVal(rightPower);

    }
    
    /**
     * Sets the motors to be off.
     */
    public void stop() {
    	
    	this.driveLR(0, 0);
   
    }
    
    
    /**
     * Calibrates gyro (takes 5 seconds while robot does nothing)
     * Do this when robot first turns on.
     */
    public void calibrateGyro() {
    	
    	gyro.calibrate();
    	
    }
    
    /**
     * Sets the drivetrain gyro back to 0 degrees
     */
    public void resetGyro() {
    	
    	gyro.reset();
    	
    }
    
    /**
     * @return the current rate of turning in degrees per second
     */
    public double getGyroRate() {
    	
    	return gyro.getRate();
    	
    }
    
    /**
     * 
     * @return gets an approximation of the gyro angle since reset was last called from an accumulation using the FPGA. Will accumulate error over time.
     * 
     */
    public double getGyroAngle() {
    	
    	return gyro.getAngle();
    	
    }
}

