package org.usfirst.frc.team6201.robot.gearVision;

/**
 * 
 *
 * @author David Matthews
 * @version Jan 29, 2017
 *
 */
public class GearVisionCollator {

	private static double [] target = new double[4];
	private static boolean targetLocationKnown = false;
	
	public static double [] getTarget() {
		if (!targetLocationKnown) {
			return null;
		}
		return target;
		
	}
	
	public static void setTarget(double tX, double tY, double tW, double tH){
		target [0] = tX;
		target [1] = tY;
		target [2] = tW;
		target [3] = tH;
		
		targetLocationKnown = true;
	}
	
	public static void setTargetLocationUnknown(){
		targetLocationKnown = false;
	}
	
}
