package org.usfirst.frc.team6201.robot.gearVision;


import org.usfirst.frc.team6201.robot.dataLogger.DataCollator;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 *
 * @author David Matthews
 * @version Jan 29, 2017
 *
 */
public class GearVisionCollator {

	private static volatile double [] target = new double[4];
	private static volatile boolean targetLocationKnown = false;
	
	public static double [] getTarget() {
		if (!targetLocationKnown) {
			return null;
		}
		return target;
		
	}
	
	public static boolean isTargetLocationKnown(){
		return targetLocationKnown;
	}
	
	public static void setTarget(double tX, double tY, double tW, double tH){
		target [0] = tX;
		SmartDashboard.putNumber("Target X", tX);
		DataCollator.targetX.setVal(tX);
		
		target [1] = tY;
		SmartDashboard.putNumber("Target Y", tY);
		DataCollator.targetY.setVal(tY);
		
		target [2] = tW;
		SmartDashboard.putNumber("Target W", tW);
		DataCollator.targetW.setVal(tW);
		
		target [3] = tH;
		SmartDashboard.putNumber("Target H", tH);
		DataCollator.targetH.setVal(tH);
		
		targetLocationKnown = true;
	}
	
	public static void setTargetLocationUnknown(){
		targetLocationKnown = false;
	}
	
}
