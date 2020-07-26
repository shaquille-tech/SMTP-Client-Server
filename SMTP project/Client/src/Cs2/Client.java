package Cs2;

import java.io.*;
import java.net.*;
import java.util.Random;

/**
 * This class manages and sets up the thread for communication for the client to
 * send data on a port we wish to connect on
 */
public class Client {

	// Main Method:- called when running the class file.
	public static void main(String[] args) {

		// PortNumber:- number of the port we wish to connect on.
		int portNumber = 54000;
		// ServerIP:- IP address of the server.
		String serverIP = "localhost";

		try {
			// Create a new socket for communication
			Socket soc = new Socket(serverIP, portNumber);

			// create new instance of the client writer thread, initialise it and start it
			// running
			ClientReader clientRead = new ClientReader(soc);
			Thread clientReadThread = new Thread(clientRead);
			clientReadThread.start();

			// create new instance of the client writer thread, initialise it and start it
			// running
			ClientWriter clientWrite = new ClientWriter(soc);
			Thread clientWriteThread = new Thread(clientWrite);
			clientWriteThread.start();

		} catch (Exception except) {
			// Exception thrown (except) when something went wrong, pushing message to the
			// console
			System.out.println("Error --> " + except.getMessage());
		}
	}
}
