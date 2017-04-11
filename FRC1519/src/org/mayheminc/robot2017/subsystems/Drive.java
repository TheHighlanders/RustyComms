package org.mayheminc.robot2017.subsystems;

import com.kauailabs.navx.frc.*;
import org.mayheminc.util.*;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.*;

import java.util.Arrays;

import org.mayheminc.robot2017.Robot;
import org.mayheminc.robot2017.RobotMap;
import org.mayheminc.robot2017.RobotPreferences;
import org.mayheminc.util.MB1340Ultrasonic;
import org.mayheminc.util.Utils;
import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

public class Drive extends Subsystem {

	History headingHistory = new History();

	// Brake modes
	public static final boolean BRAKE_MODE = true;
	public static final boolean COAST_MODE = false;

	// PID loop variables
	private PIDController m_HeadingPid;
	private PIDHeadingError m_HeadingError;
	private PIDHeadingCorrection m_HeadingCorrection;

	private PIDController m_HeadingPidAlignment;
	private PIDHeadingError m_HeadingErrorAlignment;
	private PIDHeadingCorrection m_HeadingCorrectionAlignment;

	// Talons
	private final CANTalon leftFrontTalon = new CANTalon(RobotMap.FRONT_LEFT_TALON);
	private final CANTalon leftRearTalon = new CANTalon(RobotMap.BACK_LEFT_TALON);
	private final CANTalon rightFrontTalon = new CANTalon(RobotMap.FRONT_RIGHT_TALON);
	private final CANTalon rightRearTalon = new CANTalon(RobotMap.BACK_RIGHT_TALON);
	private final CANTalon middleLeftTalon = new CANTalon(RobotMap.MIDDLE_LEFT_TALON);
	private final CANTalon middleRightTalon = new CANTalon(RobotMap.MIDDLE_RIGHT_TALON);

	// Sensors
	private AHRS Navx;

	// Driving mode
	private boolean m_speedRacerDriveMode = true; // set by default

	// NavX parameters
	private double m_desiredHeading = 0.0;

	private boolean m_useHeadingCorrection = true;
	private static final double kToleranceDegreesPIDControl = 0.2;
	private static final double kToleranceDegreesPIDAlignmentControl = 0.2;

	// Drive parameters
	// Todo: check gear ratio and final wheel size
	public static final double DISTANCE_PER_PULSE = 3.14 * 6.0 * 1.0 / (250.0 * 4.0); 
	// pi * diameter * (gear ratio) / (counts per rev)
	//the gear ratio is 1 because the encoder is on the output shaft
	public static final double DISTANCE_PER_PULSE_MIDDLE =  24.0 / 218916.0; //determined emperically 20170216

	// max wheel speed history:
	// was 130 in 2015;
	// in 2017, WPI used 500, max of 7.85fps.
	// 668 is "theoretical max" for WPI gear ratios
	// 850 seems to be max attained in 30 feet of driving on 22 March 2017 after gear ratio change
	private double m_maxWheelSpeed = 850.0; 

	public final double SPEEDUP_RATIO = 500.0/850.0;  // temporary kludge to compensate for gear ratio change

	private double m_maxMiddleWheelSpeed = 67500.0;
	private double m_voltageRampRate = 48.0;
	private double m_voltageRampRateStrafe = 36.0;

	private int m_iterationsSinceRotationCommanded = 0;
	private int m_iterationsSinceStrafeOnlyCommanded = 0;

	public final double LEFT_HEADING = 60.0;
	public final double RIGHT_HEADING = -60.0;
	public final double STRAIGHT_HEADING = 0.0;

	//20170216: P: 0.015; I: 0.001: D: 0.050;
	//20170222: P: 0.015; I: 0.0004; D:0.000;
	//20170306: P: 0.020, I: 0.002, D: 0.050;

	// 2017-03-08:  KBS:  comp bot parameters for driving:  P=0.010, I=0.000, D=0.000
	// 2017-03-08:  KBS:  alas, really need different parameters for aiming to boiler when stationary:
	// 2017-03-08:  KBS:  comp bot parameters for boiler aim:  P=0.017, I=0.003, D=0.005

	final double M_HEADING_P = 0.008; // was 0.010 with old gear set; was 0.017 for aiming to boiler when stationary
	final double M_HEADING_I = 0.00;  //  was 0.003 for aiming to boiler when stationary 
	final double M_HEADING_D = 0.00;  //  was 0.005 for aiming to boiler when stationary 

	final double M_HEADING_ALIGNMENT_P = 0.018;  // was 0.012 with old gear set
	final double M_HEADING_ALIGNMENT_I = 0.001; // was 0.0015 with old gear set
	final double M_HEADING_ALIGNMENT_D = 0.045;  // was 0.002 with old gear set
	// Was 0.005, 0, 0 at beginning of 3/28 

	/*********************************** INITIALIZATION **********************************************************/

