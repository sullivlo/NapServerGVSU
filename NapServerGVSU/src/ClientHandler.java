import java.net.*;
import java.util.*;
import java.io.*;
public class ClientHandler extends Thread{
	
	/** This handles the stream from the command-line of client */
    private Scanner inFromClient;
    
    /** This allows for identification of specific users in streams */
    private String remoteIP;
    
    /** This socket takes commands from client */
    private Socket controlListen;
    
    /** This handles the output stream by command-line to client */
    private PrintWriter outToClient;
    
    /** This is used to grab bytes over the data-line */
    private String recvMsg;
    
	/*******************************************************************
    *
    * Beginning of thread.
    * This constructor marks the beginning of a thread on the server.
    * Things here happen once, exclusively with THIS connected client.
    *
    ******************************************************************/
   public ClientHandler(Socket controlListen) {
       try {
           /* Setting up a threaded input control-stream */
           inFromClient = new Scanner (
               controlListen.getInputStream());
           /* Setting up a threaded output control-stream */
           outToClient = 
               new PrintWriter(controlListen.getOutputStream());
           /* For error handling */
           /* Get IP from the control socket for future connections */
           remoteIP = controlListen.getInetAddress().getHostAddress();
           System.out.println("A new thread was successfully setup.");
           System.out.println("");
       } catch(IOException ioEx) {
           ioEx.printStackTrace();
           System.out.println("ERROR: Could not set up a " +
                       "threaded client input and output stream.");
       }
   }
   
   /******************************************************************
   *
   * Beginning of main thread code.
   * This method marks the threaded area that this client receives
   * commands and handles. When this receives "QUIT" from the client,
   * the thread closes.
   * 
   ******************************************************************/
    public void run(){   

    	/* Keeps tracks of when to close the thread */
        boolean stayAlive = true;
            
        /* The controlling loop that keeps the user alive */
        while (stayAlive) {
        	
        	 /* This reads the command from the client */
        	 try {
                 recvMsg = inFromClient.nextLine();
             } catch (Exception e) {
                 /* Client left early, or otherwise */
                 System.out.println("");
                 System.out.println("ERROR: Client left early");
                 break;
             }
             String returnedString = "";
             String currentToken;
             StringTokenizer tokens = new StringTokenizer(recvMsg);
            
             /* While checks for more user input in the token */
             while (tokens.hasMoreTokens()) {
            	 
                 /* Client opened dataport, "1079," for the server to
                    connect to */
            	 /* NOTE - This "clientDataPort" may be changed later. It was added to
            	  * have continuity with the FTP-server that we developed. Oct 25.
            	  */
                 String clientDataPort = tokens.nextToken();
                 
                 /* Client command, "LIST," or another. */
                 String commandFromClient = tokens.nextToken();
                 
                 /* This command closes this client-thread */
	             if(commandFromClient.equals("QUIT")){
	            	 System.out.println("Quitting from Client Handler");
	             	 
	            	 stayAlive = false;	
	            	 break;   
	             }
	             /* This commands initiates the keyword-search */
	             else if(commandFromClient.equals("KEYWORD")){
	            	 System.out.println("COMMAND: KEYWORD from user");
	             }
	         }
                
        /* End of the other while loop */
        }
    
    /* End of threading run() */  
    }
    
/* End of the thread class */  
}
	
