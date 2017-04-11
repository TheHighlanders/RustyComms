package org.mayheminc.robot2017.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.mayheminc.robot2017.*;
import com.ctre.*;
import com.ctre.CANTalon.FeedbackDevice;

public class Shooter extends Subsystem {

	// TODO: change ARM_TALON to correct value.
	private CANTalon m_Wheel = new CANTalon(RobotMap.SHOOTER_TALON);

	// In Speed mode, outputValue is in position change / 10ms
//	final double MAX_SPEED_RPM = 320; 			 // empirically measured max speed
	final double INITIAL_TARGET_SPEED_RPM = 199.0 ; // speed control, week zero
	
	final double MAXIMUM_SHOOTING_ERROR = 5.0;  // was 20.0 at Pine Tree
	
	final double ENCODER_COUNTS	= 12.0; // single revolution
	final double TARGET_SPEED_100MS = INITIAL_TARGET_SPEED_RPM * 60 / 0.1; // 60 sec / min / 10 (100ms)
	final double TAGET_ENCODER_COUNTS_100MS = TARGET_SPEED_100MS / ENCODER_COUNTS;
	
	final double STOPPED_ENCODER_COUNTS_10MS = 0;
//	final double SPEED_TOLERANCE_PERCENT = 0.05;	

	private int count = 0;	
	final int CURRENT_LIMIT = 50; // RJD - changed to 125% of 40A breaker.  Breaker has to hold at 135%

	public Shooter()
	{
		SetWheelTalon();
		EnableCurrentLimit();
	}
	
	/**
	 * Enables the current limiting in the shooter wheel.
	 * TODO: need to redo this to measure current over time vs motor speed.
	 * We can't have too much current if we are not moving.
	 */
	public void EnableCurrentLimit() {
		m_Wheel.EnableCurrentLimit(true);
		m_Wheel.setCurrentLimit(CURRENT_LIMIT);
	}

	private void SetWheelTalon() {
		m_Wheel.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		m_Wheel.configNominalOutputVoltage(0.0,  0.0);
		m_Wheel.configPeakOutputVoltage(+12.0,  0.0);
		m_Wheel.enableBrakeMode(false);

		m_Wheel.setProfile(0);

		// set the control mode to speed to keep a constant speed.
		m_Wheel.changeControlMode(CANTalon.TalonControlMode.Speed);	
		m_Wheel.set(0);// Per manual: Caller should ensure set() is called immediately after to properly set the initial target set point

//		 m_Wheel.setPID(20.0, 0.0, 0.0, (1023.0/320.0), 0, 12.0, 0); // 'P' has +10 error with 10 balls; +15 and -5 error with all balls
//		 m_Wheel.setPID(30.0, 0.0, 0.0, (1023.0/320.0), 0, 12.0, 0); // 'P' has +10 and -5 error
//		m_Wheel.setPID(40.0, 0.0, 1.0, (1023.0/320.0), 0, 12.0, 0); // 'PD' has + 10 and -5 error
//		m_Wheel.setPID(30.0, 0.0, 0.0, (1023.0/320.0), 0, 36.0, 0);  //'P' has +5 and -3 error
		m_Wheel.setPID(60.0, 0.0, 0.0, (1023.0/320.0), 0, 48.0, 0);  //'P' has 
		
		 m_Wheel.enableControl();
	}
	public void initDefaultCommand() {} //Cannot delete this inherited abstract method

	/**
	 * Adjust the shooter RPM in 10ms by an amount.
	 * @param amount 
	 */	
	int m_manualAdjustment = 0;	
	public void adjustShooterSpeed(double amount) {
		m_manualAdjustment += amount;		
		DriverStation.reportError("Adjusting ShooterSpeed by " + amount, false);	
	}

	boolean m_isOn = false;
	
	public void turnOn() {
		m_isOn = true;	
		m_Wheel.enableControl();
	}

	public void turnOff() {
		m_isOn = false;
		m_Wheel.disableControl();
	}
	
	public double getError(){
		return m_Wheel.getClosedLoopError();
	}

	/**
	 * Determine if the Shooter Wheel is at the correct speed +/- SHOOTING_ERROR.
	 * @return true if in tolerance
	 */
	public boolean isUpToSpeed() {
		// get the error
		double error = getError();
		
		if(error > MAXIMUM_SHOOTING_ERROR) {
			return false;
		}
		if(error < -MAXIMUM_SHOOTING_ERROR ) {
			return false;
		}
		return true;
	}
	/**
	 * Determine if shooter is really close to speed.  This method is less tolerant of error than isUpToSpeed()
	 * @return true if in tolerance
	 */
	final int SHOOTER_TIGHTER_TOLERANCE = 8;
	
