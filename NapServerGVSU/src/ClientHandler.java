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
    private  String tmpFileName, tmpKeyWords;
    
    
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
    	
    	
    	/**
    	 * TESTING
    	 */
    	
    	//String test1 = CentralServer.descriptions.remove("127.0.0.1");
    	//System.out.println(test1);
    	
    	/* Keeps tracks of when to close the thread */
        boolean stayAlive = true;
        /** Gets user information from the host      */
        StringTokenizer userTokens;
        try {
        	/* DEBUG: This shows precisely what is sent over the control-line */ 
        	userInformation = inFromClient.nextLine();
        	// System.out.println(userInformation);
        	         	 
        	 userTokens = new StringTokenizer(userInformation);
        	
        	 UserName = userTokens.nextToken();
        	 UserName = UserName.replaceAll("@@", " ");
        	 System.out.println("UserName " + UserName);
        	 /**
        	  * Todo: Call Object and find the usernames
        	  * 
        	  * 
        	  */
        	 //boolean tmpOnServer;
        	 //tmpOnServer =  Object.GetNameOnServer(UserName);
        	 //if (tmpOnServer)
        	 //end connection with client
        	  
        	 UserHostName = userTokens.nextToken();
        	 UserHostName = UserHostName.replaceAll("@@", " ");
        	 System.out.println("UserHostName " + UserHostName);
        	 
        	  
        	 UserSpeed = userTokens.nextToken();
        	 System.out.println("UserSpeed " + UserSpeed); 
        	 
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
        			 
        			 /* ADD: addValues () */
        			 
        			 System.out.println("Submit: " + tmpFileName + tmpKeywordList);
        			 break;
        		 }
        		 if(tmp_c_Token.equals("FileName")) {
        			 
        			 /* ADD: addValues () */
        			 
        			 System.out.println("Submit: " + tmpFileName + tmpKeywordList);
        			 tmpKeywordList = "";
        			 /* Image.jpg */
        			 tmpFileName = userTokens.nextToken();
        		 } 
        		 else {
        			 tmpKeywordList = tmpKeywordList + " " + tmp_c_Token; 
        		 }
        	 }
        	 
        	 /**
        	  * Louis add bananas
        	  */
        	 
        	 /*
        	 String tmpToken  ;
        	 tmpKeyWords = " ";
        	 boolean newFileDescription = true;
             while(true)
             {
             	try{
             		tmpToken = userTokens.nextToken();
             		
             	}catch (Exception e){
             		break;
             	}
             	
             	if (tmpToken.equals("FileName")){
             		
             		
             		tmpToken = userTokens.nextToken();
             		
             		tmpFileName = tmpToken;
             				
             		System.out.println("XFileName: " + tmpToken);
             	}
             	
             	System.out.println("FileName: " + tmpToken);
             	
             	while(!(tmpToken.equals("FileName"))){
             		
             		tmpToken = userTokens.nextToken();
             		
             		if (tmpToken.equals("FileName")) {
             			tmpToken = userTokens.nextToken();
             			System.out.println("XXXFileNameXXX: " + tmpToken);
             			break;
             		}
             		
             		tmpKeyWords = tmpKeyWords + " " + tmpToken;
             		//System.out.println("Keys: " + tmpToken);
             		
             	}
             	System.out.println("addValues: " + tmpKeyWords);
             	tmpKeyWords = " ";
             	
             	//addValues (String addThisToSpeeds, String addThisToHostname, String addThisToFilename, String addThisToKeywords)
             	//System.out.println("FileName boolean " + tmpFileName );
             	//newFileDescription = true;
             	
             	
             	
             	
             	
             	
             	
             
             }	 
             //System.out.println("Keys: " + tmpKeyWords);
              */
        	 
        	 
        	 
        	 
        }
             
        
        catch (Exception e) {
            /* Host did not supply user information  */
            System.out.println("");
            System.out.println("ERROR: Host did not supply user information");
        }
        
       // ArrayList<String[]> FileNamewKeys = new Arraylist();
        /**
         * Todo: Parse filenames, keys out of string 
         * 
         * FileName pexels-photo-218863.jpeg             
         * Image Keyboard Laptop Stock Image Mac jpeg          
         * FileName pexels-photo-225223.jpeg             
         * Image Cell Phone Breakfast Soda Stock Image jpeg          
         * FileName pexels-photo-225767.jpeg             
         * Image Laptop Keyboard Paper Pen jpeg         
         * FileName pexels-photo-4545	7.jpeg 
         * 
         * 
         */
        
	        
	        
        //descriptions.add(UserSpeed, UserHostName, tmpFileName, tmpKeyWords);
        
        
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
	
