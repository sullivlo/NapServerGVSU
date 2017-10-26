import java.net.*;
import java.util.*;
import java.io.*;
import java.awt.*;


/**
 * 
 * @author		    Brendon Murthum
 *                  Javier Ramirez-Moyano
 *                  Matthew Schuck
 *                  Louis Zullivan
 *
 */

public class CentralServer {
	ArrayList<HostLog> HostedFiles = new ArrayList<HostLog>();
	
	private static final int welcomePort = 1234;
    private static ServerSocket welcomeSocket;
    
    public static void main(String[] args) throws IOException {
        System.out.println("\nServer initialized. Waiting for " +
                                "connections.");
        try {
            welcomeSocket = new ServerSocket(welcomePort);
        }
        catch (IOException ioEx) {
            System.out.println("\nERROR: Unable to set up port!");
            System.exit(1);
        }

        do {
            /* Wait for client... */
            Socket connectionSocket = welcomeSocket.accept();

            System.out.println("\nNew Client Connected to Server.");
            System.out.println("IP: " + 
                connectionSocket.getInetAddress());

            /* Create a thread to handle communication with  */
            /* this client and pass the constructor for this */
            /* thread a reference to the relevant socket...  */
            ClientHandler handler = new ClientHandler(connectionSocket);
            handler.start();
        
        } while (true);
    }
}
	


/* TO-DO: implement the server's ability to make "objects" that 
 * contain the information for each file availble. (1) the hostIP, (2) the filename
 * (3) the userSpeed and (4) the "description" that has keywords.
 */
	/*
	 * User has entered Filename, UserIP, UserSpeed, Description 
	 */
	
	/*public void addFile() 
	{
		HostLog tmp = new HostLog(
		Filename, UserIP, 
		UserSpeed, Description);
	
	}*/
	
	
		




	

