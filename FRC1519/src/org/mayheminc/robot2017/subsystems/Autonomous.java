package org.mayheminc.robot2017.subsystems;

import org.mayheminc.robot2017.autonomousroutines.*;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Autonomous extends Subsystem {
	private static int delay = 0;
	private static final int MAX_DELAY = 9;
	
	private int programNumber = 0;
	private Command autonomousPrograms[] = {
					  new SelectAutoRoutine()
			        , new FortykPA() 
			        , new CrossBaseline()
			        , new HangGearLeft()
					, new HangGearCenter()
					, new HangGearRight()
					, new HangGearLeftAnd10kPA()
					, new HangGearCenterAnd10kPA()
					, new HangGearRightAnd10kPA()
					, new SlenderHangGearRightAnd10kPA()
					, new DeadGearLeftAnd10kPA()
					, new DeadGearCenterAnd10kPA()
					, new DeadGearRightAnd10kPA()
					, new HangGearRightAndGoDownfield()
			        , new StayStill()	
			        , new SelfTest() 
					, new TEST() //development auto routine
					
					
	};
	public Command getSelectedProgram(){
		return autonomousPrograms[programNumber];
	}
	public boolean isAutonomousMode(){
		return DriverStation.getInstance().isAutonomous();
	}
	
	public void adjustProgramNumber(int delta) {
		programNumber += delta;
		if (programNumber < 0) {
			programNumber = autonomousPrograms.length - 1;
		}
		else if (programNumber >= autonomousPrograms.length) {
			programNumber = 0;
		}
		updateSmartDashboard();
	}
	
	public boolean isRed() {
		return (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red);
	}
	public double multiplyByNegativeOneIfBlueAlliance(double arg){
		if(!isRed()){
			return arg * -1;
		}
		return arg;
	}
	
	public int getDelay() {
		return delay;
	}

	public void adjustDelay(int delta) {
		delay += delta;
		if (delay < 0) {
			delay = 0;
		} else if (delay > MAX_DELAY) {
			delay = MAX_DELAY;
		}
	}
	
	StringBuffer sb = new StringBuffer();
	public void updateSmartDashboard() {
		
		SmartDashboard.putBoolean("Auto Routine Selected", programNumber != 0);
		
		sb.setLength(0);
		sb.append(programNumber + " " + autonomousPrograms[programNumber].getName());
		sb.append("         ");
		SmartDashboard.putString("Auto Prog", sb.toString());
		
		SmartDashboard.putNumber("Autonomous Delay", delay);		
	}

	public String toString(){
		return autonomousPrograms[programNumber].getName();
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

