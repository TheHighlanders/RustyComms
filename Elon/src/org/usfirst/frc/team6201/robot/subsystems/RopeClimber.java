package org.usfirst.frc.team6201.robot.subsystems;

import org.usfirst.frc.team6201.robot.RobotMap;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * RopeClimber subsystem: Climbs using two motors, m1 and m2.
 * 
 * @author David Matthews
 * @author Max
 * 
 */
public class RopeClimber extends Subsystem {

	private VictorSP m1 = new VictorSP(RobotMap.CLIMBER1);
	private VictorSP m2 = new VictorSP(RobotMap.CLIMBER2);

	public void initDefaultCommand() {
	}

	/**
	 * Sets motors to full power
	 */
	public void climb() {
		m1.set(1);
		m2.set(1);
	}

	/**
	 * Unspools at half power
	 */
	public void fall() {
		m1.set(-0.5);
		m2.set(-0.5);

	}

	/**
	 *  Stops motors, generally when the robot has reached the touchpad
	 */
	public void stop() {
		m1.set(0);
		m2.set(0);
	}
}
