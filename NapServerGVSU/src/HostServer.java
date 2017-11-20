import java.net.*;
import java.util.*;
import java.io.*;

/***********************************************************************
 * 
 * Host Server
 * 
 * This class is a part of the host in this project, being initiated by 
 * it. This part of the host, keeps and open connection for other host 
 * to connect and request files. 
 * 
 * @author Javier Ramirez
 * @version November 20, 2017
 *
 **********************************************************************/
public class HostServer extends Thread {
	
	/** Used in handling incoming messages */
	private String recvMsg;
    
    /** The port number that accepts new FTP-connects */
	private static int welcomePort = 1235;
	
	/** The socket that listens for new FTP-connects */
	private static ServerSocket welcomeSocket;
	
	/** This keeps track of success and failure in connecting */
	private boolean successfullySetFTPPort = false;
	
	/*******************************************************************
	* Constructor initializes the socket that listens for new FTP
	* connections. It also flexibly sets up the port-number based on
	* a realization of which ones are available to be used. This allows 
	* us avoid issues with multiple FTP-hosts. It logs the connection 
	* having been setup.
	*
	* @exception EmptyStackException if no port was established to 
	* listen for new FTP-connections.
	*******************************************************************/
	public HostServer() {
	
		/* Show server starting status */
		System.out.println(" ");
		System.out.println("Client-As-FTP-Server initialized. Waiting" +
					" for connections...");
		for(int i = 0; i < 5; i++){
		
			/* Initialize the welcome socket */
			try {
				welcomeSocket = new ServerSocket(welcomePort);
				successfullySetFTPPort = true;
				
				/* For debugging */
				// System.out.println("  DEBUG: FTP-Welcome Port: " + welcomePort);  
				
				/* Stop the loop if found a good port */
				break;
			}
			catch (IOException ioEx) {
			
				/* 
				 Setting a port for another host to connect to.
				 This is setup to handle the case of multiple users on 
				 one computer. Without this loop and catching, the 
				 second user immediately breaks.
				*/
				welcomePort = welcomePort + 1;
				
				/* For debugging */
				// System.out.println("  DEBUG: Changing the " + 
				//                      "port number.");  
			}
		}
		
		/* If unable to setup a port for later FTP connections */
		if (successfullySetFTPPort == false) {
		
			/* Throw exception to the GUI */
			throw new EmptyStackException();
		} 
	}
	
	/*******************************************************************
	* This is the continuously running part of the class. It welcomes
	* new connections, then sets them up to their own thread to allow
	* for simultaneous other processes. 
	*
	* @exception Throws an error if there was trouble in setting up a
	* new thread for our new connection to be handled within.
	*******************************************************************/
	public void run(){   
	
		/* Perform a loop to wait for new connections */
		try {
			do {
			
				/* Wait for client... */
				Socket connectionSocket = welcomeSocket.accept();

                /* Display to terminal when new client connects */
				System.out.println("\nClient-As-FTP-Server: New Client Connected!");
				
				/* For debugging */
				// System.out.println("  DEBUG: New Connection's IP: " + 
				//     connectionSocket.getInetAddress());

				/* 
				 Create a thread to handle communication with this 
				 client and pass the constructor for this thread a 
				 reference to the relevant socket and user IP.  
				*/
				FTPClientHandler handler 
				               = new FTPClientHandler(connectionSocket);

				/* Start a new thread for this client */
				handler.start();
			} while (true);
		}
		catch (Exception e) {
			System.out.println("ERROR: Failure in setting up a " +
			                   "new thread.");
		}
	}
    
	/*******************************************************************
	 * Once the host-FTP-server is setup, we know it's welcomePort. THEN
	 * this needs to be passed to the Central-Server to allow others to 
	 * download files from this host. This function helps in this 
	 * process.
	 * @return String - the string of the port number that this host is
	 * listening on.
	 ******************************************************************/
	public String getFTPWelcomePort() {
		return Integer.toString(welcomePort);
	}
	
/* End of Entire HostServer Class */
}

/***********************************************************************
 * This class handles allows for threading and handling the various 
 * clients that are connected to server simultaneously.
 **********************************************************************/
class FTPClientHandler extends Thread {
    
	/** This sets the packet size to be sent across to this size */
	private static final int BUFSIZE = 32768;
	
	/** Port for the commands to be sent across */
	private static final int controlPort = 1078;
	
	/** This socket takes commands from client */
	private Socket controlListen;
	