	public Drive() {
		try {
			/* Communicate w/navX MXP via the MXP SPI Bus. */
			/*
			 * Alternatively: I2C.Port.kMXP, SerialPort.Port.kMXP or
			 * SerialPort.Port.kUSB
			 */
			/*
			 * See
			 * http://navx-mxp.kauailabs.com/guidance/selecting-an-interface/
			 * for details.
			 */
			Navx = new AHRS(SPI.Port.kMXP);
		} catch (RuntimeException ex) {
			DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
		}

		// create a PID Controller that reads the heading error and outputs the
		// heading correction.
		m_HeadingError = new PIDHeadingError();
		m_HeadingCorrection = new PIDHeadingCorrection();

		m_HeadingErrorAlignment = new PIDHeadingError();
		m_HeadingCorrectionAlignment = new PIDHeadingCorrection();


		m_HeadingPid = new PIDController(M_HEADING_P, M_HEADING_I, M_HEADING_D, m_HeadingError, m_HeadingCorrection);
		m_HeadingPidAlignment = new PIDController(M_HEADING_ALIGNMENT_P, M_HEADING_ALIGNMENT_I, M_HEADING_ALIGNMENT_D, m_HeadingErrorAlignment, m_HeadingCorrectionAlignment);

		m_HeadingPid.setInputRange(-180.0f, 180.0f);
		m_HeadingPid.setContinuous(true); // treats the input range as "continuous" with wrap-around
		m_HeadingPid.setOutputRange(-1.0, 1.0); // set the maximum power to correct twist
		m_HeadingPid.setAbsoluteTolerance(kToleranceDegreesPIDControl);

		m_HeadingPidAlignment.setInputRange(-180.0f, 180.0f);
		m_HeadingPidAlignment.setContinuous(true); // treats the input range as "continuous" with wrap-around
		m_HeadingPidAlignment.setOutputRange(-.25, .25); // set the maximum power to correct twist
		m_HeadingPidAlignment.setAbsoluteTolerance(kToleranceDegreesPIDAlignmentControl);

		m_HeadingPidAlignment.reset();
		m_HeadingPidAlignment.enable();

		middleLeftTalon.changeControlMode(TalonControlMode.Follower);
		middleLeftTalon.set(middleRightTalon.getDeviceID());

		setBrakeMode(true);

		// see section 12.4 of Talon SRX Manual for Velocity Closed-Loop Walkthrough
		// describing how to set these PIDF parameters
		double wheelP = 2.0; //1.5
		double wheelI = 0.0;
		double wheelD = 0.0; //.5
		double wheelF = 1023.0 / m_maxWheelSpeed;    // should be 1023 / m_maxWheelSpeed; 

		double middleWheelP = 0.021;
		double middleWheelI = 0.0;
		double middleWheelD = 0.0;
		double middleWheelF = 0.0147;

		// talon closed loop config
		configureCanTalon(leftFrontTalon, wheelP, wheelI, wheelD, wheelF);
		configureCanTalon(rightFrontTalon, wheelP, wheelI, wheelD, wheelF);
		configureCanTalon(leftRearTalon, wheelP, wheelI, wheelD, wheelF);
		configureCanTalon(rightRearTalon, wheelP, wheelI, wheelD, wheelF);

		// for now, just have middle wheel in Vbus mode...
		middleRightTalon.changeControlMode(TalonControlMode.PercentVbus);
		middleRightTalon.setVoltageRampRate(m_voltageRampRateStrafe);
		//		configureCanTalon(middleRightTalon, middleWheelP, middleWheelI, middleWheelD, middleWheelF);
	}

	public void init() {
		// reset the NavX
		zeroHeadingGyro();

		//Why is this here? SSM
		//		rightFrontTalon.set(1);
	}

	/** Required Method */
	public void initDefaultCommand() {
	}

	//	public void goToZeroHeading(){
	//
	//	}

	public void zeroHeadingGyro() {
		Navx.zeroYaw();
		DriverStation.reportError("heading immediately after zero = " + getHeading() + "\n", false);

		m_desiredHeading = 0.0;
		m_headingOffset = 0.0;

		SmartDashboard.putString("Trace", "Zero Heading Gyro");

		// restart the PID controller loop
		m_HeadingPid.reset();
		m_HeadingPid.enable();

		m_HeadingPidAlignment.reset();
		m_HeadingPidAlignment.enable();
	}

	private void configureCanTalon(CANTalon talon, double p, double i, double d, double f) {

		talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		// Note: comment out below line so that encoder units are forced to be
		// in counts (x4)
		// talon.configEncoderCodesPerRev(360);

		talon.reverseSensor(false);
		talon.configNominalOutputVoltage(+0.0f, -0.0f);
		talon.configPeakOutputVoltage(+12.0, -12.0);
		talon.changeControlMode(TalonControlMode.Speed);
		//		talon.changeControlMode(TalonControlMode.PercentVbus);

		talon.setPID(p, i, d, f, 0, m_voltageRampRate, 0);
		talon.enableControl();
		talon.setEncPosition(0);

		DriverStation.reportError("setWheelPIDF: " + p + " " + i + " " + d + " " + f + "\n", false);
	}

	public void vBusControl(double power){
		positiveSimpleDrive(power, power, 0.0);
	}

	/**
	 * Set the "Brake Mode" behavior on the drive talons when "in neutral"
	 * 
	 * @param brakeMode
	 *            - true for "brake in neutral" and false for "coast in neutral"
	 */
	public void setBrakeMode(boolean brakeMode) {
		leftFrontTalon.enableBrakeMode(brakeMode);
		leftRearTalon.enableBrakeMode(brakeMode);
		rightFrontTalon.enableBrakeMode(brakeMode);
		rightRearTalon.enableBrakeMode(brakeMode);

		// the h-drive center wheel should never be set to brake (true) or robot jumps at the end of a strafe.
		middleLeftTalon.enableBrakeMode(false);		
		middleRightTalon.enableBrakeMode(false);
	}

	public double distanceTravelledMeters() {
		double y = Navx.getDisplacementY();
		double x = Navx.getDisplacementX();

		// Pythagorean theorem
		// return Math.sqrt(y * y + x * x);

		// assume linear motion only
		return 0.0;// x;
	}
	
	double [] initialWheelDistance = new double[4];
	double [] calcWheelDistance = new double[4];
	/**
	 * Start a distance travel
	 */
	public void saveInitialWheelDistance(){
		initialWheelDistance[0] = rightFrontTalon.getPosition();
		initialWheelDistance[1] = rightRearTalon.getPosition();
		initialWheelDistance[2] = leftFrontTalon.getPosition();
		initialWheelDistance[3] = leftRearTalon.getPosition();
	}
	/**
	 * Calculate the distance travelled.  Return the second shortest distance.
	 * If a wheel is floating, it will have a larger value - ignore it.
	 * If a wheel is stuck, it will have a small value
	 * @return
	 */
	public double getWheelDistance(){
		calcWheelDistance[0] = Math.abs(rightFrontTalon.getPosition() - initialWheelDistance[0]);
		calcWheelDistance[1] = Math.abs(rightRearTalon.getPosition() - initialWheelDistance[1]);
		calcWheelDistance[2] = Math.abs(leftFrontTalon.getPosition() - initialWheelDistance[2]);
		calcWheelDistance[3] = Math.abs(leftRearTalon.getPosition() - initialWheelDistance[3]);
		
		Arrays.sort(calcWheelDistance);
		return  calcWheelDistance[1];
	}

	public int getRightEncoder() {
		return (int) rightFrontTalon.getPosition();
	}
	public int getMiddleEncoder() {
		return (int) middleRightTalon.getPosition();
	}
	public int getLeftEncoder() {
		return (int) leftFrontTalon.getPosition();
	}