	public boolean isMoreUpToSpeed(){
		double error = m_Wheel.getClosedLoopError();
		return (SHOOTER_TIGHTER_TOLERANCE >= Math.abs(error));
	}
	
	/**called in Robot.teleopPeriodic() and Robot.autonomousPeriodic()
	 * sets a wheel speed that considers shooter state (on/off), camera target distancing, 
	 * and prior operator manual adjustment*/
	public void periodic() {
		if (m_isOn) {
			//set shooter speed
			double desired = convertCameraPixelToSpeed(Robot.getBoilerTop());
			desired += m_manualAdjustment;
			m_Wheel.set(desired);		
		} else {
			m_Wheel.set(STOPPED_ENCODER_COUNTS_10MS);	
		}
		
	}
	/**convert error to adjustment using empirical data points
	 * @return optimal speed based upon camera distancing, or a default if target not found
	 * */
	private double convertCameraPixelToSpeed(double pixelY){
		
		//temp for testing
		
		//return 191.5;
		
		if(pixelY > 999){
			//return TARGET_SPEED_100MS; //this constant is ~119k
			return 191.5;
		}
//	
////		OLD DATA:
////		/**Search WolframAlpha.com
////		 * use keyword "fit"
////		 * data points determined empirically 20170225---
////		 * (115, 184) (38, 175) (81, 178) (162, 204) 	 */
////		final double a = 0.00227211;
////		final double b = -0.223937;
////		final double c = 180.453;
		
//		OLD EQUATIONS AS USED AT WPI TOURNAMENT
//		final double a = -0.0000363358;
//		final double b = 0.0128145;
//		final double c = -1.2033;
//		final double d = 213.552;
//		return (a * Math.pow(pixelY, 3)) + (b * Math.pow(pixelY, 2)) + (c * pixelY) + d;
		
		// plotted two separate curves from data collected on 3/29:
		// Blue Cam:  y = .3891x + 142.37
		// Red Cam:  y = .3802x + 151.36
		// Generated an "average slope" equation for Blue (and Red) with same output for x = 100
		// Average slope equation for Blue:  y = .38465x + 142.815
		// Average slope equation for Red:  y = .38465x + 150.915
		// Using average slope equation for Blue!
		// Difference in above is 8.10 for "+ Constant".  Equates to a pixel offset of 21.05 (call it 21) for Red

		final double m = 0.38465;
		final double b = 142.815;
		
		return ((m * pixelY) + b);
	}

	public void updateSmartDashboard() {
		//cap error at +-20 so as to avoid annoying spikes in error on lineplot smartdashboard
		double error = getError();
		if (error > 20) {
			error = 20;
		}
		if (error < -20) {
			error = -20;
		}
		
		count++;    	
		SmartDashboard.putNumber("LoopCounter", count);    	
		SmartDashboard.putNumber("ShooterCounter", m_Wheel.getEncPosition());
		SmartDashboard.putNumber("ShooterCurrentSpeed", m_Wheel.get());
		SmartDashboard.putNumber("ShooterTargetSpeed",  m_Wheel.getSetpoint());    	
		SmartDashboard.putNumber("ShooterSpeedError",  error); 
		SmartDashboard.putNumber("ShooterSpeedRpm",  convertMotorNativeUnitsToRpm(m_Wheel.get()));
		SmartDashboard.putNumber("ShooterCurrent",  getShooterCurrentDraw());
		SmartDashboard.putBoolean("   Shooter Safe", getShooterCurrentDraw() <= SAFE_SHOOTER_CURRENT);
		SmartDashboard.putBoolean("   ReadyToShoot", isMoreUpToSpeed());
	}
	double convertMotorNativeUnitsToRpm(double ticks) {
		return ticks/(0.1*60.0/12.0*12.0/29.0);   // 12 counts per revolution, 12:29 gear ratio
	} 
	
	public final double SAFE_SHOOTER_CURRENT = 8.0;
	public double getShooterCurrentDraw(){
		return Robot.pdp.getCurrent(RobotMap.SHOOTER_PDP);
	}
}

