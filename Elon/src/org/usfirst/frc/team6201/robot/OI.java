package org.usfirst.frc.team6201.robot;


import org.usfirst.frc.team6201.robot.commands.ClimbRopeCmd;
import org.usfirst.frc.team6201.robot.commands.DescendRopeCmd;
import org.usfirst.frc.team6201.robot.commands.DriveTimeCmd;
import org.usfirst.frc.team6201.robot.commands.SolenoidStuff;
import org.usfirst.frc.team6201.robot.commands.StopClimbingCmd;
import org.usfirst.frc.team6201.robot.commands.TurnAngleCmd;
import org.usfirst.frc.team6201.robot.commands.gears.CenterStationAutoPos;

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
	
	private Button b1 = new JoystickButton(logitech, 1);
	
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
	
	/**
	 * @return True if button 1 is pressed, false otherwise.
	 */
	public boolean getButton1() {
		
		return b1.get();
		
	}

	public OI() {
		//starts the process of climbing the rope
		Button b7 = new JoystickButton(logitech, 7);
		b7.whileHeld(new ClimbRopeCmd());
		
		//stops the rope climber
		//might want to be whenPressed instead
		Button b2 = new JoystickButton(logitech, 2);
		b2.whileHeld(new StopClimbingCmd());
		
		Button b1 = new JoystickButton(logitech, 1);
		b1.whileHeld(new StopClimbingCmd());
		
		//starts the process of unwinding the rope
		Button b8 = new JoystickButton(logitech, 8);
		b8.whileHeld(new DescendRopeCmd());
		
		//using bc# intean of b# when using the control board as an input.
		//controls solenoid 0
		Button bc8 = new JoystickButton(controlBoard, 8);
		bc8.whenPressed(new SolenoidStuff(1, Robot.pn.solenoid0));
		//controls solenoid 1
		Button bc4 = new JoystickButton(controlBoard, 4);
		bc4.whenPressed(new SolenoidStuff(1, Robot.pn.solenoid1));
		//controls solenoid 2
		Button bc5 = new JoystickButton(controlBoard, 5);
		bc5.whenPressed(new SolenoidStuff(1, Robot.pn.solenoid2));
		//Climbs rope when GREEN button on control board is held down
		//Button controlClimb = new JoystickButton(controlBoard, 8);
		//controlClimb.whileHeld(new ClimbRopeCmd());
		
	}
	
}


