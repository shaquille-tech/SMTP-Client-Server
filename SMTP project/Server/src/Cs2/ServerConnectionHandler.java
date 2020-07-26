package Cs2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ServerConnectionHandler implements Runnable {

	SocketManager selfs = null;
	ArrayList<SocketManager> clients = null;
	boolean verbose = false;
	State currentState = State.NONE;
	State previousState = State.NONE;
	String userInput;
	String data;
    String domainInput;
    String rcpttoInput;
	InputStorage store = new InputStorage();
	String UserName = "shaq";
	String Password = "kakashi";
	MailRetrieval retrievedMail = new MailRetrieval();

	public ServerConnectionHandler(ArrayList<SocketManager> l, SocketManager inSoc) {
		selfs = inSoc;
		clients = l;
	}

	public ServerConnectionHandler(ArrayList<SocketManager> l, SocketManager inSoc, boolean v) {
		selfs = inSoc;
		clients = l;
		verbose = v;
	}

	public void run() {
		try {
			// Catch the incoming data in a data stream, read a line and output it to the
			// console

			selfs.output.writeUTF("Client Connected");
			selfs.output.writeUTF("220 DBZ ready");
			currentState = State.CS;

			while (true) {
				// Print out message
				String message = selfs.input.readUTF();

				if (verbose) {
					System.out.println("--> " + message);
				}

				currentState = parse(message, selfs);

				for (SocketManager sm : clients) {
					sm.output.writeUTF(selfs.soc.getInetAddress().getHostAddress() + ":" + selfs.soc.getPort()
							+ " wrote: " + message);
				}

			}
			// close the stream once we are done with it
		} catch (Exception except) {
			// Exception thrown (except) when something went wrong, pushing message to the
			// console
			System.out.println("Error in ServerHandler--> " + except.getMessage());
		}
	}

	private State parse(String message, SocketManager sm) throws IOException {
		//Splits the users input and puts it into a string array
		String[] components = message.split(":");

		if (components.length > 0) {
			// The implementation of the quit command
			if (components[0].toUpperCase().equals("QUIT")) {
				previousState = currentState;
				// process further stuff to quit, notify all clients of quit message
				broadcast("CLIENTQUIT:" + sm.ip() + ":" + sm.port());
				// remove client from list:
				sm.close();
				clients.remove(sm);
				// update state
				return State.QUIT;

				// The implementation of the helo command
			} else if (components[0].toUpperCase().equals("HELO")) {
				userInput = components[1];
				sm.output.writeUTF("250 OK");
				
			 } else if (components[0].toUpperCase().equals("MAIL FROM")) {
	    	    	domainInput = components[1];
	    	    	sm.output.writeUTF("250 OK");
	    	    	
			 } else if (components[0].toUpperCase().equals("RCPT TO")) {
	    	    	rcpttoInput = components[1];
	    	    	sm.output.writeUTF("250 OK");
				
				// The implementation of the noop command
			} else if (components[0].toUpperCase().equals("NOOP")) {
				userInput = components[1];
				sm.output.writeUTF("250 OK");
				
            } else if (components[0].toUpperCase().equals("DATA")) {
    	    	
    	    	sm.output.writeUTF("354 send the mail data, end with (stop)");
    	    	boolean end = false;
    	    	
    	    	data = "";
    	    	// This loop allows user Input to loop until the user presses stop
    	    	while (end == false) {
    	    		
    	    		String userInput;
    	    		userInput = sm.input.readUTF();
    	    		
    	    		data += " "+ userInput;
    	    		
    	    		if (userInput.equals("stop")) {
    	    			end = true;
    	    		}
    	    	}
    	    	
    	    	store.storeInput(domainInput, rcpttoInput, data);
    	    	
    	    	sm.output.writeUTF("250 OK");

				// This implementation is for authentication
			} else if (components[0].toUpperCase().equals("USERN")) {
				userInput = components[1];
				
				if (userInput.equals("shaq")) {
					sm.output.writeUTF("250 OK: User name is correct");
					return State.PASSW;
					
				}

				else {
					sm.output.writeUTF("400: User name is incorrect");
					return State.USERN;

				}
				
				// This implementation is another part for the authentication
			} else if (components[0].toUpperCase().equals("PASSW")) {
				userInput = components[1];

				if (userInput.equals("kakashi")) {
					sm.output.writeUTF("250 OK: Password is correct");
					return State.MSG;
					
				} else {
					sm.output.writeUTF("400: Password is incorrect");
					return State.PASSW;
				}
				
			} else if (components[0].toUpperCase().equals("MSG")) {
				userInput = components[1];
				retrievedMail.viewMail(sm);
				return State.DELETE;
				
			} else if (components[0].toUpperCase().equals("DELETE")) {
				userInput = components[1];
				retrievedMail.deleteMail(sm);
				
			} else if (components[0].toUpperCase().equals("HELP")) {
				userInput = components[1];
				sm.output.writeUTF("HELP");
          
				sm.output.writeUTF("                            ");
                sm.output.writeUTF("USERN: Allows username input");
                sm.output.writeUTF("PASSW: Allows password input");
                sm.output.writeUTF("MSG: retrieves all messages");
                sm.output.writeUTF("DELETE: Deletes all mail on server");
                sm.output.writeUTF("RCPTDELETE: Deleted specific recipient mail, specified by user");
                sm.output.writeUTF("NOOP: contacts the server");
                sm.output.writeUTF("HELP: Outputs commands that can be used");
			}


		} else if (components[0].toUpperCase().equals("SETNAME")) {
			// Check that the message contained a name
			if (components.length > 1) {
				// set the name for the component
				selfs.setName(components[1]);
			}
			previousState = currentState;
			// set state to None
		} else if (components[0].toUpperCase().equals("SENDIP")) {
			boolean success = false;
			for (SocketManager sms : clients) {
				if (sms.ip().equals(components[1]) && sms.port().equals(components[2])) {
					success = true;
					sms.output.writeUTF(components[3]);
					sms.output.flush();
				}
			}
			sm.output.writeUTF("CLIENTSEND:" + components[1] + ":" + components[2] + ":" + Boolean.toString(success));
			sm.output.flush();

		} else if (components[0].toUpperCase().equals("SENDNAME")) {
			boolean success = false;
			for (SocketManager sms : clients) {
				if (sms.getName().equals(components[1])) {
					success = true;
					sms.output.writeUTF(components[2]);
					sms.output.flush();
				}
			}
			sm.output.writeUTF("CLIENTSEND:" + components[1] + ":" + Boolean.toString(success));
			sm.output.flush();
			// This implementation allows the client to send a message through broadcast
			// command
		} else if (components[0].toUpperCase().equals("BROADCAST")) {
			broadcast(components[1]);
			// Gets a list of all clients connected
		} else if (components[0].toUpperCase().equals("GETLIST")) {
			for (SocketManager sms : clients) {
				selfs.output.writeUTF("CLIENT:" + sms.ip() + ":" + sms.port() + ":" + sms.getName());
				selfs.output.flush();
			}
		}

		return State.NONE;

	}

	// This method allows the sever to send messages to all clients connected
	private void broadcast(String message) throws IOException {
		for (SocketManager sm : clients) {
			sm.output.writeUTF(message);
			sm.output.flush();
		}
	}
}
