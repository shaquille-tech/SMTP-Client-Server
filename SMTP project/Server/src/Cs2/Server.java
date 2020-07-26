package Cs2;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
/** This class deals with the server and it also sets up a thread for communication and it
 * waits for a client to connect. It also creates a list of sockets for multiple clients to connect
 */
public class Server {
	
	public static void main(String[] args) {
	
    	int portNumber = 54000;
        try{
            //Setup the socket for communication 
            ServerSocket serverSoc = serverSoc = new ServerSocket(portNumber);
            ArrayList<SocketManager> clients = new ArrayList<SocketManager>();
            
            while (true){
                
                //accept incoming communication
                System.out.println("Server online waiting for client");
                Socket soc = serverSoc.accept();
                
                SocketManager temp = new SocketManager(soc);
                clients.add(temp);
                
                //create a new thread for the connection and start it.
                ServerConnectionHandler sch = new ServerConnectionHandler(clients, temp);
                Thread schThread = new Thread(sch);
                schThread.start();
            }
            
        }
        catch (Exception except){
            //Exception thrown (except) when something went wrong, pushing message to the console
            System.err.println("Error --> " + except.getMessage());
        }
    }   
}