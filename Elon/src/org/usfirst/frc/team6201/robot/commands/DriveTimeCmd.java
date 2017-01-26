package org.usfirst.frc.team6201.robot.commands;

import org.usfirst.frc.team6201.robot.Robot;
import org.usfirst.frc.team6201.robot.dataLogger.DataCollator;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * 
 * Time that robot drives (correct me if I'm wrong)
 * 
 * @author Baxter Ellard
 *
 */

public class DriveTimeCmd extends Command {

	public double calibrated;
	
	private Timer timer = new Timer ();
	
	private double drivingTime;
	
	public DriveTimeCmd(double drivingTime) {
		
		this.drivingTime = drivingTime;
		requires(Robot.dt);
		
	}
	
	protected void initialize() {
	
		timer.start();
		
		timer.reset();
		DataCollator.state.setVal("DriveTimeCmdInit");
		
	}

	protected boolean isFinished() {

		return true;

	}
	
	public void execute() {
		
		DataCollator.state.setVal("DriveTimeCmdExecute");
	
		while(timer.get() < drivingTime) {
			
			calibrated = ((0.8 - 0.05 * Robot.dt.getGyroAngle()) * 0.05);
			System.out.println("driving");
			Robot.dt.driveLR(0.5, 0.5);
			
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
