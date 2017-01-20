package org.usfirst.frc.team6201.robot.subsystems;



import java.io.IOException;

import org.usfirst.frc.team6201.robot.dataLogger.*;
import edu.wpi.first.wpilibj.ADXL362;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;

/**
 *@author David Matthews
 */
public class DataLoggerFetcher extends Subsystem {
	private PowerDistributionPanel powerPanel;
	private	ADXRS450_Gyro gyro;
	private ADXL362 accel;
	private DataLoggerPublisherThread loggerPublisherThread;
	public DataLoggerFetcher() {
		powerPanel = new PowerDistributionPanel(0);
		gyro = new ADXRS450_Gyro();
		accel = new ADXL362(Accelerometer.Range.k16G);
		
		try {
			loggerPublisherThread = new DataLoggerPublisherThread();
			loggerPublisherThread.start();
		}
		catch (IOException e) {
			System.out.println("DataLoggerPublisherThread().start(); crashed" + e.getStackTrace());
		}
		

	}
	
	

	public void stopLoggingRecorder() {
		loggerPublisherThread.stopLoggingRecorder();
	}

	
	public void initDefaultCommand() {
        setDefaultCommand(new DataLoggerScannerCmd());
    }

	public void setTotalCurrent() {
		DataCollator.totalCurrent.setVal(powerPanel.getTotalCurrent());		
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
		DataCollator.gyro.setVal(gyro.getRate());
	}
	public void setAccelX() {
		DataCollator.accelX.setVal(accel.getX());
	}
	public void setAccelY() {
		DataCollator.accelY.setVal(accel.getY());
	}
	public void setAccelZ() {
		DataCollator.accelZ.setVal(accel.getZ());
	}
}