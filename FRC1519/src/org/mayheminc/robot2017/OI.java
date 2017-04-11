package org.mayheminc.robot2017;

import org.mayheminc.util.*;
//import org.mayheminc.robot2017.autonomousroutines.*;
import org.mayheminc.robot2017.commands.*;
import org.mayheminc.robot2017.autonomousroutines.*;
import org.mayheminc.robot2017.commands.DriveStraight.DistanceUnits;
import org.mayheminc.robot2017.commands.StrafeForDistance.StrafeDistanceUnits;
import org.mayheminc.robot2017.subsystems.GearFloorPickerUpper;
import org.mayheminc.robot2017.subsystems.GearManipulator;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.buttons.*;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 * 
 * NOTE ON TERMINOLOGY: "Joystick" can mean a controller (which includes many buttons)
 * 		It can also mean a joystick axis.  Please be careful; this term can be ambiguous.
 */
public class OI {
	
	// variable to maintain state of whether or not we've "checked in" with the field
	private boolean m_CheckedInWithFieldManagement = false;

	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released  and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());

	//	Axis Definitions for the F310 gamepad
	//	Axis 0 - Left X Axis (-1.0 left to +1.0 right)
	//	Axis 1 - Left Y Axis (-1.0 forward to +1.0 backward)
	//	Axis 2 - Left Trigger (0.0 unpressed to +1.0 fully pressed)
	//	Axis 3 - Right Trigger (0.0 unpressed to +1.0 fully pressed)
	//	Axis 4 - Right X Axis (-1.0 left to +1.0 right)
	//	Axis 5 - Right Y axis (-1.0 forward to +1.0 backward)
	//  Empirical testing on 23 Jan 2015 shows that +/-0.07 is a reasonable threshold for "centered"
	//   (in other words, intentional non-zero values will have magnitude >= 0.07;
	//    values with a magnitude of < 0.07 should probably be treated as zero.)

/********************************** DRIVER PAD ***********************************************/
	private static final Joystick DRIVER_PAD = new Joystick(RobotMap.DRIVER_GAMEPAD);
	
	//Buttons:
	private static final Button DRIVER_PAD_START_BUTTON = new JoystickButton(DRIVER_PAD, 8);
	private static final Button DRIVER_PAD_BACK_BUTTON	= new JoystickButton(DRIVER_PAD, 7);
	private static final Button DRIVER_PAD_GREEN_BUTTON = new JoystickButton(DRIVER_PAD, 1);
	private static final Button DRIVER_PAD_RED_BUTTON = new JoystickButton(DRIVER_PAD, 2);
	private static final Button DRIVER_PAD_BLUE_BUTTON = new JoystickButton(DRIVER_PAD, 3);
	private static final Button DRIVER_PAD_YELLOW_BUTTON = new JoystickButton(DRIVER_PAD, 4);
	private static final Button DRIVER_PAD_TOP_LEFT_TRIGGER = new JoystickButton(DRIVER_PAD, 5);
	private static final Button DRIVER_PAD_TOP_RIGHT_TRIGGER = new JoystickButton(DRIVER_PAD, 6);
	private static final Button DRIVER_PAD_LEFT_STICK_BUTTON = new JoystickButton(DRIVER_PAD, 9);
	private static final Button DRIVER_PAD_RIGHT_STICK_BUTTON = new JoystickButton(DRIVER_PAD, 10);
	
	//Driver Pad Joysticks / Lower Triggers (variable input):
	private static final int GAMEPAD_F310_LEFT_X_AXIS = 0;
	private static final int GAMEPAD_F310_LEFT_Y_AXIS = 1;
	private static final int GAMEPAD_F310_LEFT_TRIGGER = 2;
	private static final int GAMEPAD_F310_RIGHT_TRIGGER = 3;
	private static final int GAMEPAD_F310_RIGHT_X_AXIS = 4;
	private static final int GAMEPAD_F310_RIGHT_Y_AXIS = 5;
	private static final JoystickAxisButton DRIVER_PAD_LEFT_LOWER_TRIGGER_BUTTON = new JoystickAxisButton(DRIVER_PAD, GAMEPAD_F310_LEFT_TRIGGER, JoystickAxisButton.POSITIVE_ONLY);
	private static final JoystickAxisButton DRIVER_PAD_RIGHT_LOWER_TRIGGER_BUTTON = new JoystickAxisButton(DRIVER_PAD, GAMEPAD_F310_RIGHT_TRIGGER, JoystickAxisButton.POSITIVE_ONLY);
						//left and right joystick not necessary here, their values will be returned by oi.driveThrottle() etc.
	
	//Driver Pad POV:
	private static final JoystickPOVButton DRIVER_PAD_D_PAD_UP = new JoystickPOVButton(DRIVER_PAD, 0);
	private static final JoystickPOVButton DRIVER_PAD_D_PAD_RIGHT = new JoystickPOVButton(DRIVER_PAD, 90);
	private static final JoystickPOVButton DRIVER_PAD_D_PAD_DOWN = new JoystickPOVButton(DRIVER_PAD, 180);
	private static final JoystickPOVButton DRIVER_PAD_D_PAD_LEFT = new JoystickPOVButton(DRIVER_PAD, 270);
	
	//AND, OR Buttons:

