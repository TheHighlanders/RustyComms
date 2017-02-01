package org.usfirst.frc.team6201.robot;

import org.usfirst.frc.team6201.robot.commands.ClimbRope;
import org.usfirst.frc.team6201.robot.commands.DriveTimeCmd;
import org.usfirst.frc.team6201.robot.commands.FallRope;
import org.usfirst.frc.team6201.robot.commands.StopClimbing;
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
	private Joystick logitech = new Joystick(RobotMap.LOGITECH);
	
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
	 * TODO: what is the range? what is it at one of the states?
	 */
	public double getSliderAxisOfArcade() {
		
		return 0.5 * (1 + logitech.getRawAxis(RobotMap.LOGITECH_SLIDER_AXIS));
		
	}

	public OI() {
		
		Button button12 = new JoystickButton(logitech, 12);
		button12.whenPressed(new TurnAngleCmd(90, 5));
		
		Button b1 = new JoystickButton(logitech, 1);
		b1.whileHeld(new ClimbRope());
		
		Button b2 = new JoystickButton(logitech, 2);
		b2.whileHeld(new StopClimbing());
		
		Button b3 = new JoystickButton(logitech, 3);
		b3.whileHeld(new FallRope());
		
		Button b4 = new JoystickButton(logitech, 3);
		b4.whenPressed(new DriveTimeCmd(3));
		
		
	}
	
}


