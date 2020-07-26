package Cs2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Random;

/**
 * This class makes a stream to send data to server. The buffered reader asks
 * for user input and a loop keeps asking for user input
 * 
 */

// This is responsible for writing messages
public class ClientWriter implements Runnable {
	Socket cwSocket = null;

	public ClientWriter(Socket outputSoc) {
		cwSocket = outputSoc;
	}

	public void run() {
		Random rand = new Random();
		int start = rand.nextInt(cwSocket.getLocalPort());
		try {
			// Create the outputStream to send data through
			DataOutputStream dataOut = new DataOutputStream(cwSocket.getOutputStream());

			System.out.println("Client writer running");

			// Reads a message to output stream and send through socket
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			boolean loop = true;

			while (loop == true) {
				String userInput = bufferedReader.readLine();
				dataOut.writeUTF(userInput);
			}
			// close the stream once we are done with it
			// dataOut.close();
		} catch (Exception except) {
			// Exception thrown (except) when something went wrong, pushing message to the
			// console
			System.out.println("Error in Writer--> " + except.getMessage());
		}
	}
}
