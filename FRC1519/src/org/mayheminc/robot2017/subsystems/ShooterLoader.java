package org.mayheminc.robot2017.subsystems;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.mayheminc.robot2017.Robot;
import org.mayheminc.robot2017.RobotMap;

import com.ctre.CANTalon;

public class ShooterLoader extends Subsystem {
	final double FEED_BALL_SPEED=1.0;
	final double FEED_BALL_OFF_SPEED=0.0;
	final double RELEASE_HOPPER_DEPLOYERS_SPEED = -0.25;
	
	final double THUMPER_ON_SPEED = 1.0;
	final double THUMPER_OFF_SPEED = 0.0;

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	CANTalon m_Wheel = new CANTalon(RobotMap.SHOOTER_LOADER_TALON);
	CANTalon m_thumper = new CANTalon(RobotMap.THUMPER_TALON);

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		//setDefaultCommand(new MySpecialCommand());
	}

	public void periodic(){
		double activeFloorPower = Robot.oi.getDriverStickY();
		//m_thumper.set(activeFloorPower);
	}

	public void turnOn()
	{
		m_Wheel.set(FEED_BALL_SPEED);
		turnThumperOn();
	}
	
	public void turnThumperOn(){
		m_thumper.set(THUMPER_ON_SPEED);
	}
	
	public void reverseThumper() {
		m_thumper.set(-THUMPER_ON_SPEED);
	}

	/**
	 * Turn off the Shooter Wheel.
	 */
	public void turnOff()
	{
		m_Wheel.set(FEED_BALL_OFF_SPEED);
		turnThumperOff();
	}
	
	public void turnThumperOff(){
		m_thumper.set(THUMPER_OFF_SPEED);
	}
	
	//used to release ball hopper deployers
	public void releaseHopperDeployers(){
		m_Wheel.set(RELEASE_HOPPER_DEPLOYERS_SPEED);
	}
}