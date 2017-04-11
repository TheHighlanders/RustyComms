package org.mayheminc.robot2017;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	// Drive CAN Talons
	/**@SeanMoushegian: These Talons IDs are accurate for the 1st iteration
	 * H-Drive robot, 20170131*/
	public static final int FRONT_LEFT_TALON = 6;
	public static final int BACK_LEFT_TALON = 4;
	public static final int FRONT_RIGHT_TALON = 5;
	public static final int BACK_RIGHT_TALON = 3;
	public static final int MIDDLE_LEFT_TALON = 2;
	public static final int MIDDLE_RIGHT_TALON = 1;
		
	//Shooter Talon
	public static final int SHOOTER_TALON = 7;

	public static final int CLIMBER_TALON = 12;
	
	public static final int SHOOTER_LOADER_TALON = 9;
	public static final int THUMPER_TALON = 11;
	
//	public static final int GEAR_LEFT_TALON = 10;
//	public static final int GEAR_RIGHT_TALON = 14;
	public static final int GEAR_ROLLER_TALON = 10;
	public static final int GEAR_ARM_TALON = 14;

	// digital inputs / output
	public static final int LAUNCHER_RESET_LIMIT_SWITCH = 0;
	public static final int LAUNCHER_RESET_LIMIT_SWITCH2 = 0;
	
    // Analog inputs
    //public static final int HEADING_GYRO = 1;
    public static final int LEFT_PROXIMITY_SENSOR = 1;
    public static final int RIGHT_PROXIMITY_SENSOR = 2;
    
	// Joysticks
	public static final int DRIVER_GAMEPAD = 0;
	public static final int DRIVER_JOYSTICK = 1;
	public static final int OPERATOR_GAMEPAD = 2;
	public static final int OPERATOR_JOYSTICK = 3;
	
	//Solenoids:
	public static final int GEAR_ELBOW_SOLENOID = 0;
	public static final int GEAR_FINGER_SOLENOID = 4;
	
	public static final int DRIVE_FRONT_LEFT_PDP = 3;
	public static final int DRIVE_FRONT_RIGHT_PDP = 14;
	public static final int DRIVE_CENTER_LEFT_PDP = 1;
	public static final int DRIVE_CENTER_RIGHT_PDP = 13;
	public static final int DRIVE_BACK_LEFT_PDP = 0;
	public static final int DRIVE_BACK_RIGHT_PDP = 2;
	
	public static final int THUMPER_PDP = 4;
	public static final int SHOOTER_PDP = 12;
	public static final int SHOOTER_LOADER_PDP = 11;
	public static final int CLIMBER_PDP = 15;
	public static final int ROLLER_PDP = 5;
	public static final int ARM_PDP = 6;
	
}
