import java.net.*;
import java.util.*;
import java.io.*;

/*************************
 * 
 * Host Server
 * 
 * This class is a part of the host in this project, being initiated by it. 
 * This part of the host, keeps and open connection for other host to 
 * connect and request files. The request host client will know 
 * 
 * @author Javier Ramirez
 *
 *************************/
public class HostServer extends Thread {
	
	// private static final int serverPort = 1234;
	// private static ServerSocket serverSocket;
	
    private String recvMsg;
    
    /* Brendon Version Nov 16, 2017 */
	private static int welcomePort = 1235;
	private static ServerSocket welcomeSocket;
	
	private boolean successfullySetFTPPort = false;
	
	/* NEW Nov 16 */
	public HostServer() {
	    /* Show server starting status */
        System.out.println(" ");
        System.out.println("Client-As-FTP-Server initialized. Waiting " +
                                "for connections...");
        for(int i = 0; i < 5; i++){
            /* Initialize the welcome socket */
            try {
                welcomeSocket = new ServerSocket(welcomePort);
                successfullySetFTPPort = true;
                
                /* For debugging */
                System.out.println("  DEBUG-05: FTP-Welcome Port: " + welcomePort);  
                
                /* Stop the loop if found a good port */
                break;
            }
            catch (IOException ioEx) {
                /* 
                 Setting a port for another host to connect to.
                 This is setup to handle the case of multiple users on one 
                 computer. Without this loop and catching, the second user
                 immediately breaks.
                */
                welcomePort = welcomePort + 1;
                
                /* For debugging */
                System.out.println("  DEBUG-06: Changing the port number.");  
            }
        }
        
        /* If unable to setup a port for later FTP connections */
        if (successfullySetFTPPort == false) {
            /* Throw exception to the GUI */
            throw new EmptyStackException();
        } 
	}
	
	/* This is the "threaded" part of this class */
	public void run(){   
	
        /* Perform a loop to wait for new connections */
        try {
            do {
                /* Wait for client... */
                Socket connectionSocket = welcomeSocket.accept();

                System.out.println("\nClient-As-FTP-Server: New Client Connected!");
                
                /* For debugging */
                // System.out.println("  DEBUG: New Connection's IP: " + 
                //     connectionSocket.getInetAddress());

                /* 
                 Create a thread to handle communication with this client and 
                 pass the constructor for this thread a reference to the 
                 relevant socket and user IP.  
                */
                FTPClientHandler handler = new FTPClientHandler(connectionSocket);

                /* Start a new thread for this client */
                handler.start();
            
            } while (true);
        }
        catch (Exception e) {
            System.out.println("ERROR-01: Failure in setting up a thread.");
        }
    }
	
/* End of Entire HostServer Class */
}


/***********************************************************************
 *
 * This class handles allows for threading and handling the various 
 * clients that are connected to server simultaneously.
 *
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
    
    /** This handles the output stream by command-line to client */
    // private PrintWriter outToClient;
    
    /** This is used for handling the buffering of files over /
        the data stream */
    private int recvMsgSize;
    
    /** This is used to grab bytes over the data-line */
    private String recvMsg;
    
    /** This allows for identification of specific users in streams */
    private String remoteIP;
    
    /** Javier's Stream */
    private InputStream inFromClient;
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
        /* For sending and retrieving file */
        int BUFSIZE = 32768;
        byte[] byteBuffer = new byte[BUFSIZE];
    
        Socket dataConnection;
        boolean stayAlive = true;

        while (stayAlive) {
            
            //Try to get input from client
            try {
                recvMsg = inFromClient.toString();
            } catch (Exception e) {
                System.out.println("");
                System.out.println("NO INPUT FROM CLIENT");
                break;
            }
            
            StringTokenizer tokens = new StringTokenizer(recvMsg);
            String currentToken = tokens.nextToken();
            
            if(currentToken.toLowerCase().equals("retr")) {
            	String fileName = tokens.nextToken();
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
                        outToClient.write(fileByteArray);
                        outToClient.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
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
                        continue;
                    }
                    
                    try {
                        outToClient.flush();
                    } catch (Exception e) {
                        System.out.println("ERROR: Input/out " +
                                    "flush failure.");
                        continue;
                    }
                }
                try {
                    // Close after the data is written to client
                    controlListen.close();
                } catch (Exception e) {
                    System.out.println("ERROR: Closing data" +
                                    "-connection failure.");
                    continue;
                }
            	
            }
            else if(currentToken.toLowerCase().equals("quit")) {
            	//If the quit command is received, then the connection between the host client
            	//and this host server will close
            	try {
            	    controlListen.close();
            	    System.out.println("HOST SERVER: CONNECTION CLOSED");
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

