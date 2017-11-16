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
        
        /* Show server starting status */
        System.out.println(" ");
        System.out.println("Server initialized. Waiting for " +
                                "connections.");
        
        /* Initialize the welcome socket */
        try {
            welcomeSocket = new ServerSocket(welcomePort);
        }
        catch (IOException ioEx) {
            System.out.println(" ");
            System.out.println("  ERROR: Unable to set up port!");
            System.exit(1);
        }

        /* Perform a loop to wait for new connections */
        do {
            /* Wait for client... */
            Socket connectionSocket = welcomeSocket.accept();

            System.out.println("\nNew Client Connected to Server!");
            
            /* For debugging */
            // System.out.println("  DEBUG: New Connection's IP: " + 
            //     connectionSocket.getInetAddress());

            /* 
             Create a thread to handle communication with this client and 
             pass the constructor for this thread a reference to the 
             relevant socket and user IP.  
            */
            ClientHandler handler = new ClientHandler(connectionSocket);

            /* Start a new thread for this client */
            handler.start();
        
        } while (true);


    /* End of main() */
    }
/* End of public class CentralServer */
}