	// speed is in inches per second
	public double getRightSpeed() {
		return  rightFrontTalon.getSpeed();
	}

	public double getLeftSpeed() {
		return  leftFrontTalon.getSpeed();
	}

	// ***********************************GYRO**********************************************************

	//makes sure our range is from -180 to 180
	public double calculateHeadingError(double Target) {
		//not the problem with the autotarettoboiler bug
		double currentHeading = getHeading();
		double error = Target - currentHeading;
		error = error % 360.0;
		if (error > 180.0) {
			error -= 360.0;
		}
		return error;	
	}

	public boolean getHeadingCorrectionMode() {
		return m_useHeadingCorrection;
	}

	public void setHeadingCorrectionMode(boolean useHeadingCorrection) {
		m_useHeadingCorrection = useHeadingCorrection;
	}

	static private final double STATIONARY = 0.1;
	static private double m_prevLeftDistance = 0.0;
	static private double m_prevRightDistance = 0.0;

	public boolean isStationary() {
		double leftDistance = getLeftEncoder();
		double rightDistance = getRightEncoder();

		double leftDelta = Math.abs(leftDistance - m_prevLeftDistance);
		double rightDelta = Math.abs(rightDistance - m_prevRightDistance);

		m_prevLeftDistance = leftDistance;
		m_prevRightDistance = rightDistance;

		return leftDelta < STATIONARY && rightDelta < STATIONARY;
	}

	public void displayGyroInfo() {
		SmartDashboard.putNumber("Gyro Heading", Utils.twoDecimalPlaces(getHeading()));
		SmartDashboard.putNumber("Gyro Roll", Utils.twoDecimalPlaces(Navx.getRoll()));
		SmartDashboard.putNumber("Gyro Pitch", Utils.twoDecimalPlaces(Navx.getPitch()));
		SmartDashboard.putNumber("Navx distance", Utils.twoDecimalPlaces(this.distanceTravelledMeters() * 100));
		SmartDashboard.putNumber("Navx distance Y", Utils.twoDecimalPlaces(Navx.getDisplacementY() * 100));
		SmartDashboard.putNumber("Navx distance X", Utils.twoDecimalPlaces(Navx.getDisplacementX() * 100));
	}

	private double m_headingOffset = 0.0;
	public void setHeadingOffset(double offset){
		m_headingOffset += offset;
		m_desiredHeading += offset;
	}
	public double getHeading() {
		return Navx.getYaw() + m_headingOffset;
	}
	public double getTrimmedHeading(){
		double heading = getHeading();
		heading = heading % 360.0;
		if (heading > 180.0) {
			heading -= 360.0;
		}
		return heading;

	}

	public double getTilt() {
		return Navx.getPitch();
	}

	public double getDesiredHeading() {
		return m_desiredHeading;
	}

	private void simpleDrive(double leftPower, double rightPower, double middlePower) {
		// Note that due to the way that the Y axis on the default joysticks
		// give a "-1" when pressed forward, and a "+1" when pulled backward,
		// it is a historical vestige that this function will result in the
		// robot going forward when the power arguments are negative

		// clip the values to [-1.0, +1.0]
		// don't clip for closed loop mode - RJD
		//		if (rightPower > 1.0) {
		//			rightPower = 1.0;
		//		} else if (rightPower < -1.0) {
		//			rightPower = -1.0;
		//		}
		//		if (leftPower > 1.0) {
		//			leftPower = 1.0;
		//		} else if (leftPower < -1.0) {
		//			leftPower = -1.0;
		//		}
		//		if (middlePower > 1.0) {
		//			middlePower = 1.0;
		//		} else if (middlePower < -1.0) {
		//			middlePower = -1.0;
		//		}

		setMotorPower(leftPower, rightPower, middlePower);
	}

	// for a "positive means forward" alternative to simpleDrive, use
	// the below "positiveSimpleDrive" method instead
	public void positiveSimpleDrive(double leftPower, double rightPower, double middlePower) {
		simpleDrive(leftPower, rightPower, middlePower);
	}

	private static final double MIN_ERROR_THRESHOLD = 0.2;
	// private static final double CORRECTION_FACTOR = 0.75;
	private static final double CORRECTION_FACTOR = 1.00;

	// Note that due to the way that the Y axis on the default joysticks
	// give a "-1" when pressed forward, and a "+1" when pulled backward,
	// it is a historical vestige that this function will result in the
	// robot going forward when the power argument is negative
	public void driveStraight(double power) {
		double left = power;
		double right = power;
		double error = 0.0;

		if (power < 0) {
			// Going forward, +ve error means pull back on left
			if (error > MIN_ERROR_THRESHOLD) {
				// Back off on the left side
				if (power < -0.5) {
					left = left * CORRECTION_FACTOR;
				} else {
					right = right / CORRECTION_FACTOR;
				}
			} else if (error < -MIN_ERROR_THRESHOLD) {
				// Back off on the right side
				if (power < -0.5) {
					right = right * CORRECTION_FACTOR;
				} else {
					left = left / CORRECTION_FACTOR;
				}
			}
		} else {
			// Going backward, +ve error means pull back on right
			if (error > MIN_ERROR_THRESHOLD) {
				// Back off on the right side
				if (power > 0.5) {
					right = right * CORRECTION_FACTOR;
				} else {
					left = left / CORRECTION_FACTOR;
				}
			} else if (error < -MIN_ERROR_THRESHOLD) {
				// Back off on the left side
				if (power > 0.5) {
					left = left * CORRECTION_FACTOR;
				} else {
					right = right / CORRECTION_FACTOR;
				}
			}
		}

		setMotorPower(left, right, 0);
	}

	// for a "positive means forward" alternative to driveStraight, use
	// the below "positiveSimpleDrive" method instead
	public void positiveDriveStraight(double power) {
		driveStraight(-power);
	}

	public void resetNavXDisplacement() {
		Navx.resetDisplacement();
	}

	public void stop() {
		setMotorPower(0, 0, 0);
	}

	public void recalibrateHeadingGyro() {
		// headingGyro.free();
		// headingGyro = new AnalogGyro(RobotMap.HEADING_GYRO);
		Navx.free();
		Navx = new AHRS(SPI.Port.kMXP);
	}

