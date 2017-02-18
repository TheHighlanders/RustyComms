package org.usfirst.frc.team6201.robot.commands;

import org.usfirst.frc.team6201.robot.Robot;
import org.usfirst.frc.team6201.robot.dataLogger.DataCollator;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * 
 * Drives robot forwards for X seconds
 * 
 * @author Baxter Ellard
 *
 */

public class DriveTimeCmd extends Command {

	public double calibrated;
	
	private Timer timer = new Timer ();
	
	private double drivingTime;
	
	/**
	 * 
	 * Stores double of seconds you want to drive for.
	 * 
	 * @param drivingTime	Number of seconds you want to drive forward
	 */
	public DriveTimeCmd(double drivingTime) {
		
		this.drivingTime = drivingTime;
		requires(Robot.dt);
		
	}
	
	/**
	 * 
	 * Starts timer from 0.
	 * 
	 */
	protected void initialize() {
	
		timer.start();
		
		timer.reset();
		DataCollator.state.setVal("DriveTimeCmdInit");
		
	}

	/**
	 * Is true if we have finished driving.
	 */
	protected boolean isFinished() {

		return timer.get() >= drivingTime;

	}
	
	/**
	 * 
	 * TODO: Add math for encoders once encoders are installed on robot 
	 * TODO: Tune gyro params to aid in driving in a straight line
	 * 
	 */
	public void execute() {
		
		DataCollator.state.setVal("DriveTimeCmdExecute");
	
		if(isFinished()) {
			
			return;
			
		} else {
			
			Robot.dt.driveLR(0.15, 0.15);
			
		}
		
		
		
	}
	
	
	protected void end() {
		
		System.out.println("Done");
		Robot.dt.stop();
		
	}
	
	protected void interrupted() {
		
		this.end();
		
	}
	
}
