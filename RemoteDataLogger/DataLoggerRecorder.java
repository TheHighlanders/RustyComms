
/**
 *
 * This is a Data Logger Recorder. It receives data over UDP and records it to a file.
 * TODO: implement timeout
 * @author David Matthews
 * @version Dec 31, 2016
 *
 */

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataLoggerRecorder {

	/**
	 * Keep recording data until this is false
	 */
	private static boolean moreDataExists = true;
	
	/**
	 * If the current file needs a header message, set this to true.
	 * When this is false, we ignore all incoming header messages.
	 */
	private static boolean needNewLogFile = true;
	
	/**
	 * If true, we make a new file on receipt of next UDP packet.
	 */
	private static boolean needLogHeader = true;
	
	/**
	 * The File that we are writing into.
	 */
	private static File logFile = null;
	
	/**
	 * The FileWriter that we are using to write into our log file.
	 */
	private static FileWriter logFileWriter = null;
	
	/**
	 * The File to write messages to.
	 */
	private static File messagesFile = null;
	
	/**
	 * The FileWriter that we are using to log messages
	 */
	private static FileWriter messagesFileWriter = null;
	
	/**
	 * True if we should make a new messagesFile reception of next UDP message
	 */
	private static boolean needNewMessagesFile = true;
	
	/**
	 * The port over which we will be receiving messages.
	 */
	private static DatagramSocket inputSocket;
	
	/**
	 * A buffer to hold the byes of the UDP packets prior to parsing into a string.
	 */
	private static byte[] buffer;

	/**
	 * Main method, loops to wait for and handle each UDP packet.
	 * 
	 * @param args Not used
	 */
	public static void main(String[] args) throws IOException {

		init();

		try {

			// receive data and save
			while (moreDataExists) {
				receiveAndProcessData();
			}
			logFileWriter.close();
			inputSocket.close();
		} catch (IOException e) {
			System.out.println("We need new files.");
			logFileWriter = null;
			logFile = null;
			needNewLogFile = true;
			needLogHeader = true;
			
			messagesFile = null;
			messagesFileWriter = null;
			needNewMessagesFile = true;
			
		}
	}

	/**
	 * parses the data to determine what type of message it is.
	 * If the message is relevant, it is added to the logging file. 
	 * 
	 */
	private static void saveData(String data) {
		try {
			if (data.charAt(0) == 'm') {
				if (needNewMessagesFile){
					newMessageFile();
				}
			}
			if (needNewLogFile){
				newLogFile();
			}
			
			// is this the exit message?
			if (data.charAt(0) == 'e') {
				if (logFileWriter == null) {
					System.out.println("No FileWriter Open");
				} else {
					logFileWriter.append("e");
					System.out.println("Starting new file");
					needNewLogFile = true;
				}
			}

			// is this a header string?
			if (data.charAt(0) == 'h' && needLogHeader) {
				logFileWriter.append(data);
				needLogHeader = false;
			}
			// is this a data message?
			if (data.charAt(0) == 'd') {
				logFileWriter.append(data);
			}
			
			// is this a message that we should be logging?
			if (data.charAt(0) == 'm'){
				messagesFileWriter.append(data.substring(1));
				messagesFileWriter.append("\n");
				
			}
			logFileWriter.flush();
			messagesFileWriter.flush();
		} catch (IOException e) {
			System.out.println("Data Logger failed to open log file." + e.getStackTrace());
		}
	}

	/**
	 * creates a new log file with the current date and time and prepares to fill it with data
	 */
	private static void newLogFile() {
		File fNew = null;
		FileWriter fwNew = null;
		try {
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			fNew = new File(dateFormat.format("Log_"+(date) + ".csv"));
			fwNew = new FileWriter(fNew, true);
		} catch (IOException e) {
			System.out.println("Log file failed to be created.");
		}

		logFile = fNew;
		logFileWriter = fwNew;
		System.out.println("A new log file has been opened.");
		needNewLogFile = false;
		needLogHeader = true;
	}
	
	/**
	 * Creates a new message file with the current date and time and prepares to fill it with messages.
	 * 
	 */
	private static void newMessageFile () {
		File fNew = null;
		FileWriter fwNew = null;
		Date date = null;
		try {
			date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			fNew = new File(dateFormat.format("Messages_"+(date) + ".txt"));
			fwNew = new FileWriter(fNew, true);
			
		} catch (IOException e) {
			System.out.println("Messages file failed to be created.");
		}

		messagesFile = fNew;
		messagesFileWriter = fwNew;
		
		saveData("eBeginning logging of messages");
		System.out.println("A new messages file has been opened.");
		needNewMessagesFile = false;
		
	}

	/**
	 * Prepares to start listening on all UDP broadcasts on port 5800
	 */
	private static void init() {
		try {
			inputSocket = new DatagramSocket(5800);
		} catch (SocketException e) {
			System.out.println("DatagramSocket Failed to open");
		}

		buffer = new byte[256];
		newLogFile();
		newMessageFile();
	}

  	/**
  	 * waits up to 5 seconds for new data to arrive. 
  	 * On arrival, the data is stored in the buffer. If no data arrives within 5 seconds, a new file is triggered to be created.
  	 */
	private static void receiveAndProcessData() {
		try {
			DatagramPacket dataPacket = new DatagramPacket(buffer, buffer.length);
			inputSocket.setSoTimeout(5000);
			inputSocket.receive(dataPacket);

			// parse to string
			String dataString = new String(dataPacket.getData(), 0, dataPacket.getLength());

			saveData(dataString);
		} catch (IOException e) {
			System.out.println("Timeout Occured. " + e.getMessage());
			needNewLogFile = true;
		}
	}
}