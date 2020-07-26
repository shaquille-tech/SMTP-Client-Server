package Cs2;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

public class InputStorage {
	void storeInput(String domainInput, String rcpttoInput, String data) throws FileNotFoundException {
		
		Date currentDate = new Date();
		
		try(FileWriter filewriter = new FileWriter ("D:\\Documents\\Mail.txt", true);
				BufferedWriter BufferedWriter = new BufferedWriter(filewriter);
				PrintWriter out = new PrintWriter(BufferedWriter)) {
			
			out.println(currentDate.toString() + ": " + domainInput + ", " + rcpttoInput + ", " + data);	
		} catch (Exception except) {
			System.out.println("File Not Found");
		}
				
	}

}
