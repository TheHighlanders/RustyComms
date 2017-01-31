package org.usfirst.frc.team6201.robot.subsystems;

import org.usfirst.frc.team6201.robot.RobotMap;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *TODO: Comment me
 */
public class RopeClimber extends Subsystem {
	
	private VictorSP m1 = new VictorSP(RobotMap.CLIMBER1);
	private VictorSP m2 = new VictorSP(RobotMap.CLIMBER2);
	

    public void initDefaultCommand() {
    }
    
    public void climb(){
    	m1.set(1);
    	m2.set(1);
    }
    
    public void fall (){
    	m1.set(-0.5);
    	m2.set(-0.5);
    	
    }
    
    public void stop (){
    	m1.set(0);
    	m2.set(0);
    }
}