	//private int LoopCounter = 0;

	private void setMotorPower(double leftPower, double rightPower, double middlePower) {
		rightFrontTalon.set(-rightPower * m_maxWheelSpeed);
		leftFrontTalon.set(leftPower * m_maxWheelSpeed);

		rightRearTalon.set(-rightPower * m_maxWheelSpeed);
		leftRearTalon.set(leftPower * m_maxWheelSpeed);

		// middle wheels just use PercentVbus mode, so don't multiply by maximum speed
		middleRightTalon.set(middlePower);

	}

	/**
	 * Update the Smart Dashboard with the Power Distribution Panel currents.
	 */
	//	private void updateSdbPdp() {
	//		PowerDistributionPanel pdp = new PowerDistributionPanel();
	//
	//		double lf;
	//		double rf;
	//		double lb;
	//		double rb;
	//		double fudgeFactor = 0.0;
	//
	//		lf = pdp.getCurrent(RobotMap.DRIVE_FRONT_LEFT_PDP) - fudgeFactor;
	//		rf = pdp.getCurrent(RobotMap.DRIVE_FRONT_RIGHT_PDP) - fudgeFactor;
	//		lb = pdp.getCurrent(RobotMap.DRIVE_BACK_LEFT_PDP) - fudgeFactor;
	//		rb = pdp.getCurrent(RobotMap.DRIVE_BACK_RIGHT_PDP) - fudgeFactor;
	//
	//		SmartDashboard.putNumber("Left Front I", lf);
	//		SmartDashboard.putNumber("Right Front I", rf);
	//		SmartDashboard.putNumber("Left Back I", lb);
	//		SmartDashboard.putNumber("Right Back I", rb);
	//	}

	public double getHeadingForCapturedImage(){
		double now = Timer.getFPGATimestamp();
		double indexTime = now - CAMERA_LAG;
		return headingHistory.getAzForTime(indexTime);
	}

	public void stoppedAligningToLift() {
		m_alignToLiftState = AlignToLiftStates.INIT;
	}

	public void forceAlignQuit(){
		if (m_alignToLiftState == AlignToLiftStates.ALIGN){
			m_alignToLiftState = AlignToLiftStates.INIT_CREEP_FORWARD;
		}
	}

	public void periodic() {	
		updateHistory();
		targetBoiler(AIM_REQUIRED_LOOPS_ON_TARGET_TELEOP);
		m_count++;
	}


	private final double PLACE_GEAR_SPEED = -0.15;
	public void placeGear(){

		if(!Robot.autonomous.isAutonomousMode() && //we are in teleop
				!Robot.gearFloorPickerUpper.getPermissionToPlace()){
			DriverStation.reportError("unexpected failure in drive.placeGear() : Operator PermissionToPlace not depressed when in teleop mode.", false);
			return;
		}	
		hDrive(PLACE_GEAR_SPEED, 0.0, 0.0, false, false);
		Robot.gearFloorPickerUpper.placeGear();
	}

	private enum AlignToLiftStates { 
		INIT, 
		ALIGN, //bring the robot to its "happy spot" ~2ft away from target
		INIT_CREEP_FORWARD, CREEP_FORWARD, //drive forward ~3-4" away from peg
		INIT_BLIND_DRIVE, BLIND_DRIVE, //drive forward for time while monitoring encoder ticks
		EVALUATE, //did we get spoked?
		BACK_UP, //if we were spoked, back out from the spoke
		INIT_ADJUST, ADJUST,  //if we were spoked, strafe left/right (next: goto init_blind_drive)
		DONE
	};
	private AlignToLiftStates m_alignToLiftState = AlignToLiftStates.INIT;

	private double m_blindDriveStartTime = 0.0;
	private double m_blindDriveStartPosition = 0.0;
	private double m_creepForwardStartPosition = 0.0;
	private double m_adjustStartPosition = 0.0;
	private boolean m_adjustToRight = true;

	public final double CREEP_FORWARD_DISTANCE = 240.0;
	public final double BLIND_DRIVE_DURATION = 1.3;
	//public final double BLIND_DRIVE_EXPECTED_DISTANCE = 325;//550.0;
	public final double ARM_IS_SPOKED_ENCODER_COUNT = -1000;
	public final double BLIND_DRIVE_DISPLACEMENT = 510;

	public final double ADJUSTMENT_DISTANCE = 20000;

	final double LIFT_X_TOLERANCE_S = 7.0;
	final double LIFT_Y_TOLERANCE_S = 10.0;
	final double LIFT_Y_DESIRED = 77.0;
	//public final double STOP_CREEPING_FORWARD = 100.0;

	public final double FIFTH_SPEED_OVERSHOOT_DISTANCE = 75.0; //don't rely on this for other applications please

