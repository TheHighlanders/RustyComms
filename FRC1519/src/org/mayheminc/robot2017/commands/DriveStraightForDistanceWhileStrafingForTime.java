package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;
import org.mayheminc.robot2017.commands.DriveStraightWhileStrafing.DriveDistanceUnits;
import org.mayheminc.robot2017.commands.DriveStraightWhileStrafing.StrafeDistanceUnits;
import org.mayheminc.robot2017.subsystems.Drive;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class DriveStraightForDistanceWhileStrafingForTime extends Command {


	double m_targetDrivePower;
	double m_desiredDriveDisplacement;
	public int m_startDrivePos = 0;
	public enum DriveDistanceUnits { ENCODER_TICKS, INCHES };
	boolean m_driveDone;

	double m_targetStrafePower;
	double m_desiredStrafeTime;
	double m_strafeStartTime;
	boolean m_strafeDone;
	/**
	 * 
	 * @param arg_targetPower +/- motor power [-1.0, +1.0]
	 * @param arg_driveDistance Distance in encoder counts
	 */
	public DriveStraightForDistanceWhileStrafingForTime(double arg_targetDriveSpeed, DriveDistanceUnits driveUnits, 
			double arg_driveDistance, double arg_targetStrafeSpeed, 
			double arg_strafeTime) {
		
		//requires(Robot.drive);
		
		if (driveUnits == DriveDistanceUnits.INCHES) {
			arg_driveDistance = arg_driveDistance / Drive.DISTANCE_PER_PULSE;
		}
		m_targetDrivePower = arg_targetDriveSpeed;
		m_desiredDriveDisplacement = Math.abs(arg_driveDistance);   	
		
		m_targetStrafePower = arg_targetStrafeSpeed;
		m_desiredStrafeTime = arg_strafeTime;		
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		
		m_driveDone = false;
		m_strafeDone = false;
		
//		m_startDrivePos = Robot.drive.getRightEncoder();
		Robot.drive.saveInitialWheelDistance();
		
		m_strafeStartTime = Timer.getFPGATimestamp();			
		
		System.out.println("Starting Routine: Drive Straight Forward While Strafing");

	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double strafePower;
		double drivePower;
		
		if(m_driveDone){
			drivePower = 0.0;
		}else{
			drivePower = m_targetDrivePower;
		}
		
		if(m_strafeDone){
			strafePower = 0.0;
		}else{
			strafePower = m_targetStrafePower;
		}
		
		Robot.drive.hDrive(drivePower, 0, strafePower, false, false);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
//		int driveDisplacement = Robot.drive.getRightEncoder() - m_startDrivePos;
		int driveDisplacement = (int)Robot.drive.getWheelDistance();
		
		driveDisplacement = Math.abs(driveDisplacement);
		if(driveDisplacement >= m_desiredDriveDisplacement) m_driveDone = true;
		
		double strafeDuration = Timer.getFPGATimestamp() - m_strafeStartTime;
		strafeDuration = Math.abs(strafeDuration);
		if(strafeDuration >= m_desiredStrafeTime) m_strafeDone = true;
		
		return (m_driveDone && m_strafeDone);
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.drive.stop();    	
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		Robot.drive.stop();

	}
}
