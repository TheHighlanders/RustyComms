package org.usfirst.frc.team6201.robot.subsystems;

import org.usfirst.frc.team6201.robot.RobotMap;
import org.usfirst.frc.team6201.robot.commands.rgb.TeleOpRGB;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class RobotRGB extends Subsystem {

	private Spark red;
	private Spark green;
	private Spark blue;
	
	public RobotRGB() {
		
		red = new Spark(RobotMap.RED);
		green = new Spark(RobotMap.GREEN);
		blue = new Spark(RobotMap.BLUE);
		
	}
	
    public void initDefaultCommand() {
    	
    	
    	
    }

    public void setColour(double red, double green, double blue) {

    	if(red < 0 || green < 0 || blue < 0) {
    		
    		DriverStation.reportError("Unexpected input in RobotRGB Red: " + red + " Green: " + green + " Blue: " + blue, false);
    		
    	}
    	
    	this.red.set(Math.abs(red));
    	this.green.set(Math.abs(green));
    	this.blue.set(Math.abs(blue));
    	
    }

}

