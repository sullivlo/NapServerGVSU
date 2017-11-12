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
public class HostServer {
	
	private final int serverPort = 1234;
	private ServerSocket serverSocket;
	private InputStream inFromClient;
	private OutputStream outToClient;
    private String recvMsg;
	
	public HostServer() throws IOException {
		
		try {
            serverSocket = new ServerSocket(serverPort);
        }
        catch (IOException ioEx) {
            System.out.println("\nHOST SERVER ERROR: Unable to set up port!");
            System.exit(1);
        }

        do {
            //Wait for client...
            Socket connectionSocket = serverSocket.accept();

            //Print message for testing
            System.out.println("\nHOST SERVER: New Client Connected to Server.");
            System.out.println("IP: " + connectionSocket.getInetAddress());	
        
            // Setting up input stream
            inFromClient = connectionSocket.getInputStream();
            
            // Setting up output stream
            outToClient = connectionSocket.getOutputStream();
            
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
                    connectionSocket.close();
                } catch (Exception e) {
                    System.out.println("ERROR: Closing data" +
                                    "-connection failure.");
                    continue;
                }
            	
            }
            else if(currentToken.toLowerCase().equals("quit")) {
            	//If the quit command is received, then the connection between the host client
            	//and this host server will close
            	connectionSocket.close();
            	System.out.println("HOST SERVER: CONNECTION CLOSED");
            }
            else {
            	System.out.println("HOST SERVER: WRONG INPUT");
            }
            
            
        } while (true);
	}
}