	public void autoTargetToLift() {
		double strafeAmount = 0.0;
		double forwardAmount = 0.0;

		// check to see if we should set the desiredHeading to a gear-aligning angle
		if ((m_desiredHeading != -60) && (m_desiredHeading != 0) && (m_desiredHeading != 60)) {

			// 	determine which of -60, 0, 60 is nearest and set m_desiredHeading appropriately
			double currentHeading = getTrimmedHeading();
			if ((-120 < currentHeading) && (currentHeading < -30)) {
				m_desiredHeading = -60;
			} else if ((-30 < currentHeading) && (currentHeading < 30)) {
				m_desiredHeading = 0;
			} else if ((30 < currentHeading) && (currentHeading < 120)) {
				m_desiredHeading = 60;
			}

			// reset the PID controller loop now that we have a new desired heading
			m_HeadingPid.reset();
			m_HeadingPid.enable(); // need to re-enable the PID
		}

		//// KBS: TEMPORARY CODE FOR TESTING FLOOR PICKUP, skip rest of align-to-lift
		//if (true) { return; }

		if (m_alignToLiftState == AlignToLiftStates.INIT) {	
			//please don't kill the 'INIT' state!
			//m_alignToLiftState = AlignToLiftStates.ALIGN;
			//to test auto-recovery without working gear targeting
			m_alignToLiftState = AlignToLiftStates.ALIGN;
		} else if (m_alignToLiftState == AlignToLiftStates.ALIGN) {
			if (!Robot.isGearVisible()) {
				DriverStation.reportError("Could not see gear target, not moving", false);
				return;
			}

			// we're not yet aligned to the lift, let's get aligned!
			// strafe amount is computed based upon gear "CenterX"  location 
			strafeAmount = ((double) Robot.getGearCenterX()) * 0.006;  // was .01 at WPI

			final double MIN_STRAFE = 0.20;
			if ((strafeAmount > 0.01) && (strafeAmount < MIN_STRAFE)) { strafeAmount = MIN_STRAFE; }
			if ((strafeAmount < -0.01) && (strafeAmount > -MIN_STRAFE)) { strafeAmount = -MIN_STRAFE; }

			final double MAX_STRAFE = 0.30;
			if (strafeAmount > MAX_STRAFE) { strafeAmount = MAX_STRAFE; }
			if (strafeAmount < -MAX_STRAFE) { strafeAmount = -MAX_STRAFE; }

			// forward amount is computed based upon gear "CenterY"  location
			//				forwardAmount = 0.20;
			forwardAmount = ( ((double) Robot.getGearCenterY()) - LIFT_Y_DESIRED ) * -0.005;

			final double FORWARD_DEAD_ZONE = 0.05;
			if ((forwardAmount > -FORWARD_DEAD_ZONE) && (forwardAmount < FORWARD_DEAD_ZONE)) { 
				forwardAmount = 0.00;
			}

			//			final double MIN_DRIVE = 0.05;
			//			if ((forwardAmount > 0.01) && (forwardAmount < MIN_DRIVE)) { forwardAmount = MIN_DRIVE; }
			//			if ((forwardAmount < -0.01) && (forwardAmount > -MIN_DRIVE)) { forwardAmount = -MIN_DRIVE; }

			final double MAX_DRIVE_FORWARDS = 0.2;
			final double MAX_DRIVE_BACKWARDS = -0.15;
			if (forwardAmount > MAX_DRIVE_FORWARDS) { forwardAmount = MAX_DRIVE_FORWARDS; }
			if (forwardAmount < MAX_DRIVE_BACKWARDS) { forwardAmount = MAX_DRIVE_BACKWARDS; }		

			// tell the robot to drive as computed
			hDrive(forwardAmount, 0.0, strafeAmount,  false,  false);
			//			DriverStation.reportError("forwardAmount: " + forwardAmount + "; strafeAmount = " + strafeAmount, false);
			if (isAlignedToLift()) {
//				m_alignToLiftState = AlignToLiftStates.INIT_CREEP_FORWARD;
				m_alignToLiftState = AlignToLiftStates.INIT_BLIND_DRIVE;
			}
		} else if (m_alignToLiftState == AlignToLiftStates.INIT_CREEP_FORWARD) {
			stop();
			m_creepForwardStartPosition = getRightEncoder();
			m_alignToLiftState = AlignToLiftStates.CREEP_FORWARD;
		} else if (m_alignToLiftState == AlignToLiftStates.CREEP_FORWARD) {
			hDrive(0.15, 0.0, 0.0, false, false);
			if (Math.abs(m_creepForwardStartPosition - getRightEncoder()) >= CREEP_FORWARD_DISTANCE){
				m_alignToLiftState = AlignToLiftStates.INIT_BLIND_DRIVE;
			}
			//			if(Robot.getGearCenterY() <= STOP_CREEPING_FORWARD){
			//				m_alignToLiftState = AlignToLiftStates.INIT_BLIND_DRIVE;
			//			}
		} else if (m_alignToLiftState == AlignToLiftStates.INIT_BLIND_DRIVE) {
			stop();
			DriverStation.reportError("State: INIT_BLIND_DRIVE", false);

			m_blindDriveStartTime = Timer.getFPGATimestamp();
			m_blindDriveStartPosition = getRightEncoder();

			m_alignToLiftState = AlignToLiftStates.BLIND_DRIVE;
		} else if (m_alignToLiftState == AlignToLiftStates.BLIND_DRIVE) {
			DriverStation.reportError("State: BLIND_DRIVE", false);

			// we recently concluded we were sufficiently aligned to the lift, so now we're just "going for it"
			// keep driving straight forward
			hDrive(0.15, 0.0, 0.0 /*strafeAmount*/, false, false);

						if ((Timer.getFPGATimestamp() - m_blindDriveStartTime) >= BLIND_DRIVE_DURATION) {
							m_alignToLiftState = AlignToLiftStates.DONE;
						}
//			if(Robot.gearFloorPickerUpper.getArmEncoder() <= ARM_IS_SPOKED_ENCODER_COUNT){
//				m_alignToLiftState = AlignToLiftStates.BACK_UP;
//			}
//
//			double displacement = Math.abs(getRightEncoder() - m_blindDriveStartPosition);
//			if(displacement >= BLIND_DRIVE_DISPLACEMENT){
//				m_alignToLiftState = AlignToLiftStates.DONE;
//			}

		}
		//		else if (m_alignToLiftState == AlignToLiftStates.EVALUATE){
		//			double distanceTraveled = Math.abs(m_blindDriveStartPosition - getRightEncoder());
		//			if(distanceTraveled >= BLIND_DRIVE_EXPECTED_DISTANCE){
		//				//gear placed successfully
		//				m_alignToLiftState = AlignToLiftStates.DONE;
		//			}else{
		//				//gear placed unsuccessfully
		//				m_alignToLiftState = AlignToLiftStates.BACK_UP;
		//			}
		//
		//		}
		else if(m_alignToLiftState == AlignToLiftStates.BACK_UP){
			//backup 
			hDrive(-0.2, 0.0, 0.0, false, false);
			if(getRightEncoder() >= (m_blindDriveStartPosition - FIFTH_SPEED_OVERSHOOT_DISTANCE)){
				m_alignToLiftState = AlignToLiftStates.INIT_ADJUST;
			}
		}else if(m_alignToLiftState == AlignToLiftStates.INIT_ADJUST){
			stop();
			m_adjustToRight = true;//(Robot.getGearCenterX() > 0.0);
			m_adjustStartPosition = getMiddleEncoder();
			m_alignToLiftState = AlignToLiftStates.ADJUST;

		}else if (m_alignToLiftState == AlignToLiftStates.ADJUST) {
			hDrive(0.0, 0.0, (m_adjustToRight ? 0.2 : -0.2), false, false);
			double distanceTraveled = Math.abs(m_adjustStartPosition - getMiddleEncoder());
			if (distanceTraveled >= ADJUSTMENT_DISTANCE) {
				m_alignToLiftState = AlignToLiftStates.INIT_BLIND_DRIVE;
			}

		} else if (m_alignToLiftState == AlignToLiftStates.DONE) {
			DriverStation.reportError("State: DONE", false);
			stop();
		}

		// new thoughts:
		// when two targets, do what we do now
		// when one target, strafe to center the target(s) that we see
		// when one target, adjust forward/backward to get to "optimal distance"
		// when previously had two centered targets, at some
		//      "close enough distance", but don't anymore, just go for it.

		// suggestions:
		// strafeAmount = function(Robot.getGearCenterX())
		// forward/back = function(Robot.getGearCenterY()) (seeks to be 2' from wall)
		// if getGearCenter(X) < threshold (adequately centered)
		// 		and forward/back good, just go for it

		//////////////////////////////////////////////////////////////////////////////////////
		// Below is the auto-align to lift code as it was before varying forwardAmount:

		//		// if we are aligned left-to-right...
		//		if (isAlignedToLift()) {
		//			// drive straight forward
		//			hDrive(0.35, 0.0,  strafeAmount,  false,  false);
		//			DriverStation.reportError("AutoAlignLift Driving forward as target has been locked", false);
		//		} else {
		//			SmartDashboard.putString("Trace", "AutoAlignLift H: ");
		//			// we're mis-aligned, need to strafe left or right while driving forward slowly
		//			// KBS:  Would prefer for strafe speed to be proportional to error
		//			// (At WPI, had been constants of -0.2 or + 0.2)
		//			// want an error of LIFT_TOLERANCE_S to result in a power of 0.05
		//			strafeAmount = ((double) Robot.getGearCenterX()) / ((double) LIFT_TOLERANCE_S) * 0.05;
		//			
		//			// clip the strafe to 0.35
		//			if (strafeAmount > 0.35) { strafeAmount = 0.35; }
		//			if (strafeAmount < -0.35) { strafeAmount = -0.35; }
		////			hDrive(0.2, 0.0,  (Robot.getGearCenterX() < -LIFT_TOLERANCE_S) ? -0.2 : 0.2 ,  false,  false);
		//			hDrive(0.2, 0.0, strafeAmount,  false,  false);
		//		}
	}	
	public boolean isDonePlacingGear() {
		return m_alignToLiftState == AlignToLiftStates.DONE;
	}