/********************************** DRIVER STICK ***********************************************/
	private static final Joystick DRIVER_STICK = new Joystick(RobotMap.DRIVER_JOYSTICK);
	
	// axis of the stick
	private static final int DRIVER_STICK_X_AXIS = 0;
	private static final int DRIVER_STICK_Y_AXIS = 1;
	//Buttons:
	private static final Button DRIVER_STICK_BUTTON_ONE = new DisabledOnlyJoystickButton(DRIVER_STICK, 1);
	private static final Button DRIVER_STICK_BUTTON_TWO = new EnabledOnlyJoystickButton(DRIVER_STICK, 2);
	private static final Button DRIVER_STICK_BUTTON_THREE = new EnabledOnlyJoystickButton(DRIVER_STICK, 3);	
	private static final Button DRIVER_STICK_BUTTON_FOUR = new EnabledOnlyJoystickButton(DRIVER_STICK, 4);
	private static final Button DRIVER_STICK_BUTTON_FIVE = new EnabledOnlyJoystickButton(DRIVER_STICK, 5);
	private static final Button DRIVER_STICK_BUTTON_SIX = new DisabledOnlyJoystickButton(DRIVER_STICK, 6);
	private static final Button DRIVER_STICK_BUTTON_SEVEN = new DisabledOnlyJoystickButton(DRIVER_STICK, 7);	
	private static final Button DRIVER_STICK_BUTTON_EIGHT= new DisabledOnlyJoystickButton(DRIVER_STICK, 8);
	private static final Button DRIVER_STICK_BUTTON_NINE = new DisabledOnlyJoystickButton(DRIVER_STICK, 9);
	private static final Button DRIVER_STICK_BUTTON_TEN = new DisabledOnlyJoystickButton(DRIVER_STICK, 10);
	private static final Button DRIVER_STICK_BUTTON_ELEVEN = new DisabledOnlyJoystickButton(DRIVER_STICK, 11);