	/** This socket takes data from client */
	private Socket dataSocket;
	
	/** This handles the stream from the command-line of client */
	private Scanner inScanFromClient;
	
	/** This is used for handling the buffering of files over /
	    the data stream */
	private int recvMsgSize;
	
	/** This is used to grab bytes over the data-line */
	private String recvMsg;
	
	/** This allows for identification of specific users in streams */
	private String remoteIP;
	
	/** Output Stream for Control */
	private OutputStream outToClient;
	
	/*******************************************************************
	*
	* Beginning of thread.
	* This constructor marks the beginning of a thread on the server.
	* Things here happen once, exclusively with THIS connected client.
	*
	******************************************************************/
	public FTPClientHandler(Socket controlListen) {
	    try {
		    /* Setting up a threaded input control-stream */
		    inScanFromClient = new Scanner (
		        controlListen.getInputStream());
		    /* Setting up a threaded output control-stream */
		    outToClient = controlListen.getOutputStream();
		    /* Get IP from the control socket for future connections */
		    remoteIP = controlListen.getInetAddress().getHostAddress();
		    /* For debugging */
		    // System.out.println("  DEBUG: A new thread was successfully setup.");
		    // System.out.println("");
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
	
		/* For sending and retrieving file */
		int BUFSIZE = 32768;
		byte[] byteBuffer = new byte[BUFSIZE];
	    
		Socket dataConnection;
		boolean stayAlive = true;

		while (stayAlive) {
			/* Try to get input from client */
			try {
				recvMsg = inScanFromClient.nextLine();
				
				/* For debugging */
				// System.out.println("RECEIVED MESSAGE: " + recvMsg);
			} catch (Exception e) {
				System.out.println("");
				
				/* For debugging */
				// System.out.println("NO INPUT FROM CLIENT");
				
				break;
			}
			
			/* Set up a tokenizer */
			StringTokenizer tokens = new StringTokenizer(recvMsg);
			String commandToken = tokens.nextToken();
			
			if(commandToken.toLowerCase().equals("retr")) {
				
				/* 
				 From the string read from the connection with the other
				 host...
				 (1) Grab the dataport to connect back to.
				 (2) Grab the filename to retrieve.
				*/
				String dataPort = tokens.nextToken();
				String fileName = tokens.nextToken();
				
				InputStream inFromClient_Data;
				OutputStream outToClient_Data;
				
				try {
				
					/* Data connection socket */
					dataConnection = new Socket(remoteIP,
					Integer.parseInt(dataPort));
							
					/* Initiate data Input/Output streams */
					inFromClient_Data = 
					    dataConnection.getInputStream();
					outToClient_Data = 
					    dataConnection.getOutputStream();
					System.out.println("Data line started.");
				
					/* Handle the sending of file over the line */
					File myFile = new File(fileName);
					if (myFile.exists()) {
						try {
							/* Declare variables for converting 
							    file to byte[] */
							FileInputStream fileInputStream = 
							    new FileInputStream(myFile);
							byte[] fileByteArray = 
							    new byte[(int) myFile.length()];
							// Grabs file to memory to then be passed
							fileInputStream.read(fileByteArray);
							fileInputStream.close();

							// Write to client over DATA line
							outToClient_Data.write(fileByteArray);
							outToClient_Data.flush();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						// Write error message to client over DATA line
						String errMsg = new String("File does " +
						    "not exist.");
					    
						try {
							outToClient.write(errMsg.getBytes());
							System.out.println("Writing data.");
						} catch (Exception e) {
							System.out.println("ERROR: Input/out " +
								    "stream failure.");
						}
					    
						try {
							outToClient.flush();
							System.out.println("Data sent.");
						} catch (Exception e) {
							System.out.println("ERROR: Input/out " +
								    "flush failure.");
						}
					}
					
					/* Close after the data is written to client */
					dataConnection.close();
				}
				catch (Exception j) {
				    System.out.println("  DEBUG: Catch-all error..");
				}
			}
			else if(commandToken.toLowerCase().equals("quit")) {
				try {
				    inScanFromClient = null;
				    outToClient = null;
				    remoteIP = "";
				    System.out.println("Disconnected!");
				} 
				catch (Exception e) {
				    System.out.println("  ERROR: Closing connection error");
				}
			}
			else {
				System.out.println("HOST SERVER: WRONG INPUT");
			}
			
		/* End of the other while loop */
		}
	/* End of threading run() */  
	}
/* End of the thread class */  
}
