package org.usfirst.frc.team6201.robot;


import org.usfirst.frc.team6201.robot.commands.ClimbRopeCmd;
import org.usfirst.frc.team6201.robot.commands.DescendRopeCmd;
import org.usfirst.frc.team6201.robot.commands.DriveTimeCmd;
import org.usfirst.frc.team6201.robot.commands.StopClimbingCmd;
import org.usfirst.frc.team6201.robot.commands.TurnAngleCmd;
import org.usfirst.frc.team6201.robot.commands.gears.AutoPosRobotGearDeliveryCmd;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This provides the framework to connect the DriverStation to the Robot both for getting values from the joystick(s), 
 * and for starting commands when buttons are pressed.
 * 
 * 
 * @author Baxter Ellard
 * @author David Matthews
 */
public class OI {

	/**
	 * Create an object out of our logitech arcade joystick.
	 * This allows us to get the  current position of the joystick, and the state of all the buttons.
	 * Initialized with the USB devices plugged into the robot
	 */
	private Joystick logitech = new Joystick(RobotMap.LOGITECH);
	private Joystick controlBoard = new Joystick(RobotMap.CONTROLBOARD);
	
	/**
	 * @return  a double corresponding to how much the joystick's handle is rotated.
	 * This has a range of -1 to 1. All the way to the right is +1.
	 */
	public double getRotationAxisOfArcade() {
		
		return logitech.getRawAxis(RobotMap.LOGITECH_ROTATE_AXIS);
	}
	
	/**
	 * @return a double corresponding to the position of the joystick in the side to side direction (X axis).
	 * Range of -1 to 1. All the way to the right is +1.
	 */
	public double getXAxisOfArcade() {
		
		return logitech.getRawAxis(RobotMap.LOGITECH_X_AXIS);
	}

	/**
	 * @return a double corresponding to the position of the joystick in the Y axis (front and back).
	 * range of -1 to 1, with all the way forward being 1
	 */
	public double getYAxisOfArcade() {
		
		return logitech.getRawAxis(RobotMap.LOGITECH_Y_AXIS);
		
	}
	
	
	/**
	 * @return a double corresponding to the slider on the joystick roughly under the wrist of someone if they are holding it.
	 * range of -1 or 1, where -1 is pointing the slider up and 1 is pointing it down
	 */
	public double getSliderAxisOfArcade() {
		
		return logitech.getRawAxis(RobotMap.LOGITECH_SLIDER_AXIS);
		
	}

	public OI() {
		
		Button b12 = new JoystickButton(logitech, 12);
		//SUPPOSED to turn the robot 90 degrees when the 12 button is pressed
		b12.whenPressed(new TurnAngleCmd(90, 5));
		
		Button b11 = new JoystickButton(logitech, 11);
		//SUPPOSED to turn the robot 90 degrees when the 12 button is pressed
		b11.whenPressed(new TurnAngleCmd(-90, 5));
		
		Button b10 = new JoystickButton(logitech, 10);
		//SUPPOSED to turn the robot 90 degrees when the 12 button is pressed
		b10.whenPressed(new TurnAngleCmd(180, 5));
		
		//starts the process of climbing the rope
		Button b1 = new JoystickButton(logitech, 1);
		b1.whileHeld(new ClimbRopeCmd());
		
		//stops the rope climber
		//might want to be whenPressed instead
		Button b2 = new JoystickButton(logitech, 2);
		b2.whileHeld(new StopClimbingCmd());
		
		//starts the process of unwinding the rope
		Button b3 = new JoystickButton(logitech, 3);
		b3.whileHeld(new DescendRopeCmd());
		
		//Drives for, in this case, 3 seconds
		Button b4 = new JoystickButton(logitech, 4);
		b4.whenPressed(new DriveTimeCmd(3));
		
		Button b5 = new JoystickButton(logitech, 5);
		b5.whenPressed(new AutoPosRobotGearDeliveryCmd());
		
		//Climbs rope when GREEN button on control board is held down
		Button controlClimb = new JoystickButton(controlBoard, 8);
		controlClimb.whileHeld(new ClimbRopeCmd());
		
	}
	
}


