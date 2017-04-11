package org.mayheminc.robot2017.subsystems;

import org.mayheminc.robot2017.Robot;
import org.mayheminc.robot2017.RobotMap;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GearFloorPickerUpper extends Subsystem {

	public enum RollerAction { STOP, IN, RETAIN, OUT };
	private final double ROLLER_STOP_POWER = 0.0;
	private final double ROLLER_IN_POWER = -0.30;
	private final double ROLLER_RETAIN_POWER = -0.10;
	private final double ROLLER_OUT_POWER = 0.40;

	public enum ArmAction { STOP, UP, DOWN };
	private final double ARM_STOP_POWER = 0.0;
	private final double ARM_UP_POWER = -0.30;
	private final double ARM_DOWN_POWER = 0.25;
	private final double ARM_PRESS_AGAINST_FLOOR_POWER = 0.1;
	private final double ARM_PRESS_AGAINST_TOP_POWER = -0.1;
	private final double ARM_PLACE_GEAR_ON_PEG_POWER = 0.30;
	private final double ZERO_ARM_ENCODER_POWER = -0.15;  // was -0.20 at start of Pine Tree event, reduced to lessen bending

	public enum ArmPosition { CARRY, PLACE, FLOOR };
	private final double FLOOR_ENCODER_POSITION = 1375.0;  // was 0.0)
	private final double PLACE_ENCODER_POSITION = 270.0;  // (was 230.0 at Pine Tree)
	private final double CARRY_ENCODER_POSITION = 0.0;   // this is the "zero position" (was 1375.0)
	private final double LOW_ENCODER_POSITION = 1057.0;   // (was 318.0) When "placing" the gear, we cannot bring the arm to the floor (problems occur when we back up).  So instead, we should lower the arm to the encoder position defined by this final double.

	private final double FLOOR_TOLERANCE = 100.0;
	private final double CARRY_TOLERANCE = 50.0;
	
	// Normal state transitions expected:
	// At start of match:  STOWED_WITH_GEAR
	// In auto, when placing a gear:  ->  READY_TO_PLACE

	private enum GFPUStates { 
		MANUAL,           // arm in manual control mode (invoke by moving the throttle)
		CARRY_WITH_GEAR,  // arm in the stowed position, rollers on slowly to retain gear
		//CARRY_NO_GEAR,    // arm in the stowed position, rollers off since no gear
		READY_TO_PLACE,    // arm in the "ready-to-place gear" position (with a gear)
		PERMISSION_TO_PLACE,           // arm moving towards the "placing gear" position, while ejecting gear
		FLOOR,         // arm touching the floor, rollers on to acquire a gear
		ZEROING,       //applying pressure to the top stop to locate the zero point
	};
	private GFPUStates m_state = GFPUStates.MANUAL;

	// arm encoder: as of 24 March 2017, have an encoder, but:
	//   * no cabling to connect it to the TalonSRX
	//   * no experience with this model encoder to know about reliability

	// arm position thoughts:
	// * stow position would be all the way up against the stop (is there a stop?)
	//   -  stow position would also be "encoder zero"
	// * place position would be with the back of the gear about two inches beyond the "gear retainer"
	// * floor position would be lightly pressed (continuously) against the floor - maybe 20%?

	CANTalon m_rollers = new CANTalon(RobotMap.GEAR_ROLLER_TALON);
	CANTalon m_arm = new CANTalon(RobotMap.GEAR_ARM_TALON);

	private boolean m_armPositionControlMode = false;
	private double m_armVoltageRampRate = 48.0;
	
	private final double HIGH_ROLLER_CURRENT_THRESHOLD = 2.5;
	private int m_loopsHighRollerCurrent = 0;
	private final int HIGH_ROLLER_CURRENT_LOOPS = 5; //@SSM was 10 on 20170404; changed at request of Faith
	private boolean m_gotGear = false;

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public GearFloorPickerUpper() {
		// appropriately configure the talon for the rollers
		m_rollers.enableBrakeMode(true);
		m_rollers.changeControlMode(TalonControlMode.PercentVbus);

		// configure the arm talon appropriately
		m_arm.enableBrakeMode(true);
		m_arm.setFeedbackDevice(FeedbackDevice.QuadEncoder);

		// see Talon SRX Manual for walkthrough
		//THESE VALUES ARE NOT GOOD PLEASE DO NOT USE
		double armP = 3.0;
		double armI = 0.00;
		double armD = 550.0;  // typically 10x to 100x the "P" gain for Talon SRX position control
		double armF = 0.0; 

		// Note: comment out below line so that encoder units are forced to be
		// in native counts (x4)
		// m_arm.configEncoderCodesPerRev(1024);

		m_arm.reverseSensor(false);
		m_arm.configNominalOutputVoltage(+0.0f, -0.0f);
		m_arm.configPeakOutputVoltage(+3.0, -6.0);
//		m_arm.setPosition(0);
		m_arm.setPID(armP, armI, armD, armF, 0, m_armVoltageRampRate, 0);

		if (m_armPositionControlMode) {
			setPositionControlMode();
		} else {
			setVbusControlMode();
		}

	}
	
	public void setVbusControlMode() {
		m_arm.changeControlMode(TalonControlMode.PercentVbus);
		m_arm.set(0.0);
		m_arm.enableControl();
		m_armPositionControlMode = false;

	}
	
	public void setPositionControlMode() {
		m_arm.changeControlMode(TalonControlMode.Position);
		m_arm.set(m_arm.getPosition());
		m_arm.enableControl();
		m_armPositionControlMode = true;
	}
	
	
	//this method is public so that the drive controls can call it
	public void placeGear() {		
		setRollers(RollerAction.OUT);
		setPermissionToPlace();
		m_gotGear = false;
		SmartDashboard.putBoolean("Got Gear", m_gotGear);
		
		if (getArmEncoder() >= LOW_ENCODER_POSITION) {
			setArmPower(ARM_STOP_POWER);
		} else {
			setArmPower(ARM_PLACE_GEAR_ON_PEG_POWER);
		}	
	}

	private void carryPositionWithGear(){
		// arm in the stowed position, rollers on slowly to retain gear
		setRollers(RollerAction.RETAIN);
		
		if (getArmEncoder() <= (CARRY_ENCODER_POSITION + CARRY_TOLERANCE)){
			//apply small voltage into the ground
			setArmPower(ARM_PRESS_AGAINST_TOP_POWER);			
		} else {
			//use position control to get to the floor position
			setArmPosition(ArmPosition.CARRY);
		}
	}
//	public void carryPositionNoGear(){
//		// arm in the stowed position, rollers off since no gear		
//	}
	
	private void readyToPlacePosition(){
		// arm in the "ready-to-place gear" position (with a gear)
		setArmPosition(ArmPosition.PLACE);
		setRollers(RollerAction.RETAIN);
		//DriverStation.reportError("Setting Ready to place", false);
	}
	
	private void floorPosition(){
		// arm touching the floor, rollers on to acquire a gear		
		setRollers(RollerAction.IN);
		
		// KBS NOTE:  When in this position, with rollers rolling in, should
		// check for too-high rollers current for 5 consecutive loops, as this indicates we picked up a gear,
		// and change to the "stow" stater
		
		if (highRollerCurrentDraw()) {
			m_loopsHighRollerCurrent++;
		} else {
			m_loopsHighRollerCurrent = 0;
			m_gotGear = false;
			SmartDashboard.putBoolean("Got Gear", m_gotGear);
		}
		
		if (m_loopsHighRollerCurrent > 10) {
			m_loopsHighRollerCurrent = 0;
			m_gotGear = true;
			SmartDashboard.putBoolean("Got Gear", m_gotGear);
			m_state = GFPUStates.CARRY_WITH_GEAR;
		}
		
		if (getArmEncoder() >= (FLOOR_ENCODER_POSITION - FLOOR_TOLERANCE)){
			//apply small voltage into the ground
			setArmPower(ARM_PRESS_AGAINST_FLOOR_POWER);			
		} else {
			//use position control to get to the floor position
			setArmPosition(ArmPosition.FLOOR);
		}
	}
	
	public void setRollerPower(double power){
		m_rollers.set(power);
	}

	public void setRollers (RollerAction rollerAction) {
		switch (rollerAction) {
		case STOP:
			m_rollers.set(ROLLER_STOP_POWER);
			break;
		case IN:
			m_rollers.set(ROLLER_IN_POWER);
			break;
		case RETAIN:
			m_rollers.set(ROLLER_RETAIN_POWER);
			break;
		case OUT:
			m_rollers.set(ROLLER_OUT_POWER);
			break;
		default:
			DriverStation.reportError("setRollers encountered unexpected default.", false);
			break;
		}
	}
	
	public void setArmPosition(ArmPosition pos) {
		if (!m_armPositionControlMode) {
			setPositionControlMode();
		}		
		switch (pos) {
		case FLOOR:
			m_arm.set(FLOOR_ENCODER_POSITION);
			break;
		case CARRY:
			m_arm.set(CARRY_ENCODER_POSITION);
			break;
		case PLACE: 
			m_arm.set(PLACE_ENCODER_POSITION);
			break;
		default:
			DriverStation.reportError("setArmPosition encountered unexpected default.", false);
			break;
		}
	}

	public int getArmEncoder() {
		return (int) m_arm.getPosition();
	}
	
	public void zeroArmEncoder() {
		DriverStation.reportError("Resetting Arm Encoder", false);
		m_arm.setPosition(CARRY_ENCODER_POSITION);
	}
	public void zeroArmEncoderState(){
		setArmPower(ZERO_ARM_ENCODER_POWER);
	}
	
	// used for the operator commands to explicitly change between manual / automatic modes
	public void setArmManualMode(boolean positionControlMode) {
		DriverStation.reportError("Changing arm manual mode to " + m_armPositionControlMode, false);
		m_armPositionControlMode = !positionControlMode;
		if (m_armPositionControlMode) {
			setPositionControlMode();
		} else {
			setVbusControlMode();
		}
	}

//	private boolean m_permissionToPlace = false;
	
	public boolean getPermissionToPlace() {
		return Robot.oi.getPermissionToPlaceButton();
	}
	
	public void periodic() {
		double armPower = Robot.oi.getArmThrottle();
		double rollerPower = 0.0;
		if(Robot.oi.getRollerSpitButton()){
			rollerPower = ROLLER_OUT_POWER;
		}else if ( Robot.oi.getRollerSuckButton() ){
			rollerPower = ROLLER_IN_POWER;
		}
		
		// check to see if operator is moving the throttle.
		if ( ( !( Math.abs( rollerPower ) == 0.0) || (Math.abs(armPower) >= 0.05)) && !getPermissionToPlace()) { //we're commanding manual movement and are not giving permission to place the gear
//			m_permissionToPlace = false;
			// If operator using manual throttle,
			// give priority to the operator's manual control instead of the position buttons.
			
			// set arm mode and state to "Manual" mode
			setArmManualMode(true);
			m_state = GFPUStates.MANUAL;
			
			// scale armPower to one-third of range
			armPower = armPower / 3.0;
			setArmPower(armPower);
			
			
			
//			DriverStation.reportError("You're in manual mode!", false);
			
//			if (Robot.oi.getPermissionToPlaceButton()) {
//				setRollers(RollerAction.OUT);
//			} else {
				setRollerPower(rollerPower);
//			}
			

		} else {
			// there is no current commanded joystick throttle
			switch (m_state) {
			case MANUAL:
				// if we got here, it is because we are in manual mode, but no throttle commanded
				setArmPower(0.0);
				setRollerPower(0.0);
				break;
			case FLOOR:
				floorPosition();
//				m_permissionToPlace = false;
				break;
			case CARRY_WITH_GEAR: 
				carryPositionWithGear();
//				m_permissionToPlace = false;
				break;
			case READY_TO_PLACE:
				readyToPlacePosition();
//				m_permissionToPlace = false;
				break;
			case ZEROING:
				zeroArmEncoderState();
				break;
			case PERMISSION_TO_PLACE:
				//do nothing as the driver pad commands will now take precedence
//				m_permissionToPlace = true;
				// KBS NOTE:  Bug here - when operator lets go of button, still in this mode
				break;
			default:
				DriverStation.reportError("GFPU periodic encountered unexpected default.", false);
//				m_permissionToPlace = false;
				break;
			}			
		}

	}
	
	public void setFloorState(){
		m_state = GFPUStates.FLOOR;
	}
	
	public void setCarryState(){
		m_state = GFPUStates.CARRY_WITH_GEAR;		
	}

	public void setManualState(){
		m_state = GFPUStates.MANUAL;		
	}
	
	public void setReadyToPlaceState(){
		m_state = GFPUStates.READY_TO_PLACE;		
	}
	
	public void setPermissionToPlace(){
		m_state = GFPUStates.PERMISSION_TO_PLACE;
	}
	public void setZeroingState(){
		m_state = GFPUStates.ZEROING;
	}

	public void setArmPower(double armPower) {
		if (m_armPositionControlMode){
			setVbusControlMode();
		}

		// Unfortunately, the arm motor is wired up such that positive arm powers move down
		// and negative arm powers move up.  
		
		// This fact makes the following statements very odd, but they are right...

		// clip maximum "up" power
		if (armPower < ARM_UP_POWER) {
			armPower = ARM_UP_POWER;
		}

		// clip maximum "down" power
		if (armPower > ARM_DOWN_POWER) {
			armPower = ARM_DOWN_POWER;
		}
		//DriverStation.reportError("Setting " + armPower + " to arm", false);
		m_arm.set(armPower);
	}


	private boolean highRollerCurrentDraw() {
		return (getRollerCurrentDraw() > HIGH_ROLLER_CURRENT_THRESHOLD);
	}
	
	public double getRollerCurrentDraw(){
		return Robot.pdp.getCurrent(RobotMap.ROLLER_PDP);
	}

	public double getArmCurrentDraw(){
		return Robot.pdp.getCurrent(RobotMap.ARM_PDP);
	}

	public void updateSmartDashboard(){
		SmartDashboard.putNumber("Roller Current Draw", getRollerCurrentDraw());
		SmartDashboard.putNumber("Arm Current Draw", getArmCurrentDraw());

		SmartDashboard.putNumber("Arm Position", m_arm.getPosition());
		SmartDashboard.putNumber("Arm Set Point", m_arm.getSetpoint());
		SmartDashboard.putNumber("Arm Speed", m_arm.getSpeed());

		//SmartDashboard.putBoolean("Arm Auto Mode", m_armPositionControlMode);
		SmartDashboard.putString("Arm State: ", m_state.name());
		SmartDashboard.putBoolean("Got Gear", m_gotGear);
//		SmartDashboard.putBoolean("Permission To Place", m_permissionToPlace);
		
		SmartDashboard.putNumber("Arm Setpoint", m_arm.getSetpoint());
		SmartDashboard.putNumber("Arm Error", m_arm.getClosedLoopError());
		SmartDashboard.putNumber("Arm Voltage", m_arm.getOutputVoltage());
		
		SmartDashboard.putString("TalonControlMode", m_arm.getControlMode().name());
	}

	public void initDefaultCommand() { //required method
	}
}

