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
    
    /** This is for thread control */
    private boolean endThread = false;
    
    /** This takes the user information from the host     */
    private String userInformation;
    private String UserName;
    private String UserHostName;
    private String UserSpeed;
    private String tmpFileName, tmpKeyWords;
    
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
           
           /* For debugging */
           // System.out.println("  DEBUG: A new thread was successfully setup.");
           // System.out.println("");
           
       } catch(IOException ioEx) {
           ioEx.printStackTrace();
           System.out.println("  ERROR: Could not set up a " +
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
            /* This grabs the string of data with filenames and keys */
        	userInformation = inFromClient.nextLine();
        	
        	/* Client is "connected", but did not read from own XML. */
        	if (userInformation.equals("XML-READ-ERROR")) {
                System.out.println("  ERROR-01: [" + remoteIP + "] had XML" +
                                   " troubles in connection!");
                endThread = true;
                throw new EmptyStackException();
        	}
        	
        	/* For Debugging. This shows what is read from control-line. */ 
        	// System.out.println("  DEBUG: Read-In: " + userInformation);
        	
        	/* Make the string parseable by tokens */         	 
        	userTokens = new StringTokenizer(userInformation);
        	
        	/*
        	 TODO - Check User's username for copies.
        	 Brendon - NOV 11, 2017
        	 We can error handle multiple users on the same IP (two people 
        	 from the same house) by checking THIS username against the 
        	 stored ones. On User-SUCCESS, of choosing a unique one, 
        	 continue as planned. On User-ERROR, of choosing a taken one,
        	 never show the main table of info to the user, and break the 
        	 current thread.
        	 A failure to the user will look as nothing happened, while
        	 success will look as a table generated.
        	*/
        	
        	/* Initialize and display the new user's shown username */
            UserName = userTokens.nextToken();
            UserName = UserName.replaceAll("@@", " ");
            System.out.println("Username: " + UserName);
                        
            /* 
             TODO - Change UserHostName
             Brendon - NOV 11, 2017
             The User's IP is grabbed directly from the stream by our
             methods. Right now, the variable remoteIP equals the user's 
             actual IP regardless of what the user typed in. I propose we 
             change UserHostName to be strictly the User's 
             typed-computer-name, to have our program paste the ACTUAL IP 
             to the end. Currently, a User could type in a false IP to 
             later have another user ping the wrong address.
            */

            /* Initialize and display the new user's hostname and IP */
            UserHostName = userTokens.nextToken();
            UserHostName = UserHostName.replaceAll("@@", " ");
            System.out.println("UserHostname: " + UserHostName);

            /* Initialize and display the new user's speed */
            UserSpeed = userTokens.nextToken();
            System.out.println("UserSpeed: " + UserSpeed); 

            /* Display new user's IP */
            System.out.println("IP Address: " + remoteIP); 

            /* Just for show */
            System.out.println(" ");
        	 
            /* Variables used in parsing the crazy string */
            String tmp_a_Token, tmpFilename, tmp_c_Token;
            /* First "Filename" */
            tmp_a_Token = userTokens.nextToken();
            /* First "Image.jpg" */
            tmpFileName = userTokens.nextToken();
            String tmpKeywordList = "";
            while(true) {
                /* If there are still tokens left */
                try {
                    /* Should mostly be keys */
                    tmp_c_Token = userTokens.nextToken();
                } catch (Exception e) {
                    /* The last submit, as if "Filename" found again */

                    /* TODO ADD: addValues () */

                    System.out.println("Submit: " + tmpFileName + tmpKeywordList);
                    break;
                }
                if(tmp_c_Token.equals("FILENAME")) {

                    /* TODO ADD: addValues () */

                    System.out.println("Submit: " + tmpFileName + tmpKeywordList);
                    tmpKeywordList = "";
                    /* Image.jpg */
                    tmpFileName = userTokens.nextToken();
                } 
                else {
                    tmpKeywordList = tmpKeywordList + " " + tmp_c_Token; 
                }
            }
             
        } catch (Exception e) {
            /* Host did not supply user information  */
            System.out.println("  ERROR-04: Host did not supply user information");
            endThread = true;
        }
        
        /* End the thread */
        if (endThread == true) {
            System.out.println("  ERROR-03: Ending user's thread");
            return;
        }
        /* For debugging */
        // System.out.println("  DEBUG-01: User's thread running");
        
        
        /* TODO - Add this? BRENDON, Nov 11. */
        //descriptions.add(UserSpeed, UserHostName, tmpFileName, tmpKeyWords);
        
        /* The controlling loop that keeps the user alive */
        while (stayAlive) {
        	
        	/* For debugging */
            // System.out.println("  DEBUG-02: User's thread running");
        	
            /* This reads the command from the client */
            try {
                recvMsg = inFromClient.nextLine();
                
            } catch (Exception e) {
                /* Client left early, or otherwise */
                System.out.println("Client " + UserName + " [" + remoteIP + "] left early!");
                break;
            }
            String returnedString = "";
            String currentToken;
            StringTokenizer tokens = new StringTokenizer(recvMsg);

            String clientDataPort = tokens.nextToken();

            /* Client command, "UPDATE" or another. */
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
                    System.out.println("Client " + UserName + " [" + remoteIP + "] disconnected!");
                    stayAlive = false;	
                    break;   
                }
                /* This commands initiates the keyword-search */
                else if(commandFromClient.equals("KEYWORD")){
                    System.out.println("Client " + UserName + " [" + remoteIP + "]: ");
                    System.out.println("COMMAND: KEYWORD");
                    /*Passes the last arguement into the key */
                    key = tokens.nextToken();
                    System.out.println("Key " + key);
                }
                /* This command updates the files the host have in their root directory */ 
                else if(commandFromClient.equals("UPDATE")){
                    System.out.println("Client " + UserName + " [" + remoteIP + "]: ");
                    System.out.println("COMMAND: UPDATE");

                    /* Passes the last arguement into the key */
                    fileName = tokens.nextToken();
                    System.out.println("Filename: " + fileName);

                    keyWords = tokens.nextToken();
                    System.out.println("keyWords " + keyWords);

                    /* TODO - Is this to current? BRENDON, NOV 11. */
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
	
