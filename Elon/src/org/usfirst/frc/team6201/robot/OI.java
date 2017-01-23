package org.usfirst.frc.team6201.robot;

import edu.wpi.first.wpilibj.Joystick;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 * 
 * @author Baxter Ellard
 * 
 */
public class OI {

	private Joystick arcade = new Joystick(RobotMap.ARCADE);
	
	public double getRotationAxisOfArcade() {
		
		return arcade.getRawAxis(RobotMap.LOGITECH_ROTATE_AXIS);
		
	}
	
	public double getXAxisOfLogitech() {
		
		return arcade.getRawAxis(RobotMap.LOGITECH_X_AXIS);
		
	}

	public double getYAxisOfArcade() {
		
		return arcade.getRawAxis(RobotMap.LOGITECH_Y_AXIS);
		
	}

	public double getZAxisOfArcade() {
		
		return arcade.getRawAxis(RobotMap.LOGITECH_Z_AXIS);
		
	}
}