/********************************** OPERATOR STICK ***********************************************/
	private static final Joystick OPERATOR_PAD = new Joystick(RobotMap.OPERATOR_GAMEPAD);
	
	//Buttons:
	private static final Button OPERATOR_PAD_BUTTON_ONE = new JoystickButton(OPERATOR_PAD, 1);
	private static final Button OPERATOR_PAD_BUTTON_TWO = new JoystickButton(OPERATOR_PAD, 2);
	private static final Button OPERATOR_PAD_BUTTON_THREE = new JoystickButton(OPERATOR_PAD, 3);
	private static final Button OPERATOR_PAD_BUTTON_FOUR = new JoystickButton(OPERATOR_PAD, 4);
	private static final Button OPERATOR_PAD_BUTTON_FIVE = new JoystickButton(OPERATOR_PAD, 5);
	private static final Button OPERATOR_PAD_BUTTON_SIX = new JoystickButton(OPERATOR_PAD, 6);
	private static final Button OPERATOR_PAD_BUTTON_SEVEN = new JoystickButton(OPERATOR_PAD, 7);
	private static final Button OPERATOR_PAD_BUTTON_EIGHT = new JoystickButton(OPERATOR_PAD, 8);
	private static final Button OPERATOR_PAD_BUTTON_NINE = new JoystickButton(OPERATOR_PAD, 9);
	private static final Button OPERATOR_PAD_BUTTON_TEN = new JoystickButton(OPERATOR_PAD, 10);
	private static final Button OPERATOR_PAD_BUTTON_ELEVEN = new JoystickButton(OPERATOR_PAD, 11);
	private static final Button OPERATOR_PAD_BUTTON_TWELVE = new JoystickButton(OPERATOR_PAD, 12);

	//Operator Axes
	private static final int OPERATOR_PAD_LEFT_X_AXIS = 0;
	private static final int OPERATOR_PAD_LEFT_Y_AXIS = 1;
	private static final int OPERATOR_PAD_RIGHT_X_AXIS = 2;
	private static final int OPERATOR_PAD_RIGHT_Y_AXIS = 3;	
	private static final JoystickAxisButton OPERATOR_PAD_LEFT_Y_AXIS_UP = new JoystickAxisButton(OPERATOR_PAD, OPERATOR_PAD_LEFT_Y_AXIS, JoystickAxisButton.NEGATIVE_ONLY);
	private static final JoystickAxisButton OPERATOR_PAD_LEFT_Y_AXIS_DOWN = new JoystickAxisButton(OPERATOR_PAD, OPERATOR_PAD_LEFT_Y_AXIS, JoystickAxisButton.POSITIVE_ONLY);
	private static final JoystickAxisButton OPERATOR_PAD_RIGHT_Y_AXIS_UP = new JoystickAxisButton(OPERATOR_PAD, OPERATOR_PAD_RIGHT_Y_AXIS, JoystickAxisButton.NEGATIVE_ONLY);
	private static final JoystickAxisButton OPERATOR_PAD_RIGHT_Y_AXIS_DOWN = new JoystickAxisButton(OPERATOR_PAD, OPERATOR_PAD_RIGHT_Y_AXIS, JoystickAxisButton.POSITIVE_ONLY);

	//Operator POV
	private static final JoystickPOVButton OPERATOR_PAD_D_PAD_LEFT = new JoystickPOVButton(OPERATOR_PAD, 270);
	private static final JoystickPOVButton OPERATOR_PAD_D_PAD_RIGHT = new JoystickPOVButton(OPERATOR_PAD, 90);
	private static final JoystickPOVButton OPERATOR_PAD_D_PAD_UP = new JoystickPOVButton(OPERATOR_PAD, 0);
	private static final JoystickPOVButton OPERATOR_PAD_D_PAD_DOWN = new JoystickPOVButton(OPERATOR_PAD, 180);	
	
	//AND, OR Buttons
	private static final Button FORCE_FIRE_BUTTON = new AndJoystickButton(OPERATOR_PAD, 5, OPERATOR_PAD, 7);	
	

	public OI() {
		DriverStation.reportError("OI constructor.\n", false);

	/********************************** DRIVER PAD ***********************************************/
		//Buttons:
		
		DRIVER_PAD_BACK_BUTTON.whenPressed(new ToggleHeadingCorrectionMode());
		
		//Strafe For Width of Spoke Buttons
		DRIVER_PAD_D_PAD_LEFT.whileHeld(new RunCommandOnceOnly(new StrafeForDistance(-0.2, StrafeDistanceUnits.INCHES, 3)));
		DRIVER_PAD_D_PAD_RIGHT.whileHeld(new RunCommandOnceOnly(new StrafeForDistance(0.2, StrafeDistanceUnits.INCHES, 3)));
		
		DRIVER_PAD_BLUE_BUTTON.whileHeld(new DriveRoundTheAirshipRtoL());
		DRIVER_PAD_GREEN_BUTTON.whileHeld(new DriveStraightOnHeading(-1.0, DriveStraightOnHeading.DistanceUnits.INCHES, 42.0*12, 0.0));
		DRIVER_PAD_RED_BUTTON.whileHeld(new DriveRoundTheAirshipLtoR());

		//Joystick Axis Buttons:
		
		DRIVER_PAD_RIGHT_LOWER_TRIGGER_BUTTON.whenPressed(new ResetHeadingPidAlignment());
		
		//POV:
		
		//AND / OR Buttons:
		
	/********************************** DRIVER STICK ***********************************************/
		DRIVER_STICK_BUTTON_ONE.whenPressed(new CheckInWithFMS());
		
		DRIVER_STICK_BUTTON_TWO.whileHeld(new GoToHeading(20.0));
		DRIVER_STICK_BUTTON_THREE.whileHeld(new GoToHeading(0.0));
		DRIVER_STICK_BUTTON_FOUR.whileHeld(new GoToHeading(-10.0));
		DRIVER_STICK_BUTTON_FIVE.whileHeld(new GoToHeading(15.0));
		
		DRIVER_STICK_BUTTON_SIX.whenPressed(new SelectAutonomousProgram(1));
		DRIVER_STICK_BUTTON_SEVEN.whenPressed(new SelectAutonomousProgram(-1));
		DRIVER_STICK_BUTTON_TEN.whenPressed(new SelectAutonomousDelay(-1));
		DRIVER_STICK_BUTTON_ELEVEN.whenPressed(new SelectAutonomousDelay(1));
		
		DRIVER_STICK_BUTTON_EIGHT.whenPressed(new ZeroGyro());
		DRIVER_STICK_BUTTON_NINE.whenPressed(new ZeroArmEncoder());
		
	/********************************** OPERATOR PAD ***********************************************/
		//Buttons:
		OPERATOR_PAD_BUTTON_ONE.whenPressed(new AdjustShooterSpeed(-1));
		OPERATOR_PAD_BUTTON_THREE.whenPressed(new AdjustShooterSpeed(+1));
		OPERATOR_PAD_BUTTON_FOUR.whenPressed(new EnableShooter());
		OPERATOR_PAD_BUTTON_TWO.whenPressed(new DisableShooter());

		// OPERATOR_PAD_BUTTON_FIVE -- used for "Climb Fast" via getClimberFastButton() below
		// OPERATOR_PAD_BUTTON_SEVEN -- used for "Climb Slow" via getClimberSlowButton() below
		
	    OPERATOR_PAD_BUTTON_SIX.whileHeld(new EnableShooterLoader());
//	    OPERATOR_PAD_BUTTON_NINE.whenPressed(new ArmAutomaticMode());
//	    OPERATOR_PAD_BUTTON_TEN.whenPressed(new ArmManualMode());
	    
	    //Removed per request of Gabe
//	    OPERATOR_PAD_BUTTON_EIGHT.whenPressed(new PushGearOut());
//	    OPERATOR_PAD_BUTTON_EIGHT.whenReleased(new CloseGearDoor());
	    
//	    OPERATOR_PAD_BUTTON_NINE.whenPressed(new ReleaseHopperDeployers());
//	    OPERATOR_PAD_BUTTON_TEN -- unused
	    
	    // old controls for GearManipulator with window-motor doors
//	    OPERATOR_PAD_D_PAD_UP.whenPressed(new PushGearOut());
//	    OPERATOR_PAD_D_PAD_DOWN.whenPressed(new CloseGearDoor());
	    
	    // new controls for GearFloorPickerUpper
//	    OPERATOR_PAD_D_PAD_UP.whileHeld(new OperateGearRollers(GearFloorPickerUpper.RollerAction.OUT));
//	    OPERATOR_PAD_D_PAD_DOWN.whileHeld(new OperateGearRollers(GearFloorPickerUpper.RollerAction.IN));
	    OPERATOR_PAD_D_PAD_UP.whenPressed(new ArmCarryState());
	    OPERATOR_PAD_D_PAD_DOWN.whenPressed(new ArmFloorState());
	    OPERATOR_PAD_D_PAD_LEFT.whileHeld(new PermissionToPlace());
	    OPERATOR_PAD_D_PAD_RIGHT.whenPressed(new ArmReadyToPlaceState());
	    
	    //Joystick Axis Buttons:
	    //OPERATOR_PAD_RIGHT_Y_AXIS_UP.whenPressed(new PrintToSmartDashboard("Increase Shooter Speed"));

	    //POV:
//	    OPERATOR_PAD_D_PAD_RIGHT.whenPressed(new Print(""));

	    //AND / OR Buttons:
		
		DriverStation.reportError("OI constructor done.\n", false);
	}
	
