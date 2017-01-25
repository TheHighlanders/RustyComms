package org.usfirst.frc.team6201.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 * 
 * @author David Matthews
 * 
 */
public class RobotMap {
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
	
	
	//ADD NUMBERS AS THEY ARE ON THE ROBOT
	public static final int LEFT_DRIVE1 = 0;
	public static final int LEFT_DRIVE2 = 0;
	public static final int RIGHT_DRIVE1 = 0;
	public static final int RIGHT_DRIVE2 = 0;
	
	public static final int ARCADE = 0;
	public static final int LOGITECH = 1;
	public static final int LOGITECH_X_AXIS = 0;
	public static final int LOGITECH_Y_AXIS = 1;
	public static final int LOGITECH_ROTATE_AXIS = 2;
	public static final int LOGITECH_SLIDER_AXIS = 3;
}
