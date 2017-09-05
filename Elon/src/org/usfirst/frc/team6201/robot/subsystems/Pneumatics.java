package org.usfirst.frc.team6201.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * 
 * Subsystem for controlling Pneumatics
 * 
 * @author Owen Chiu
 *
 */
public class Pneumatics extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	//For the marshmallow launcher there a 3 solenoids, one for each cannon.
	//For a normal cylinder use DoubleSolenoid instead, it will control motion in both directions.
	public Solenoid solenoid0;
	public Solenoid solenoid1;
	public Solenoid solenoid2;
	private Compressor c;
	public Pneumatics(){
		c = new Compressor(0);
		//disables closed loop control
		c.setClosedLoopControl(false);
		
		solenoid0 = new Solenoid(0);
		solenoid1 = new Solenoid(1);
		solenoid2 = new Solenoid(2);
		
	}


	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	
    }
}

