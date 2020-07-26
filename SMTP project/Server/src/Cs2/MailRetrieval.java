
package Cs2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MailRetrieval {
	
	void viewMail (SocketManager sm) throws IOException {
		
		sm.output.writeUTF("Mail box\n");
		File mail = new File("D:\\Documents\\Mail.txt");
		
		try {
			BufferedReader getM = new BufferedReader(new FileReader(mail));
			String Mail = getM.readLine();
			
			while(Mail != null) {
				
				sm.output.writeUTF(Mail);
				Mail = getM.readLine();
			}
		}
		catch(FileNotFoundException e) {
			sm.output.writeUTF("Couldn't find file");
			System.exit(0);
		}
		catch(IOException e) {
			sm.output.writeUTF("An I/O error occured");
			System.exit(0);
		}
	}
	
	void deleteMail (SocketManager sm) throws IOException {
		File mail = new File("D:\\Documents\\Mail.txt");
		
		if(mail.delete()) {
			sm.output.writeUTF(mail.getName()+"All Messages on Server Deleted");
		}
		else {
			sm.output.writeUTF("400: Operation has failed");
		}
	}
}


