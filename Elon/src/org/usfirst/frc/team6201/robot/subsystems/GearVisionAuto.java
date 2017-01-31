package org.usfirst.frc.team6201.robot.subsystems;

import org.usfirst.frc.team6201.robot.gearVision.GearVisionReceiverThread;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class GearVisionAuto extends Subsystem {

	private GearVisionReceiverThread gVRT;
	
	public GearVisionAuto () {
		gVRT = new GearVisionReceiverThread();
		gVRT.start();
	}

	@Override
	protected void initDefaultCommand() {
	//TODO: make a command to notify the driver when we find a target, and then start the gear delivery routine. 
	//	setDefaultCommand(new GearDeliveryCmd());
	}
}