/********************************** RETURN INPUT VALUE ***********************************************/
	public boolean getQuickTurn() {
		return(DRIVER_PAD.getRawButton(6));
	}

	public boolean getAutoTargetBoiler() {
		return DRIVER_PAD_RIGHT_LOWER_TRIGGER_BUTTON.get();
	}
	
	public boolean getAutoTargetLift() {
		return DRIVER_PAD_LEFT_LOWER_TRIGGER_BUTTON.get();
		
	}
	public double getDriveThrottle() {
		// the driveThrottle is the "Y" axis of the Driver Gamepad.
		// However, the joysticks give -1.0 on the Y axis when pushed forward
		// This method reverses that, so that positive numbers are forward
		double throttleVal = -DRIVER_PAD.getY();

		if (Math.abs(throttleVal) < 0.05) {
			throttleVal = 0.0;
		}

		return(throttleVal);
	}
	
	public double getStrafeThrottle() {
		double throttleVal = DRIVER_PAD.getX();
		if (Math.abs(throttleVal) < 0.05) {
			throttleVal = 0.0;
		}
		return(throttleVal);

	}
	
	public double getSteeringX() {
		// SteeringX is the "X" axis of the right stick on the Driver Gamepad.
		double value = DRIVER_PAD.getRawAxis(OI.GAMEPAD_F310_RIGHT_X_AXIS);
		if(Math.abs(value) < 0.05) {
			value = 0.0;
		}
		
		return value;
	}
	
	public double getSteeringY() {
		// However, the joysticks give -1.0 on that axis when pushed forward
		// This method reverses that, so that positive numbers are forward
		return(-DRIVER_PAD.getRawAxis(OI.GAMEPAD_F310_RIGHT_Y_AXIS)); 
	}
	
	public boolean getSlowModifier() {
		return DRIVER_PAD.getRawButton(5);
	}
	
	public double getDriverStickY(){
		double value = -DRIVER_STICK.getRawAxis(DRIVER_STICK_Y_AXIS); // negative to reverse the forward is up on the joystick
		if (Math.abs(value) < 0.05) {
			value = 0.0;
		}
		return value;
	}
	
