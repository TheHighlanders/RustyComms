package org.mayheminc.robot2017.subsystems;

import org.mayheminc.robot2017.RobotMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class GearManipulator extends Subsystem {

//	CANTalon m_leftGear = new CANTalon(RobotMap.GEAR_LEFT_TALON);
//	CANTalon m_rightGear = new CANTalon(RobotMap.GEAR_RIGHT_TALON);
	
	CANTalon m_leftGear;
	CANTalon m_rightGear; 
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    } 
    public void PushGearOut()
    {
    	m_leftGear.set(0.55);
    	m_rightGear.set(0.55);
    }
    public void Stop()
    {
    	m_leftGear.set(0.0);
    	m_rightGear.set(0.0);
    
    }
    public void CloseGearDoor()
    {
    	m_leftGear.set(-0.25);
    	m_rightGear.set(-0.25);
    	
    }
}