	public boolean isAlignedToLift(){
		// conceptually, aligned to lift if all of the following are true:
		// A - can see two targets
		// B - aligned to the "CenterX" of the targets (pointed at targets)
		// C - aligned to the "CenterY" of the targets (at proper range)
		return (Robot.getGearCenterX() > -LIFT_X_TOLERANCE_S
				&& Robot.getGearCenterX() < LIFT_X_TOLERANCE_S
				&& (Robot.getGearCenterY() - LIFT_Y_DESIRED) > -LIFT_Y_TOLERANCE_S
				&& (Robot.getGearCenterY() - LIFT_Y_DESIRED) < LIFT_Y_TOLERANCE_S
				//				&& Robot.getGearNumberOfTargets() >= 2
				);
	}


	int m_HeadingCorrectionalLoop;
	/**
	 * 1 wheel in each corner, 1 wheel in the center.  Each wheel is an omni.  The center wheel
	 * is turned 90 degees.
	 * @param throttle Forward/Backwards power
	 * @param rawSteeringX Tank-drive turning power
	 * @param middlePower  Strafing power
	 * @param quickTurn Turn quickly by driving one side backwards.
	 */
	public void hDrive(double throttle, double rawSteeringX, double middlePower, boolean quickTurn, boolean slowModifier) {
		hDrive(throttle, rawSteeringX, middlePower, quickTurn, slowModifier, m_desiredHeading);
	}

