package org.usfirst.frc.team6201.robot.commands.gears;

import org.usfirst.frc.team6201.robot.Robot;
import org.usfirst.frc.team6201.robot.dataLogger.DataCollator;
import org.usfirst.frc.team6201.robot.gearVision.GearVisionCollator;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Drives the robot towards the peg for the remainder of the gear delivery
 * command. TODO: what does our robot's cameras see when it is at the peg? TODO:
 * what does our robot see when it is near the peg?
 * 
 * @author David Matthews
 * @author Adriana Massie
 */
public class CenterStationAutoPos extends Command {
	/**
	 * If we close to the peg, we may loose tracking of the target. This boolean
	 * is a flag so that when we loose tracking of the peg we know to drive
	 * forward if we are close.
	 */
	private boolean stopMe = false;

	public static double turnTuning = 0.75;

	/**
	 * An array of doubles with length of 4.
	 * 
	 * target [0] holds the x position of the center of the target. 0 is left of
	 * frame, 1 is right
	 * 
	 * target [1] holds the y position of the center of the target. 0 is top of
	 * frame, 1 is bottom
	 * 
	 * target [2] holds the width of the target. 0 is no width, 1 is full frame.
	 * 
	 * target [3] holds the height of the target. 0 is no height, 1 is full
	 * frame.
	 * 
	 * target is null if we do not currently have tracking of the target. The
	 * cameras we are using are Axis M1045LW cameras.
	 */
	private double[] target;

	/**
	 * The last known target.
	 */
	private double[] lastKnownTarget = {0.0, 0.0, 0.0, 0.0};

	/**
	 * Constructor for this command, requires use of the drive train.
	 */
	public CenterStationAutoPos() {
		requires(Robot.dt);
	}

	protected void initialize() {
	}

	/**
	 * Attempts to get an updated vision target and center the target, and drive
	 * towards it. if no target is available and we are close to the peg, then
	 * we drive straight forward, else we hunt for the target by turning in a
	 * circle
	 */
	protected void execute() {

		DataCollator.state.setVal("AutoPosRobotGearDeliveryCmdExe");

		target = GearVisionCollator.getTarget();

		// case: we have a recent vision target
		if (target != null) {

		

			lastKnownTarget = target;

			
			// if we are really close to the peg, stop!
			if (target[3] >= 0.15 && lastKnownTarget[3] >= 0.15) {
				DriverStation.reportWarning("StopMe is now true", true);
				stopMe = true;

			}
			
			/**
			 * Calculate desired forward speed based on the distance from the peg.
			 * If farther from the target, drive faster.
			 */
			double avgAvailablePower =( Math.pow(target[3] + 0.2, -0.3) - 1.1 )/1.3;
			
			/**
			 * Distance the target  is from the center of our frame.
			 */
			double targetXError = (target[0] - 0.50);
			
			/**
			 * How much are we going to be turning? a percentage of the available power, which is calculated by target distance.
			 */
			double turningPower;
			
			
			if (targetXError < 0) {
				turningPower = avgAvailablePower * -1 * turnPercent(targetXError);
			}
			else {
				turningPower = avgAvailablePower * turnPercent(targetXError); 
			}
			
			double motorPower = avgAvailablePower - Math.abs(turningPower);
			
			double leftPower = motorPower+turningPower;
			double rightPower = (motorPower - turningPower);
			
			DriverStation.reportWarning("motorPower: " + motorPower + "\tturningspeed: " + turningPower, false);
			Robot.dt.driveLR(leftPower, rightPower);
		}
		// case: have don't have a recent vision target.
		else {
			DriverStation.reportWarning("No Target being tracked: going off of last known target.", false);

			// if we loose tracking of the peg, but the robot is close, drive
			// forward.
			if (stopMe != true) {
				
				if (lastKnownTarget[0] > 0.5) {
					Robot.dt.driveLR(0.15, -0.2);
				}

				else {
					Robot.dt.driveLR(-0.2, 0.15);
				}

			}
		}
	}

	// use an ultrasonic sensor to determine if this command no longer needs to
	// run?
	// or maybe use accelormeter for hitting wall?
	protected boolean isFinished() {
		DriverStation.reportError("Stopping AutoPosRobot!!!!!\n\n", false);
		
		return ( lastKnownTarget[3] >= 0.15);
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.dt.driveLR(0, 0);
		DriverStation.reportWarning("Ending AutoPosRobotGearDelivery", false);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
	
	
	/**
	 * 
	 * @param xPos The X position of the target.
	 * @return The percentage of the available power that we want to use for turning.
	 */
	private double turnPercent(double xPos){
		
		return( Math.log10(Math.abs(xPos*50) + 2 )/ 10) / Math.log10(2);
	
		
		
	}

}
