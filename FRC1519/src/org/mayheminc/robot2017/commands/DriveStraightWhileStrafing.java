package org.mayheminc.robot2017.commands;

import org.mayheminc.robot2017.Robot;
import org.mayheminc.robot2017.subsystems.Drive;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveStraightWhileStrafing extends Command {


	double m_targetDrivePower;
	double m_desiredDriveDisplacement;
//	public int m_startDrivePos = 0;
	public enum DriveDistanceUnits { ENCODER_TICKS, INCHES };
	boolean m_driveDone;

	double m_targetStrafePower;
	double m_desiredStrafeDisplacement;
	public int m_startStrafePos = 0;
	public enum StrafeDistanceUnits { ENCODER_TICKS, INCHES };
	boolean m_strafeDone;
	/**
	 * 
	 * @param arg_targetPower +/- motor power [-1.0, +1.0]
	 * @param arg_driveDistance Distance in encoder counts
	 */
	public DriveStraightWhileStrafing(double arg_targetDriveSpeed, DriveDistanceUnits driveUnits, 
			double arg_driveDistance, double arg_targetStrafeSpeed, StrafeDistanceUnits strafeUnits, 
			double arg_strafeDistance) {
		
		//requires(Robot.drive);
		
		if (driveUnits == DriveDistanceUnits.INCHES) {
			arg_driveDistance = arg_driveDistance / Drive.DISTANCE_PER_PULSE;
		}
		m_targetDrivePower = arg_targetDriveSpeed;
		m_desiredDriveDisplacement = Math.abs(arg_driveDistance);   	
		
		if (strafeUnits == StrafeDistanceUnits.INCHES) {
			arg_strafeDistance = arg_strafeDistance / Drive.DISTANCE_PER_PULSE_MIDDLE;
		}
		m_targetStrafePower = arg_targetStrafeSpeed;
		m_desiredStrafeDisplacement = Math.abs(arg_strafeDistance);   	
		
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		
		m_driveDone = false;
		m_strafeDone = false;
		
//		m_startDrivePos = Robot.drive.getRightEncoder();	
		Robot.drive.saveInitialWheelDistance();
		
		m_startStrafePos = Robot.drive.getMiddleEncoder();		
		
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
		
		int strafeDisplacement = Robot.drive.getMiddleEncoder() - m_startStrafePos;
		strafeDisplacement = Math.abs(strafeDisplacement);
		if(strafeDisplacement >= m_desiredStrafeDisplacement) m_strafeDone = true;
		
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