	public void hDrive(double throttle, double rawSteeringX, double middlePower, boolean quickTurn, boolean slowModifier, double heading) {
		double leftPower, rightPower;
		double rotation = 0;
		final double STEERING_GAIN = .7;
		double adjustedSteeringX = rawSteeringX * Math.max(Math.abs(throttle), Math.abs(middlePower)) * STEERING_GAIN;
		final double QUICK_TURN_GAIN = 0.75 * STEERING_GAIN; // culver drive used 1.5
		final double SLOW_MODE_QUICK_TURN_GAIN = 1.15 * QUICK_TURN_GAIN;  // spin a little faster in SLOW_MODE

		m_desiredHeading = heading;

		// determine if we are going forwards or backwards
		int throttleSign;
		if (throttle >= 0.0) {
			throttleSign = 1;
		} else {
			throttleSign = -1;
		}

		// if we are not turning...

		if (rawSteeringX == 0.0) {
			// no turn being commanded, drive straight or stay still
			m_iterationsSinceRotationCommanded++;
			if (throttle == 0.0) {

				if(middlePower != 0.0){
					m_iterationsSinceStrafeOnlyCommanded = 0;
					//pod power being commanded, let's keep correcting as if we are driving straight
					if ((m_iterationsSinceRotationCommanded == 10) || (m_iterationsSinceStrafeOnlyCommanded >= 5)) {
						// exactly five iterations with no commanded turn, get
						// current heading as desired heading
						m_desiredHeading = getHeading();
						// reset the PID controller loop now that we have a new
						// desired heading
						m_HeadingPid.reset();
						m_HeadingPid.enable(); // need to re-enable the PID
						// controller after a reset()
						rotation = 0.0;
					} else if (m_iterationsSinceRotationCommanded < 10) {
						// just start driving straight without special heading
						// maintenance
						rotation = 0.0;
					} else if (m_iterationsSinceRotationCommanded > 10) {
						// after more then five iterations since commanded turn,
						// maintain the target heading
						rotation = maintainHeading();
						SmartDashboard.putNumber("Heading_Correctional_Loop", m_HeadingCorrectionalLoop++);
					}
				}else{
					// no motion commanded, stay still
					m_iterationsSinceStrafeOnlyCommanded++;
					rotation = 0.0;
					m_desiredHeading = getHeading(); // whenever motionless, set
					// desired heading to
					// current heading
					// reset the PID controller loop now that we have a new desired
					// heading
					m_HeadingPid.reset();
					m_HeadingPid.enable(); // need to re-enable the PID
					// controller after a reset()
				}
			} else {
				// driving straight
				if ((m_iterationsSinceRotationCommanded == 10) || (m_iterationsSinceStrafeOnlyCommanded >= 5)) {
					// exactly five iterations with no commanded turn, get
					// current heading as desired heading
					m_desiredHeading = getHeading();
					// reset the PID controller loop now that we have a new
					// desired heading
					m_HeadingPid.reset();
					m_HeadingPid.enable(); // need to re-enable the PID
					// controller after a reset()
					rotation = 0.0;
				} else if (m_iterationsSinceRotationCommanded < 10) {
					// just start driving straight without special heading
					// maintenance
					rotation = 0.0;
				} else if (m_iterationsSinceRotationCommanded > 10) {
					// after more then five iterations since commanded turn,
					// maintain the target heading
					rotation = maintainHeading();
					SmartDashboard.putNumber("Heading_Correctional_Loop", m_HeadingCorrectionalLoop++);
				}
				m_iterationsSinceStrafeOnlyCommanded = 0;
			}
		} else {
			// commanding a turn, reset iterationsSinceRotationCommanded
			m_iterationsSinceRotationCommanded = 0;
			m_iterationsSinceStrafeOnlyCommanded = 0;

			if (quickTurn) {
				// want a high-rate turn (also allows "spin" behavior)
				if (slowModifier) {
					// use a bigger quickTurn gain when in "slow_mode"
					rotation = rawSteeringX * throttleSign * SLOW_MODE_QUICK_TURN_GAIN;
				} else {
					// use a normal quickTurn gain otherwise
					rotation = rawSteeringX * throttleSign * QUICK_TURN_GAIN;
				}
			} else {
				// want a standard rate turn (scaled by the throttle)
				rotation = adjustedSteeringX * throttleSign; // set the turn to the
				// throttle-adjusted steering
				// input
			}
		}

		// power to each wheel is a combination of the throttle and rotation
		leftPower = throttle + rotation;
		rightPower = throttle - rotation;

		final double SLOW_MODIFIER = 0.22;
		final double SLOW_MODIFIER_MIDDLE = 0.35;

		if (slowModifier) {
			leftPower *= SLOW_MODIFIER;
			rightPower *= SLOW_MODIFIER;
			middlePower *= SLOW_MODIFIER_MIDDLE;
		}

		simpleDrive(leftPower, rightPower, middlePower);
	}

	public int stage = 0;
	public final double DELAY = 4;
	public double timerStart = Timer.getFPGATimestamp();

	public boolean selfTest() {
		return false;
	}

	/**
	 * The headings are from -180 to 180. The CurrentHeading is the current
	 * robot orientation. The TargetHeading is where we want the robot to face.
	 * 
	 * e.g. Current = 0, Target = 10, error = 10 Current = 180, Target = -170,
	 * error = 10 (we need to turn 10 deg Clockwise to get to -170) Current =
	 * -90, Target = 180, error = -90 (we need to turn 90 deg Counter-Clockwise
	 * to get to 180) Current = 10, target = -10, error = -20 (we need to turn
	 * Counterclockwise -20 deg)
	 * 
	 * @param CurrentHeading
	 * @param TargetHeading
	 * @return
	 */

	private double maintainHeading() {
		double headingError = calculateHeadingError(m_desiredHeading);
		m_HeadingError.m_Error = headingError;
		double headingCorrection = 0.0;

		m_HeadingError.m_Error = m_desiredHeading - getHeading();

		if (m_useHeadingCorrection) {
			headingCorrection = -m_HeadingCorrection.HeadingCorrection;

		} else {
			headingCorrection = 0.0;
		}
		return headingCorrection;
	}

	double m_HeadingAligmentPrevError;

	private double maintainHeadingAlignment() {
		double headingError = calculateHeadingError(m_desiredHeading);

		if( Math.abs(headingError) > 5.0)
		{
			m_HeadingPidAlignment.setPID(M_HEADING_ALIGNMENT_P, 0.0, M_HEADING_ALIGNMENT_D);
		}
		else
		{
			m_HeadingPidAlignment.setPID(M_HEADING_ALIGNMENT_P, M_HEADING_ALIGNMENT_I, M_HEADING_ALIGNMENT_D);
		}	

		m_HeadingErrorAlignment.m_Error = headingError;
		double headingCorrection = 0.0;

		m_HeadingErrorAlignment.m_Error = m_desiredHeading - getHeading();
		SmartDashboard.putNumber("HeadingAlignment Error", m_HeadingErrorAlignment.m_Error);
		headingCorrection = -m_HeadingCorrectionAlignment.HeadingCorrection;

		//		// if the sign of the error has changed...
		//		if( (headingError > 0 ) ^ (m_HeadingAligmentPrevError > 0 ) )
		//		{
		//			// reset the PID Controller to remove the I windup.
		//			m_HeadingPidAlignment.reset();
		//			m_HeadingPidAlignment.enable();
		//		}

		m_HeadingAligmentPrevError = headingError;
		return headingCorrection;
	}

	public void rotate(double RotateX) {
		m_desiredHeading += RotateX;
		if (m_desiredHeading > 180) {
			m_desiredHeading -= 360;
		}
		if (m_desiredHeading < -180) {
			m_desiredHeading += 360;
		}
	}

	//NOTE the difference between rotateToHeading(...) and goToHeading(...)
	public void setDesiredHeading(double desiredHeading) {
		m_desiredHeading = desiredHeading;
		m_iterationsSinceRotationCommanded = 50;
		m_iterationsSinceStrafeOnlyCommanded = 50;

		// reset the heading control loop for the new heading
		m_HeadingPid.reset();
		m_HeadingPid.enable();
	}

	public void resetGoToHeading(double heading){
		m_desiredHeading = heading;
		resetHeadingPidAlignment();
	}

	public void resetHeadingPidAlignment() {
		m_HeadingPidAlignment.reset();
		m_HeadingPidAlignment.enable();
	}

