package org.usfirst.frc.team6201.robot;

import org.usfirst.frc.team6201.robot.commands.TurnAngleCmd;

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
	 */
	private Joystick arcade = new Joystick(RobotMap.ARCADE);
	
	/**
	 * @return  a double corresponding to how much the joystick's handle is rotated.
	 * This has a range of -1 to 1. All the way to the right is +1.
	 */
	public double getRotationAxisOfArcade() {
		
		return arcade.getRawAxis(RobotMap.LOGITECH_ROTATE_AXIS);
	}
	
	/**
	 * @return a double corresponding to the position of the joystick in the side to side direction (X axis).
	 * Range of -1 to 1. All the way to the right is +1.
	 */
	public double getXAxisOfArcade() {
		
		return arcade.getRawAxis(RobotMap.LOGITECH_X_AXIS);
	}

	/**
	 * @return a double corresponding to the position of the joystick in the Y axis (front and back).
	 * range of -1 to 1, with all the way forward being 1
	 */
	public double getYAxisOfArcade() {
		
		return arcade.getRawAxis(RobotMap.LOGITECH_Y_AXIS);
		
	}
	
	/**
	 * @return a double corresponding to the slider on the joystick roughly under the wrist of someone if they are holding it.
	 * TODO: what is the range? what is it at one of the states?
	 */
	public double getSliderAxisOfArcade() {
		
		return arcade.getRawAxis(RobotMap.LOGITECH_SLIDER_AXIS);
		
	}

	public OI() {
		
		Button button12 = new JoystickButton(arcade, 12);
		
		button12.whenPressed(new TurnAngleCmd(90, 5));
		
	}
	
}


