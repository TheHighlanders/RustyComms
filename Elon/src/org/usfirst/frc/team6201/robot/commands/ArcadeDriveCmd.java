package org.usfirst.frc.team6201.robot.commands;


import org.usfirst.frc.team6201.robot.Robot;
import org.usfirst.frc.team6201.robot.RobotMap;
import org.usfirst.frc.team6201.robot.dataLogger.DataCollator;
import org.usfirst.frc.team6201.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 * NEEDS TO BE REWRITTEN!!!
 * This command interfaces between the Operator Interface object (Robot.oi) and the DriveTrain object (Robot.dt)
 * to provide smooth intuitive joystick control with fine control at slow speeds, while still being able to reach full speed.
 * Recently added support for a third axis to control the sensitivity of the joystick.
 *
 *@author David Matthews
 *@author Adriana Massie
 *@author Baxter Ellard
 *
 */


public class ArcadeDriveCmd extends Command {


	
	/**
	 * Calibrated turning amount by throwing the X axis of the joystick through a tan function
	 */
	private double tanTurn; 
	
	/**
	 * Calibrated forward motion by throwing the Y axis of the joystick through a tan function
	 */
	private double tanPower; 
	
	/**
	 * Calculates how much the robot can turn based on what the processedPower is, how much room we have to play with the motors
	 * and how much turning is desired.
	 */
	private double processedOnceTurn; 
	
	/**
	 * 
	 */
	private double processedTwiceTurn;
	
	/**
	 * reserves 5% for turning at all times.
	 */
	private double processedPower; 

	/**
	 * Creates the sensitivity curve for the Y axis
	 */
	private final double TANDOMAIN_Y = 1.3; // used for sensitivity of joystick
	
	/**
	 * Creates the sensitivity curve for the X axis
	 */
	private final double TANDOMAIN_X = 1.2; // used for sensitivity of joystick // changed after reading competition
	
	/**
	 * TODO:
	 */
	private final double pTurnGain = 0.05; // This is used for allowing us to drive in a straight line.
											//We MUST test to find the appropriate  value for this.
	private final double gyroRateGain = 0.05; // This is used to make the desired turn rate as output by the Joystick match the 
	

	/**
	 * 
	 * @param rawVal	Value to be processed by the tangent function
	 * @param domain	Domain of the tangent function. Will effect shape of this mapping function curve.
	 * @return			A double corresponding mapped from the input rawVal via a tangent curve and the domain.
	 * 					provides high motion control at slow speeds, and full robot speed.
	 */
	private double scaledValTan (double rawVal, double domain){
		return Math.tan(rawVal*domain) / (Math.tan(domain));
	}
	
	/**
	 * Requires the Robot.dt Drivetrain subsystem to run.
	 * Used for resource allocation in WPILibj
	 */
    public ArcadeDriveCmd() {
    	requires(Robot.dt);
    }

    /**
     * Updates the DataLogger's state value, and resets the gyro to prepare for the start of the command
     */
    protected void initialize() {
    	//Robot.dt.calibrateGyro(); removed after reading.
    	Robot.dt.resetGyro();
    	DataCollator.state.setVal("ArcadeDriveCmdInit");
    	
    }

    /**
     * Gets the joystick position from the Driver Station, and calculates what to set the power of the motors
     * by using a tangent curve as a mapping function, and using the gyro to correct for some natural turning of the robot.
     * 
     * TODO: The auto gyro correction does not work very well, look into why it is not working and try to fix it or remove it.
     */
    protected void execute() {
    	
    	// update that datalogger state
    	DataCollator.state.setVal("ArcadeDriveCmdExecute");
    	
    	// get joystick positions
    	double joystickX = Robot.oi.getXAxisOfArcade();
    	double joystickY = Robot.oi.getYAxisOfArcade();
    	double joystickSlider = Robot.oi.getSliderAxisOfArcade();
    	
    	// use mapping function and the joystick slider as a gain 
    	// to get a desired turn amount and a desired forward motion speed
    	tanTurn = scaledValTan(joystickX * joystickSlider, TANDOMAIN_X);
    	tanPower = scaledValTan(joystickY * joystickSlider, TANDOMAIN_Y);
    	
    	// calculate actual ability of robot by reserving 5% of motor speed for turning at all times
      	processedPower = 0.95*tanPower;
      	
      	// Combine the desired turn rate with how much the motors are not using.
    	processedOnceTurn = (1-processedPower) * tanTurn; 
    	
    	// attempt to use the gyro as a feedback to drive in a straight line or at a constant curve. probably should remove this, or use encoders instead. 
    	processedTwiceTurn = (processedOnceTurn - gyroRateGain*Robot.dt.getGyroRate())*pTurnGain + processedOnceTurn; // uses the gyro as a feedback loop to drive at the desired turn rate. 

    	// Calculates the speed of the wheels to achieve the desired turning rate
    	// Checks which side of the robot is considered the "front", and inverts the Robot.dt.driveLR() parameters if needed
    	// TODO: move RobotMap.fowardOrReverse to DriveTrain.java
    	if (DriveTrain.fowardOrReverse == 1){
        	Robot.dt.driveLR(DriveTrain.fowardOrReverse*(processedPower + processedTwiceTurn), DriveTrain.fowardOrReverse*(processedPower - processedTwiceTurn));
    	} else {
    		Robot.dt.driveLR(DriveTrain.fowardOrReverse*(processedPower - processedTwiceTurn), DriveTrain.fowardOrReverse*(processedPower + processedTwiceTurn));
    	}
	}

    // This command should always run if another command is not running.
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    // stops the motors.
    protected void end() {
    	Robot.dt.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	this.end();
    }
}
