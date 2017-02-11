package org.usfirst.frc.team6201.robot.subsystems;

import org.usfirst.frc.team6201.robot.commands.gears.AutoPosRobotGearDeliveryCmd;
import org.usfirst.frc.team6201.robot.gearVision.GearVisionReceiverThread;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Subsystem used to start a thread for receiving UDP broadcasts of the current
 * target location from the jetson.
 *
 * @author David Matthews
 * @version Feb 4, 2017
 *
 */
public class GearVisionAuto extends Subsystem {

	private GearVisionReceiverThread gVRT;

	/**
	 * constructor: starts the thread.
	 * TODO: implement this as a singleton.
	 */
	public GearVisionAuto() {
		gVRT = new GearVisionReceiverThread();
		gVRT.start();
	}

	@Override
	protected void initDefaultCommand() {
		// TODO: make a command to notify the driver when we find a target, and
		// then start the gear delivery routine.
		// setDefaultCommand(new GearDeliveryCmd());
	}

	public void setTuning(double number) {
		AutoPosRobotGearDeliveryCmd.turnTuning = number;
		
	}
	
	public double getTuning (){
		return AutoPosRobotGearDeliveryCmd.turnTuning;
	}
}
