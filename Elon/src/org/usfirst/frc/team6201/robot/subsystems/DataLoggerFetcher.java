package org.usfirst.frc.team6201.robot.subsystems;



import java.io.IOException;

import org.usfirst.frc.team6201.robot.Robot;
import org.usfirst.frc.team6201.robot.dataLogger.*;
import edu.wpi.first.wpilibj.ADXL362;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;

/**
 * A subsystem for the Data Logger to allow WPILibj to interface with the Data Logger and send a stop message, 
 * and to enable easy updating of the DataFields of the DataCollator class.
 *
 *@author David Matthews
 */
public class DataLoggerFetcher extends Subsystem {
	
	/**
	 * A flag to store if we should end logging and shutdown the jetson when we next enter disabled.
	 * Used when we are attached to the FMS.
	 */
	private boolean stopOnNextDisable = false;
	
	/**
	 * To get access to current and voltage for logging.
	 */
	private PowerDistributionPanel powerPanel;
	
	/**
	 * to get access to the accelerometer for logging.
	 */
//	current is not being foundprivate ADXL362 accel;
	
	/**
	 * Periodically broadcasts current data over UDP.
	 */
	private DataLoggerPublisherThread loggerPublisherThread;
	
	
	public DataLoggerFetcher() {
		powerPanel = new PowerDistributionPanel(0);
// see above		accel = new ADXL362(Accelerometer.Range.k16G);
		
		try {
			loggerPublisherThread = new DataLoggerPublisherThread();
			loggerPublisherThread.start();
		}
		catch (IOException e) {
			//TODO: Look up how to send this message to the Driverstation, should be one line.
			//TODO: ensure that if this exception is thrown, that we don't crash the robot via a Null pointer exception.
			DriverStation.reportError("DataLoggerPublisherThread().start(); crashed" + e.getStackTrace(), false);
		}
		

	}
	
	
	/**
	 * Instructs loggerPublisherThread to send a UDP broadcast to inform the Data Logger recorders to stop logging.
	 * 
	 */
	public void stopLoggingRecorder() {
		loggerPublisherThread.stopLoggingRecorder();
	}

	/**
	 * Allows the PDP current, and other sensors to update periodically. 
	 * TODO: think about putting this in a separate thread.
	 */
	public void initDefaultCommand() {
        setDefaultCommand(new DataLoggerScannerCmd());
    }

	/**
	 * setter method for stopOnNextDisable
	 */
	public void setStopOnNextDisable(boolean trigger){
		stopOnNextDisable = trigger;
	}
	public boolean getStopOnNextDisable(){
		return stopOnNextDisable;
		
	}
	
	// the following update the DataFields of the DataCollator.
	
	
	public void setCurrent0 (){
		DataCollator.current0.setVal(powerPanel.getCurrent(0));
	}
	public void setCurrent1() {
		DataCollator.current1.setVal(powerPanel.getCurrent(1));
	}
	public void setCurrent2() {
		DataCollator.current2.setVal(powerPanel.getCurrent(2));
	}
	public void setCurrent3() {
		DataCollator.current3.setVal(powerPanel.getCurrent(3));
	}
	public void setCurrent4 (){
		DataCollator.current4.setVal(powerPanel.getCurrent(4));
	}
	public void setCurrent13 (){
		DataCollator.current13.setVal(powerPanel.getCurrent(13));
	}
	public void setCurrent14() {
		DataCollator.current14.setVal(powerPanel.getCurrent(14));
	}
	public void setCurrent15() {
		DataCollator.current15.setVal(powerPanel.getCurrent(15));
	}
	public void setVoltage() {
		DataCollator.batteryVoltage.setVal(powerPanel.getVoltage());
	}
	public void setTemp() {
		DataCollator.pdpTemp.setVal(powerPanel.getTemperature());
	}
	public void setRate() {
		DataCollator.gyroRate.setVal(Robot.dt.getGyroRate());
	}
	public void setGyro() {
		DataCollator.gyroAngle.setVal(Robot.dt.getGyroAngle());
	}
//	public void setAccelX() {
//		DataCollator.accelX.setVal(accel.getX());
//	}
//	public void setAccelY() {
//		DataCollator.accelY.setVal(accel.getY());
//	}
//	public void setAccelZ() {
//		DataCollator.accelZ.setVal(accel.getZ());
//	}

}