//	public double getClimberThrottle(){
//		double value = -OPERATOR_PAD.getRawAxis(OPERATOR_PAD_LEFT_Y_AXIS); // negative to reverse the forward is up on the joystick
//		if(Math.abs(value) < 0.05){
//			value = 0.0;
//		}
//		return value;
//	}
	
	public boolean getRollerSpitButton(){
		return OPERATOR_PAD_LEFT_Y_AXIS_UP.get();
	}
	public boolean getRollerSuckButton(){
		return OPERATOR_PAD_LEFT_Y_AXIS_DOWN.get();
	}
	
	// Note that moving the arm throttle forward (up) gives a negative value!
	public double getArmThrottle() {
		double value;
		
		// Note:  up (forward) on the joystick results in negative numbers!
		value = OPERATOR_PAD.getRawAxis(OPERATOR_PAD_RIGHT_Y_AXIS); 
		
		//	use a dead zone of 5%
		if (Math.abs(value) < 0.05) {
			value = 0.0;
		}
		
		return value;
	}
	
	public boolean getClimberFastButton() {
		return(OPERATOR_PAD.getRawButton(5));
	}
	
	public boolean getClimberSlowButton() {
		return(OPERATOR_PAD.getRawButton(7));
	}
	
	public boolean getGoToHeadingButtons() {
		return (DRIVER_STICK.getRawButton(2) || DRIVER_STICK.getRawButton(3)
				|| DRIVER_STICK.getRawButton(4) || DRIVER_STICK.getRawButton(5));
	}
	
	public boolean getStrafeForWidthOfGearSpokeButtons() {
		return DRIVER_PAD_D_PAD_LEFT.get() || DRIVER_PAD_D_PAD_RIGHT.get();
	}
	public boolean getPermissionToPlaceButton(){
		return OPERATOR_PAD_D_PAD_LEFT.get();
	}
	public boolean getPlaceGearButton(){
		return DRIVER_PAD.getRawButton(4); //driver pad yellow button
	}
}

