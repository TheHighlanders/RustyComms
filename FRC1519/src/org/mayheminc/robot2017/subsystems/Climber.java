package org.mayheminc.robot2017.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.*;

import org.mayheminc.robot2017.Robot;
import org.mayheminc.robot2017.RobotMap;

/**
 *
 */
public class Climber extends Subsystem {
	final int CURRENT_LIMIT = 30;

	CANTalon m_winch = new CANTalon(RobotMap.CLIMBER_TALON);

	final boolean AllowBackDriveRatchet = true;

	public Climber(){
//		EnableCurrentLimit();
	}	

	/**
	 * Set a software limit to the current into the motor.
	 * This is not what we really need.  What we really need is
	 * to measure current over time.  As long as the motor is 
	 * spinning, then the current is OK.  If the motor stalls,
	 * we need to stop.  Even a low current will fry the motor
	 * so we can't just put a limit of 30 Amps.
	 */
	public void EnableCurrentLimit() {
		m_winch.EnableCurrentLimit(true);
		m_winch.setCurrentLimit(CURRENT_LIMIT);
	}

	public void initDefaultCommand() {
	}

	public final double SAFE_CLIMBER_CURRENT = 8.0;
	public final int CURRENT_M_OF_N_LIMIT = 10;
	public double getClimberCurrentDraw(){
		return Robot.pdp.getCurrent(RobotMap.CLIMBER_PDP);
	}
	int m_currentUnsafeLoops = 0;
	public boolean isClimberCurrentDrawSafe(){
		if(getClimberCurrentDraw() > SAFE_CLIMBER_CURRENT){
			m_currentUnsafeLoops++;
		}else{
			m_currentUnsafeLoops = 0;
		}
		return m_currentUnsafeLoops < CURRENT_M_OF_N_LIMIT;
	}	
	

	public void periodic() {
		final double CLIMBER_DEAD_ZONE = 0.1;
		
		double power = 0.0;
		
		if (Robot.oi.getClimberFastButton()) {
			power = 1.0;
			Robot.gearFloorPickerUpper.setCarryState();
		} else if (Robot.oi.getClimberSlowButton()) {
			power = 0.5;
			Robot.gearFloorPickerUpper.setCarryState();
		} 
//		else {
//			power = Robot.oi.getClimberThrottle();
//		}
		

		SmartDashboard.putNumber("Climber Applied Power", power);
		if ( power > CLIMBER_DEAD_ZONE ) {
			m_winch.set(power);
			Robot.gearFloorPickerUpper.setCarryState();
		} else if ((power < -CLIMBER_DEAD_ZONE) && AllowBackDriveRatchet ) {
			m_winch.set(power);
			Robot.gearFloorPickerUpper.setCarryState();
		} else {
			m_winch.set(0);
		}
	}

	public void updateSmartDashboard(){
		SmartDashboard.putNumber("ClimberCurrentDraw", getClimberCurrentDraw());
		SmartDashboard.putBoolean("   Climber Safe", isClimberCurrentDrawSafe());
	}
}

