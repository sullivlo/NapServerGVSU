import java.net.*; 
import java.util.*;
import java.io.*; 
import java.awt.*;

public class Host {
	
	static Socket server;
	static String tmpServerIp = 	"127.0.0.1";
	static String tmpServerPort = 	"1234";
	
	@SuppressWarnings("resource")
    public static void main(String[] args) throws Exception {
		/* For reading text from the user */
	    Scanner input = new Scanner(System.in);
	    
	    /* This socket handles communicating main commands to server */
	    Socket controlSocket = null;
	    
	    /* QUIT controls the loop that listens for new command */
	    boolean quit = false;
	    
	    /* Checks for the user being already connected */
	    boolean isConnected = false;
	    
	    /* This value handles the file transfer */
	    int recvMsgSize; 
	
	    /* This value holds the single string of the command */
	    String userCommand;
	    
	    /* This handles the control-line out stream */
	    PrintWriter outToServer_Control = null;
	    
	    /* This handles the control-line in stream */
	    Scanner inFromServer_Control = null;
	   
	    
	    try {
	        controlSocket = new Socket(tmpServerIp, 
	                             Integer.parseInt(tmpServerPort));
	        boolean controlSocketOpen = true;
	    }catch(Exception p){
	        System.out.println("ERROR: Did not find socket!");
	        
	    }
	                        
	    // Set-up the control-stream,
	    // if there's an error, report the non-connection.
	    try {
	        inFromServer_Control = 
	           new Scanner(controlSocket.getInputStream());
	        outToServer_Control = 
	           new PrintWriter(controlSocket.getOutputStream());
	        isConnected = true;
	        System.out.println("Connected to server!");
	    }
	    catch (Exception e) {
	        System.out.println("ERROR: Did not connect to " +
	            "server!");
	        isConnected = false;
	    }
	    
	    /* This loop to keep taking commands */
	    while (!quit) {
	        /* Menu sent to the user before every command */
	        System.out.println("");
	        System.out.println("Valid commands:"); 
	        System.out.println("QUIT");
	        System.out.println("KEYWORD");
	        
	        /* Take user command */
	        userCommand = input.nextLine();
	        
	        String currentToken;
	        /* Break the user command to tokens */
	        StringTokenizer tokens = new StringTokenizer(userCommand);
	        currentToken = tokens.nextToken();
	        String Command = currentToken;
	        userCommand = Command.toUpperCase();
	        
	        System.out.println(" ");
	        
	        /* Accidental No-Command */
	        if (userCommand.equals("")){
	            System.out.println("ERROR: No command entered.");
	            continue;
	        }
	        
	        /* Quit Command */
	        else if (userCommand.equals("QUIT") && isConnected == true) {
	            
	            /* Tells the server that this client wants disconnect */
	            String toSend = tmpServerPort + " " + "QUIT";
	            outToServer_Control.println(toSend);
	            outToServer_Control.flush();
	            /* Tells the client to stop itself */
	            quit = true;            
	            
	        /* Quit Command */
	        } else if (userCommand.equals("QUIT") && isConnected == false) {
	            /* Tells the client to stop itself */
	            quit = true;
	            
	        /* Keyword Command */
	        } else if (userCommand.contains("KEYWORD") 
	                && isConnected == true) {  
	        	
	        	String keyword;
	            try {
	            	keyword = tokens.nextToken();
	            } catch (Exception e) {
	               System.out.println("ERROR: Did not give arguement " +
	                   "to STOR.");
	               continue;
	            }
	            try {
	                /* Send the request over the control line */
	                String toSend = tmpServerPort + " " + "KEYWORD" + " " + 
	                    keyword;
	                outToServer_Control.println(toSend);
	                outToServer_Control.flush();
	            }catch (Exception e) {
	            System.out.println("ERROR: Did not give " + 
	                    "arguement to KEYWORD.");
	                continue;
	            }
	        }
    	
        /* End of controlling while */
	    }

	/* End of Main() */
	}

/* End of FTP client */
}
