package org.usfirst.frc.team6201.robot.gearVision;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.usfirst.frc.team6201.robot.RobotMap;

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
		double targetH = Double.parseDouble(s.substring(0, s.indexOf(',')));
		
		GearVisionCollator.setTarget(targetX, targetY, targetW, targetH);
	}

	private String recieveData() {
		try {
			DatagramPacket dataPacket = new DatagramPacket(buffer, buffer.length);
			inputSocket.setSoTimeout(300);
			inputSocket.receive(dataPacket);
			return new String(dataPacket.getData(), 0, dataPacket.getLength());
		}
		catch (IOException e){
			//TODO: Log messages!
			System.out.println("Target location unknown");
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
		}
		catch (SocketException e){
			//TODO: Log Messages -- Implement m
			System.out.println("Failed to open DatagramSocket for Jetson Target Analysis");
		}
		buffer = new byte[256];
	}

}
