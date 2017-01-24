package org.usfirst.frc.team6201.robot;

import edu.wpi.first.wpilibj.Joystick;

/**
 * 
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 * 
 * @author Baxter Ellard
 * 
 */
public class OI {

	//Arcade Drive
	private Joystick arcade = new Joystick(RobotMap.ARCADE);
	
	//Gets current rotation axis of Arcade Joystick
	public double getRotationAxisOfArcade() {
		
		return arcade.getRawAxis(RobotMap.LOGITECH_ROTATE_AXIS);
		
	}
	
	//Gets current X axis of Arcade Joystick
	public double getXAxisOfLogitech() {
		
		return arcade.getRawAxis(RobotMap.LOGITECH_X_AXIS);
		
	}

	//Gets current Y axis of Arcade Joystick
	public double getYAxisOfArcade() {
		
		return arcade.getRawAxis(RobotMap.LOGITECH_Y_AXIS);
		
	}
	
	//Gets current Z axis of Arcade Joystick
	public double getZAxisOfArcade() {
		
		return arcade.getRawAxis(RobotMap.LOGITECH_Z_AXIS);
		
	}
}