	public void goToHeading(double heading){
		m_desiredHeading = heading;
		double adjustment = maintainHeadingAlignment();
		positiveSimpleDrive(adjustment, -adjustment, 0);
	}

	// **********************************************DISPLAY****************************************************

	public void updateSmartDashboardTalon(String Prefix, CANTalon talon)
	{
		SmartDashboard.putNumber("" + Prefix + " Encoder Counts", talon.getPosition());
		SmartDashboard.putNumber("" + Prefix + " Speed", talon.getSpeed());
		SmartDashboard.putNumber("" + Prefix + " Set Point", talon.getSetpoint());
	}

	public void constantUpdateSmartDashboard(){
		SmartDashboard.putBoolean("On Target", isAlignedToBoiler(AIM_REQUIRED_LOOPS_ON_TARGET_TELEOP));
		SmartDashboard.putNumber("GearCenterX", Robot.getGearCenterX());
		SmartDashboard.putNumber("GearCenterY", Robot.getGearCenterY());
		SmartDashboard.putBoolean("GearOnTarget", isAlignedToLift());
	}

	public void updateSmartDashboard() {
		displayGyroInfo();

		updateSmartDashboardTalon("Left Front", leftFrontTalon);
		updateSmartDashboardTalon("Left Rear", leftRearTalon);
		updateSmartDashboardTalon("Right Front", rightFrontTalon);
		updateSmartDashboardTalon("Right Rear", rightRearTalon);
		updateSmartDashboardTalon("Mid Left", middleLeftTalon);
		updateSmartDashboardTalon("Mid Right", middleRightTalon);

		SmartDashboard.putNumber("Voltage of Left Front Motor", leftFrontTalon.getOutputVoltage() );
		SmartDashboard.putBoolean("NavX Is Calibrating",  Navx.isCalibrating());

		SmartDashboard.putNumber("DesiredHeading", m_desiredHeading);
		SmartDashboard.putNumber("Heading Error", m_HeadingError.m_Error);
		SmartDashboard.putNumber("Heading Correction", -m_HeadingCorrection.HeadingCorrection);
		SmartDashboard.putBoolean("Heading Correction Mode", getHeadingCorrectionMode());

		SmartDashboard.putNumber("HeadingErrorAlignment", m_HeadingErrorAlignment.m_Error);
		SmartDashboard.putNumber("HeadingCorrectionAlignment", -m_HeadingCorrectionAlignment.HeadingCorrection);

		SmartDashboard.putString("autoTargetToLift() state", m_alignToLiftState.name()); 
	}

	public void autoTargetToBoiler() {

		final double DEGREES_PER_PIXEL = (56.5 / 320.0); // Axis 206 - 56.5 degree FOV, 320 s
		double error = Robot.getBoilerCenterX();
		if (error < 999){
			double correction = error * DEGREES_PER_PIXEL;
			double targetHeading = (getHeadingSinceImageCaptured() + correction);
			SmartDashboard.putNumber("Auto Aim: Target Heading", targetHeading);

			// update the PID Controller with the desired heading
			m_desiredHeading = targetHeading;

			// ask the PIDController for the adjustment needed to go to the desired
			// heading
			double adjustment = maintainHeadingAlignment();
			//			final double MAX_AIM_POWER = 0.5;
			//			if (adjustment > MAX_AIM_POWER) { adjustment = MAX_AIM_POWER;}
			//			if (adjustment < -MAX_AIM_POWER) {adjustment = -MAX_AIM_POWER;}

			positiveSimpleDrive(adjustment, -adjustment, 0);
			SmartDashboard.putNumber("Auto Aim: Drive Adjustment", adjustment);	
		}else{
			DriverStation.reportError("Did not rotate to boiler: no boiler found", false);
		}
	}

	private int m_loopsOnTarget = 0;
	private static final int AIM__TOLERANCE = 8;
	public static final int AIM_REQUIRED_LOOPS_ON_TARGET_TELEOP = 4;	
	public static final int AIM_REQUIRED_LOOPS_ON_TARGET_AUTONOMOUS = 4;

	public boolean targetBoiler(int requiredLoopsOnTarget){
		int diff = Math.abs(Robot.getBoilerCenterX());
		if (diff <= AIM__TOLERANCE) {
			m_loopsOnTarget++;
		} else {
			m_loopsOnTarget = 0;
		}
		return isAlignedToBoiler(requiredLoopsOnTarget);
	}

	public boolean isAlignedToBoiler(int requiredLoopsOnTarget) {

		// require multiple loops of being on target
		//DriverStation.reportError("in Robot.drive.isAlignedToBoiler()", false);
		return (m_loopsOnTarget >= requiredLoopsOnTarget);
	}

	final double CAMERA_LAG = 0.1;//0.13;  //good  //0.2 on 3/29/2017;

	public void updateHistory() {
		double now = Timer.getFPGATimestamp();
		headingHistory.add(now, getHeading());
	}

	public double getHeadingSinceImageCaptured() {
		double now = Timer.getFPGATimestamp();
		double indexTime = now - CAMERA_LAG;
		return headingHistory.getAzForTime(indexTime);
	}

	// **********************************MISC.***********************************************
	public void toggleSpeedRacerDrive() {
		m_speedRacerDriveMode = !m_speedRacerDriveMode;
	}

	public boolean isSpeedRacerDrive() {
		return m_speedRacerDriveMode;
	}

	int m_loopsSinceLockBoilerTargetAchieved = 0;
	int m_count = 0;


	/**
	 * @param driveThrottle the throttle that controls the drive throttle
	 * @param strafeThrottle the throttle that controls the strafe throttle; will be reversed iff we are on the blue alliance
	 */
	public void strafeBasedOnAllianceColor(double driveThrottle, double strafeThrottle){
		if(Robot.autonomous.isRed()) strafeThrottle *= -1;
		hDrive(driveThrottle, 0, strafeThrottle, false, false);
	}

	/**
	 * strafe get LZHeadingBasedOnAllianceColor
	 * @param driveThrottle the throttle that controls the drive throttle
	 * @param strafeThrottle the throttle that controls the strafe throttle; will be reversed iff we are on the blue alliance
	 */
	public double getLZHeadingBasedOnAllianceColor(){
		if (Robot.autonomous.isRed()) {
			return -90.0;
		} else {
			return 90.0;
		}
	}



}
