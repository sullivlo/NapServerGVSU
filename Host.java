import java.net.*; 
import java.util.*;
import java.io.*; 
import java.awt.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class Host {
	
	/* To handle connecting to Central-Server */
	static Socket server;
	
	/* Central-Server's IP */
	private String serverHostname;
	/* Central-Server's Port */
	private String port;
	/* User's username */
	private String username;
	/* User's name of computer... and IP */
	private String hostname;
	/* User's submitted internet speed */
	private String speed;	
	
	/* Checks for the user being already connected */
    private boolean isConnected = false;

	@SuppressWarnings("resource")
    public static void main(String[] args) throws Exception {}
	
	/**
	 * This method, intiated by the GUI, handles connecting to the 
	 * Central-Server. 
	 *
	 * @param serverHostname
	 * @param port
	 * @param username
	 * @param hostname
	 */
	public void connectToServer(String serverHostname, String port, 
	                            String username, String hostname, 
	                            String speed) {
	                            
		/* Taking the parameters from the GUI */
		this.serverHostname = serverHostname;
		this.port = port;
		this.username = username;
		this.hostname = hostname;
		this.speed = speed;
	    
	    /* This socket handles communicating main commands to server */
	    Socket controlSocket = null;	    
	    /* This handles the control-line out stream */
	    PrintWriter outToServer_Control = null;
	    /* This handles the control-line input stream */
	    Scanner inFromServer_Control = null;
	    /* To catch if there was errors. Limits sending. */
	    boolean hadConnectErrors = false;
	    
	    /* Only DO connect if NOT already connected */
	    if (isConnected == false){
	    	
	    	/* Establish a TCP connection with the server */
		    try {
		        controlSocket = new Socket(serverHostname, 
		                             Integer.parseInt(port));
		        boolean controlSocketOpen = true;
		    } catch (Exception p) {
		        System.out.println("  ERROR: Did not find socket!");
		        hadConnectErrors = true;
		    }
		                        
		    /* Set-up the control-stream. Handle errors. */
		    try {
		        inFromServer_Control = 
		           new Scanner(controlSocket.getInputStream());
		        outToServer_Control = 
		           new PrintWriter(controlSocket.getOutputStream());
		        isConnected = true;
		        
		        /* For debugging */
		        // System.out.println("  DEBUG: Connection stream intiated!");
		        // System.out.println(" ");
		    }
		    catch (Exception e) {
		        System.out.println("  ERROR: Did not connect to " +
		            "server!");
		        hadConnectErrors = true;
		        isConnected = false;
		    } 
		    		     
		    /* 
		     The following prepares the string "totalDescription" to be 
		     sent over the wire. This string contains all the keywords and
		     filenames of the files included within the client's XML 
		     document that should be within their source folder.
		     
		     TODO: This CAN validate that each of those files exist in 
		     the directory before sending any list to the Central-Server.
		     There could be a case that the XML document has misspellings
		     and causes issue with retrieval of files by another host.
		     
		     NOTE: The following website was a great help to this function
		     of parsing from XML. 
		     https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
		    */
		    
		    /* BRENDON TEMP */
			String allFilenamesAndKeys = "";
		    
		    try {
		    	/* Prepare the document for XML extraction */
			    File fXmlFile = new File("HostedFiles.xml");
			    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			    Document doc = dBuilder.parse(fXmlFile);
			    
				//optional, but recommended
			    //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			    doc.getDocumentElement().normalize();
			   
			    NodeList nList = doc.getElementsByTagName("File");
			    
			    /* For each "object," or, each file-description iterate */
			    /* Each iteration would send a string to the central-server */
			    /* TO-DO: (1) Look over this. (2) Implement the server listening for these strings. */
			  
			    
                /* Loop through each "<File id='0001'>"... */ 
                for (int temp = 0; temp < nList.getLength(); temp++) {
					
			        Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					
		                /* The target element. Ex: "<File id='0001'>" */
						Element eElement = (Element) nNode;
		
						/* 
						 NOTE - If later we want to include the 
						 display-name of the file... 
						 
						 String DisplayName = 
						  eElement.getElementsByTagName("DisplayName").item(0);
						*/
						
						/* Grab the filename */
						String FileName = eElement.getElementsByTagName("FileName").item(0).getTextContent();
						FileName = FileName.replaceAll(" ", "");	
						String KeyWords = "";
						
						/* How many keys are there? */
						int tmpLength = eElement.getElementsByTagName("Key").getLength();
						
						/* For debugging */
						// System.out.println("  DEBUG: Filename: " + FileName);
					    // System.out.println("  DEBUG: Number of keys: " + tmpLength);
						
						/* Wrap all the keys to one string */
						String tempKey;
						String totalKeysForCurrentFile = "";
						for(int i = 0; i < tmpLength; i++) {
						    tempKey = eElement.getElementsByTagName("Key").item(i).getTextContent();
						    totalKeysForCurrentFile = totalKeysForCurrentFile + " " + tempKey;
						    /* For debugging */
						    // System.out.println("  DEBUG: tempKey: " + tempKey);
						}
						
						/* For debugging */
						// System.out.println("  DEBUG: File's Keys: " + totalKeysForCurrentFile);
                        // System.out.println(" ");
                        
                        /* Attach those keys to the whole sending-string */
                        allFilenamesAndKeys = allFilenamesAndKeys + "FILENAME " + FileName + " " + totalKeysForCurrentFile + " ";
					}
				}
				
		    } catch (Exception e) {
		        System.out.println("  ERROR: Error in reading XML " + 
		                           "document!");
		        System.out.println("  ERROR: Did not attempt connect!");
		        hadConnectErrors = true;
		    } 
		    
		    /* 
		     If there are no errors to this point and the client is 
		     connected to the Central-Server, then send data. 
		     */
		    if (isConnected == true && hadConnectErrors == false) {
		        /* 
		         Send userName, HostName, speed, and each filename and 
		         its associated keys to the Central-Server 
		         */
		        String newUserInformation = username + " " + hostname + " "
		                                    + speed + " " 
		                                    + allFilenamesAndKeys;
		                                    
		        /* For debugging. View the sent filenames and keys. */
		        System.out.print("  DEBUG: Sent String To Connect: " 
		                         + newUserInformation);
		                         
		        outToServer_Control.println(newUserInformation);
		        outToServer_Control.flush();
		    }
		    else {
		        /* 
		         Return to user that there was an error in connecting. 
		         This could be by the GUI. It could be several options of
		         issue: (1) an already taken username, (2) no XML document, 
		         (3) a missing file that was called by the XML document,
		         (4) no connection to available to the called IP of
		         Central-Server, (5) refused connection by the "CS" at
		         application-level.
		        */
		        
		        /* For debugging */
		        System.out.println("  DEBUG: Error in connecting...");
		    }
		    
		/* Ends of if(isConnected) */   
	    }
	    
	     
	    /* Below is fossil code. May or may not use. */
	    /* Note that the connectToServer() method does not need to loop */
	    

	    /* This loop to keep taking commands */
////	    while (!quit) {
////	        /* Menu sent to the user before every command */
////	        System.out.println("");
////	        System.out.println("Valid commands:"); 
////	        System.out.println("QUIT");
////	        System.out.println("KEYWORD");
////	        
////	        /* Take user command */
////	        userCommand = input.nextLine();
////	        
////	        String currentToken;
////	        /* Break the user command to tokens */
////	        StringTokenizer tokens = new StringTokenizer(userCommand);
////	        currentToken = tokens.nextToken();
////	        String Command = currentToken;
////	        userCommand = Command.toUpperCase();
////	        
////	        System.out.println(" ");
////	        
////	        /* Accidental No-Command */
////	        if (userCommand.equals("")){
////	            System.out.println("ERROR: No command entered.");
////	            continue;
////	        }
////	        
////	        /* Quit Command */
////	        else if (userCommand.equals("QUIT") && isConnected == true) {
////	            
////	            /* Tells the server that this client wants disconnect */
////	            String toSend = port + " " + "QUIT";
////	            outToServer_Control.println(toSend);
////	            outToServer_Control.flush();
////	            /* Tells the client to stop itself */
////	            quit = true;            
////	            
////	        /* Quit Command */
////	        } else if (userCommand.equals("QUIT") && isConnected == false) {
////	            /* Tells the client to stop itself */
////	            quit = true;
////	            
////	        /* Keyword Command */
////	        } else if (userCommand.contains("KEYWORD") 
////	                && isConnected == true) {  
////	        	
////	        	/*Pass the argument into the keyword */
////	        	String keyword;
////	            try {
////	            	keyword = tokens.nextToken();
////	            } catch (Exception e) {
////	               System.out.println("ERROR: Did not give arguement " +
////	                   "to STOR.");
////	               continue;
////	            }
////	            try {
////	                /* Send the request over the control line */
////	                String toSend = port + " " + "KEYWORD" + " " + 
////	                    keyword;
////	                outToServer_Control.println(toSend);
////	                outToServer_Control.flush();
////	            }catch (Exception e) {
////	            System.out.println("ERROR: Did not give " + 
////	                    "arguement to KEYWORD.");
////	                continue;
////	            }
////	        }
//    	
//        /* End of controlling while */
//	    }*/



	/* End of connectToServer() */
	}

/* End of FTP client */
}