/********************************** BONEYARD OF OLD CODE ***********************************************/
//    public double tankDriveLeft() {
//        double tankDriveLeftAxis = -DRIVER_PAD.getRawAxis(OI.GAMEPAD_F310_LEFT_Y_AXIS);
//    	
//        if (Math.abs(tankDriveLeftAxis) < 0.05) {
//    		tankDriveLeftAxis = 0.0;
//    	}
//    	return tankDriveLeftAxis;
//    }
//    
//    public double tankDriveRight() {
//    	double tankDriveRightAxis = -DRIVER_PAD.getRawAxis(OI.GAMEPAD_F310_RIGHT_Y_AXIS);
//     	
//    	if (Math.abs(tankDriveRightAxis) < 0.05) {
//     		tankDriveRightAxis = 0.0;
//     	}
//     	return tankDriveRightAxis;
//    }
//    
//    public void rumbleOperatorGamePad() {
//    	DRIVER_PAD.setRumble(Joystick.RumbleType.kLeftRumble, 0);
//    	DRIVER_PAD.setRumble(Joystick.RumbleType.kRightRumble, 0);
//    }
//        
//    /**
//     * This method will return a positive value when the left joystick is pushed up, 
//     * and it will return a negative value when the left joystick is pushed down.
//     * @return
//     */
//    
//    public double getArmManualControl(){
//    	double value = (OPERATOR_PAD.getRawAxis(OPERATOR_PAD_RIGHT_Y_AXIS)) * -1;
//    	if(Math.abs(value) <= 0.05){
//    		value = 0.0;
//    	}
//    	return value;
//    }
//
//    public double getWinchControl(){
//    	//return OPERATOR_PAD.getRawAxis(OPERATOR_PAD_LEFT_Y_AXIS) * -1;
//    	double value = (OPERATOR_PAD.getRawAxis(OPERATOR_PAD_LEFT_Y_AXIS)) * -1;
//    	if(Math.abs(value) <= 0.05){
//    		value = 0.0;
//    	}
//    	return value;
//    }

//	public boolean permissionToLaunch() {
//		//return(OPERATOR_PAD.getRawButton(OPERATOR_PAD_BUTTON_FIVE));
//		return OPERATOR_PAD_BUTTON_FIVE.get();
//	}
//  
//	public boolean forceLaunch() {
//		return FORCE_FIRE_BUTTON.get();
//	}    
//	
//	public boolean autoTarget() {
//		return DRIVER_PAD_RIGHT_LOWER_TRIGGER_BUTTON.get();
//	}
//
//	// returns true if any of the autoInTeleop buttons are held
//	public boolean autoInTeleop() {
//		return DRIVER_PAD_RED_BUTTON.get();
//	}