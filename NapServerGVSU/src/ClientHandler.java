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
    
    /** This takes the user information from the host     */
    private String userInformation;
    private String UserName;
    private String UserHostName;
    private  String UserSpeed;
    
    
    /** This contains objects of all the hosted files */
    /* THIS SHOULD BE GLOBAL TO THE ENTIRE SERVER */
    private ArrayList<FileContainer> AllFiles;
      
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
        /** Gets user information from the host      */
        StringTokenizer userTokens;
        try {
        	 userInformation = inFromClient.nextLine();
        	 System.out.println(userInformation);
        	         	 
        	 userTokens = new StringTokenizer(userInformation);
        	
        	 UserName = userTokens.nextToken();
        	 UserName = UserName.replaceAll("@@", " ");
        	 System.out.println("UserName " + UserName);
        	  
        	 UserHostName = userTokens.nextToken();
        	 UserHostName = UserHostName.replaceAll("@@", " ");
        	 System.out.println("UserHostName " + UserHostName);
        	  
        	 UserSpeed = userTokens.nextToken();
        	 System.out.println("UserSpeed " + UserSpeed); 
        	 
        	 
        }
             
        
        catch (Exception e) {
            /* Host did not supply user information  */
            System.out.println("");
            System.out.println("ERROR: Host did not supply user information");
        }
       // ArrayList<String[]> FileNamewKeys = new Arraylist();
        /**
         * Todo: Parse filenames, keys out of string 
         */
            
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
            
             String clientDataPort = tokens.nextToken();
             
             /* Client command, "LIST," or another. */
             String commandFromClient = tokens.nextToken();
             
             String key, fileName, keyWords; 
             
             /* While checks for more user input in the token */
             while (tokens.hasMoreTokens()) {
            	 
                 /* Client opened dataport, "1079," for the server to
                    connect to */
            	 /* NOTE - This "clientDataPort" may be changed later. It was added to
            	  * have continuity with the FTP-server that we developed. Oct 25.
            	  */
                 
                 
                 /* This command closes this client-thread */
	             if(commandFromClient.equals("QUIT")){
	            	 System.out.println("Quitting from Client Handler");
	             	 
	            	 stayAlive = false;	
	            	 break;   
	             }
	             /* This commands initiates the keyword-search */
	             else if(commandFromClient.equals("KEYWORD")){
	            	 
	            	 
	            	 System.out.println("COMMAND: KEYWORD from user");
	            	 /*Passes the last arguement into the key */
	            	 key = tokens.nextToken();
	            	 System.out.println("Key " + key);
	            	 
	             }
	             /* This command updates the files the host have in their root directory */ 
	             else if(commandFromClient.equals("UPDATE")){
	            	 
	            	 
	            	 System.out.println("COMMAND: Update from user");
	            	 /*Passes the last arguement into the key */
	            	 
	            	 fileName = tokens.nextToken();
	            	 
	            	 System.out.println("File Name: " + fileName);
	            	 
	            	 keyWords = tokens.nextToken(); 
	            	 
	            	 System.out.println("keyWords " + keyWords);
	            	 
	            	 /* Set up one "File" object to be updated on server */
	            	 FileContainer tempContainer = new FileContainer();
	            	 tempContainer.setFileName(fileName);
	            	 tempContainer.setHostIP("TEMP");
	            	 tempContainer.setSpeed("TEMP");
	            	 tempContainer.setKeyString("TEMP");
	            	 
	            	 /* Adds this object to the general list of hosted files */
	            	 AllFiles.add(tempContainer);
	            	 
	            	 
	             }
	             
	         }
                
        /* End of the other while loop */
        }
    
    /* End of threading run() */  
    }
    
/* End of the thread class */  
}
	
