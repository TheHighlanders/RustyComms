
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
	private static boolean needNewFile = true;
	
	/**
	 * If true, we make a new file on receipt of next UDP packet.
	 */
	private static boolean needHeader = true;
	
	/**
	 * The File that we are writing into.
	 */
	private static File f = null;
	
	/**
	 * The FileWriter that we are using to write into our log file.
	 */
	private static FileWriter fw = null;
	
	/**
	 * The port overwhich we will be recieving messages.
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
			fw.close();
			inputSocket.close();
		} catch (IOException e) {
			System.out.println("We need a new file.");
			fw = null;
			f = null;
			needNewFile = true;
			needHeader = true;
		}
	}

	/**
	 * parses the data to determine what type of message it is.
	 * If the message is relevent, it is added to the logging file. 
	 * 
	 * TODO: add support for logging raw String messages to a seprate file
	 */
	private static void saveData(FileWriter fw, String data) {
		try {
			// is this the exit message?
			if (data.charAt(0) == 'e') {
				if (fw == null) {
					System.out.println("No FileWriter Open");
				} else {
					fw.append("e");
					System.out.println("Starting new file");
					needNewFile = true;
				}
			}

			// is this a header string?
			if (data.charAt(0) == 'h' && needHeader) {
				fw.append(data);
				needHeader = false;
			}
			// is this a data message?
			if (data.charAt(0) == 'd') {
				fw.append(data);
			}
			fw.flush();
		} catch (IOException e) {
			System.out.println("Data Logger failed to open log file." + e.getStackTrace());
		}
	}

	/**
	 * creates a new file with the current date and time and prepairs to fill them with data.
	 */
	private static void newFile() {
		File fNew = null;
		FileWriter fwNew = null;
		try {
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			fNew = new File(dateFormat.format(date) + ".csv");
			fwNew = new FileWriter(fNew, true);
		} catch (IOException e) {
			System.out.println("File Failed to be created.");
		}

		f = fNew;
		fw = fwNew;
		System.out.println("A new file has been opened.");
		System.out.println(fw);
		System.out.println(f);
		needNewFile = false;
		needHeader = true;
	}

	/**
	 * Prepairs to start listening on all UDP broadcasts on port 5800
	 */
	private static void init() {
		try {
			inputSocket = new DatagramSocket(5800);
		} catch (SocketException e) {
			System.out.println("DatagramSocket Failed to open");
		}

		buffer = new byte[256];
		newFile();
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

			// if need new file, create one here.
			if (needNewFile) {
				newFile();
			}
			saveData(fw, dataString);
		} catch (IOException e) {
			System.out.println("Timeout Occured. " + e.getMessage());
			needNewFile = true;
		}
	}
}
