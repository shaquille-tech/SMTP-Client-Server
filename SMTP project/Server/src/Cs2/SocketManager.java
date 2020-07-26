package Cs2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketManager {

	public Socket soc = null;
	public DataInputStream input = null;
	public DataOutputStream output = null;
	String name = null;

	public SocketManager(Socket socket) throws IOException {
		soc = socket;
		input = new DataInputStream(soc.getInputStream());
		output = new DataOutputStream(soc.getOutputStream());
		name = soc.getLocalAddress().getHostName();
	}

	synchronized public DataInputStream getInput() {
		return input;
	}

	synchronized public DataOutputStream getOutput() {
		return output;
	}

	public void close() {
		try {
			input.close();
			output.close();
			soc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// This method contains all the possible error codes
	public String ErrorCodes(int codes) {
		switch (codes) {

		case 200:
			return "nonstandard success response, see rfc876";
		case 211:
			return "System status, or system help reply";
		case 214:
			return "Help message";
		case 220:
			return "<domain> Service ready";
		case 221:
			return "<domain> Service closing transmission channel";
		case 250:
			return "Requested mail action okay, completed";
		case 251:
			return "User not local; will forward to <forward-path>";
		case 252:
			return "Cannot VRFY user, but will accept message and attempt delivery";
		case 354:
			return "Start mail input; end with <CRLF>.<CRLF>";
		case 421:
			return "<domain> Service not available, closing transmission channel";
		case 450:
			return "Requested mail action not taken: mailbox unavailable";
		case 451:
			return "Requested action aborted: local error in processing";
		case 452:
			return "Requested action not taken: insufficient system storage";
		case 500:
			return "Syntax error, command unrecognised";
		default:
			return "somethings gone wrong on your device";
		}
	}

	synchronized public void setName(String val) {
		name = val;
	}

	synchronized public String getName() {
		return name;
	}

	synchronized public String ip() {
		return soc.getInetAddress().getHostAddress();
	}

	synchronized public String port() {
		return Integer.toString(soc.getPort());
	}

}
