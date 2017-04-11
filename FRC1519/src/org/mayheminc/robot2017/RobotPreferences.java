package org.mayheminc.robot2017;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author Team1519
 */
public class RobotPreferences {
    
    // PIDF Settings for Drive Wheel control loops in TalonSRX	
    static public void putWheelP(double p) {
        Preferences.getInstance().putDouble("WheelP", p);
    }
    static public void putWheelI(double i) {
        Preferences.getInstance().putDouble("WheelI", i);
    }
    static public void putWheelD(double d) {
        Preferences.getInstance().putDouble("WheelD", d);
    }
    static public void putWheelF(double f) {
        Preferences.getInstance().putDouble("WheelF", f);
    }    
    static public double getWheelP() {
        return Preferences.getInstance().getDouble("WheelP", 1.6);
    }
    static public double getWheelI() {
        return Preferences.getInstance().getDouble("WheelI", 0.0);
    }
    static public double getWheelD() {
        return Preferences.getInstance().getDouble("WheelD", 0.0);
    }
    static public double getWheelF() {
        return Preferences.getInstance().getDouble("WheelF", 1.8);
    }
    
    //PIDF Settings for Shooter Roller:    
    static public void putShooterP(double p) {
    	Preferences.getInstance().putDouble("ShooterP", p);
    }
    static public void putShooterI(double i) {
    	Preferences.getInstance().putDouble("ShooterI", i);
    }
    static public void putShooterD(double d) {
    	Preferences.getInstance().putDouble("ShooterD", d);
    }
    static public void putShooterF(double f) {
    	Preferences.getInstance().putDouble("ShooterF", f);
    }
    static public double getShooterP() {
    	return Preferences.getInstance().getDouble("ShooterP", 4.8);    
    }
    static public double getShooterI() {
    	return Preferences.getInstance().getDouble("ShooterI", 0.0);
    }
    static public double getShooterD() {
    	return Preferences.getInstance().getDouble("ShooterD", 240.0);
    }
    static public double getShooterF() {
    	return Preferences.getInstance().getDouble("ShooterF", 0.0);
    }
    
    
//    static public void putLeftIRValue(double val) {
//        Preferences.getInstance().putDouble("LeftIRValue", val);
//    }
//    static public void putRightIRValue(double val) {
//        Preferences.getInstance().putDouble("RightIRValue", val);
//    }   
//    static public double getLeftIRValue() {
//        return Preferences.getInstance().getDouble("LeftIRValue", 2.0);   
//    }
//    static public double getRightIRValue() {
//        return Preferences.getInstance().getDouble("RightIRValue", 2.0);
//    }
//    static public double getIRThreshold(int IRChannel){
//    	switch(IRChannel){
//    	case 1: 
//    		return getLeftIRValue();
//    	case 2: 
//			return getRightIRValue();
//    	}
//    	return 2.0;
//    }
}