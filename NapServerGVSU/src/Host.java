import java.net.*; 
import java.util.*;
import java.io.*; 
import java.awt.*;

public class Host {
	
	static Socket server;
	
	private String serverHostname;
	private String port;
	private String username;
	private String hostname;
	
	/* For reading text from the user */
    private Scanner input = new Scanner(System.in);
    
    /* This socket handles communicating main commands to server */
    private Socket controlSocket = null;
    
    /* QUIT controls the loop that listens for new command */
    private boolean quit = false;
    
    /* Checks for the user being already connected */
    private boolean isConnected = false;
    
    /* This value handles the file transfer */
    private int recvMsgSize; 

    /* This value holds the single string of the command */
    private String userCommand;
    
    /* This handles the control-line out stream */
    private PrintWriter outToServer_Control = null;
    
    /* This handles the control-line in stream */
    private Scanner inFromServer_Control = null;
	
	@SuppressWarnings("resource")
    public static void main(String[] args) throws Exception {}
	
	/**
	 * This function is called from the GUI. It connects the host to the central server
	 * using the parameters that the user inputs through the graphic user interface
	 * 
	 * @param serverHostname
	 * @param port
	 * @param username
	 * @param hostname
	 */
	public void connectToServer(String serverHostname, String port, String username, String hostname) {
		
		/* Taking the parameters from the GUI */
		this.serverHostname = serverHostname;
		this.port = port;
		this.username = username;
		this.hostname = hostname;
	    
	    /* Establish a TCP connection with the server using the parameters
	     * obtained from the user through the User Interface
	     */
	    try {
	        controlSocket = new Socket(serverHostname, 
	                             Integer.parseInt(port));
	        
	        // What is the variable for ?? 
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
	    
	    /*
	     * If the connection has been set up correctly, then call the methods
	     * sendDescriptions() and controllingFunction(), in order to send the 
	     * descriptions over to the central server and to listen for user
	     * commands
	     */
	    if(isConnected) {
	    	sendDescriptions();
	    	controllingFunction();
	    }
	    
	/* End of connectToServer() */
	}
	
	/**
	 * This method sends the description of its own files to the central server. 
	 * These descriptions are obtained by scanning an XML file, which is unique 
	 * to each host and describes the files that it contains
	 */
	private void sendDescriptions() {
		
	}
	
	/**
	 * This method listens for commands and handles them when it receives them
	 */
	private void controllingFunction() {
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
	            String toSend = port + " " + "QUIT";
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
	        	
	        	/*Pass the argument into the keyword */
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
	                String toSend = port + " " + "KEYWORD" + " " + 
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
	}
/* End of FTP client */

}

