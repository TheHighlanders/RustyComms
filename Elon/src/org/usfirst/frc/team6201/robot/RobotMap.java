package org.usfirst.frc.team6201.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 * 
 * @author David Matthews
 * @author Baxter Ellard
 * 
 */
public class RobotMap {
	
	// Joystick
	public static final int LOGITECH = 1;
	public static final int LOGITECH_X_AXIS = 0;
	public static final int LOGITECH_Y_AXIS = 1;
	public static final int LOGITECH_ROTATE_AXIS = 2;
	public static final int LOGITECH_SLIDER_AXIS = 3;

	// Drive Train
	public static final int LEFT_DRIVE1 = 1;
	public static final int LEFT_DRIVE2 = 2;
	public static final int RIGHT_DRIVE1 = 3;
	public static final int RIGHT_DRIVE2 = 4;
	
	// Winch Climber
	//TODO: Update the PWM ports that the Victors are plugged into.
	
	public static final int CLIMBER1 = 0;
	public static final int CLIMBER2 = 1;
	
	// Data Logging
	public static final int LOGGING_UDP_PORT = 4445;
		
	// Target identification
	public static final int VISION_UDP_PORT = 5005;
		
		
}
