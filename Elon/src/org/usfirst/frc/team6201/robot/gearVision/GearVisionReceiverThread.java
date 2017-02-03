package org.usfirst.frc.team6201.robot.gearVision;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.usfirst.frc.team6201.robot.RobotMap;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 *
 * @author David Matthews
 * @version Jan 29, 2017
 *
 */
public class GearVisionReceiverThread extends Thread {
		
	private DatagramSocket inputSocket;
	private byte[] buffer;
	
	
	public void run() {
		init();
		while (true){
			processData(recieveData());
		}
		
	}
	
	private void processData(String s) {
		if (s == null){
			return;
		}
		double targetX = Double.parseDouble(s.substring(0, s.indexOf(',')));
		s = s.substring(s.indexOf(',') + 1);
		double targetY = Double.parseDouble(s.substring(0, s.indexOf(',')));
		s = s.substring(s.indexOf(',') + 1);
		double targetW = Double.parseDouble(s.substring(0, s.indexOf(',')));
		s = s.substring(s.indexOf(',') + 1);
		double targetH = Double.parseDouble(s);

		
		GearVisionCollator.setTarget(targetX, targetY, targetW, targetH);
	}

	private String recieveData() {
		try {
			DatagramPacket dataPacket = new DatagramPacket(buffer, buffer.length);
			inputSocket.setSoTimeout(300);
			inputSocket.receive(dataPacket);
			String returnMe = new String(dataPacket.getData(), 0, dataPacket.getLength());
			return returnMe;
		}
		catch (IOException e){
			//TODO: Log messages!
			GearVisionCollator.setTargetLocationUnknown();
			return null;
		}
	
	}

	/**
	 * TODO: update the port number to be a valid FRC port.
	 */
	private void init () {
		try {
			
			inputSocket = new DatagramSocket(RobotMap.VISION_UDP_PORT);
			DriverStation.reportWarning("Opened input Socket!", false);
		}
		catch (SocketException e){
			//TODO: Log Messages -- Implement m
			DriverStation.reportWarning("Failed to open DatagramSocket for Jetson Target Analysis", false);
		}
		buffer = new byte[256];
	}

}
