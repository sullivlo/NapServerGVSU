# NapServerGVSU

## Abstract

This system implements a "Central Server" that connects multiple peers to share files among one another. It serves to list the available files to the connected users as well as to handle the file transfer with the peer that has the desired file.

## Authors

- Louis Sullivan
- Javier Ramirez-Moyano
- Matthew Schuck
- Brendon Murthum

## Assignment Information

*This information directly from the intial assignment document.*

### Introduction

In this project, your file transfer protocol will form the basis of a file sharing system that allows users to access a distributed data storage system based on simple keyword search. The specification of this project relies on the
principles of using the centralized directory indexing service to implement a P2P architecture. The system
consists of two parts:
- The first part is the host system, which can query the server for files using keywords. The host also has
a file transfer client and server. The ftp client allows a user to access files stored at the remote user
locations. The ftp server is responsible for providing file transfer services requested by a remote client.
- The second part of the system is the centralized server, which provides a search facility that can be
used to perform simple keyword searches. The result of the search is the location of the remote
resource.

### Host-Implementation

The host is a GUI application. The user must specify the hostname and port of the server in order to connect
to it. Once a connection is initiated with the server, the host sends the three pieces of information (username,
hostname, and connection speed). After that, the host uploads the file descriptions (for example a file called
“filelist.xml” in the current working directory). These descriptions include the file name and the description of
each shared file. The shared files are also located in the current working directory. The descriptions for these
files are used for the keyword searches. After user and file information are successfully registered, the client is
now able to do keyword searches. Once the search is executed, the remote host and file name of all matching
files of the keyword are given back by the server.
After getting the results, the client can contact the remote ftp server to retrieve the file. The ftp client can only
be used to “get” a file and not “put” a file. When the client wants to disconnect from the NAP server, client can
click “Disconnect”, which sends out a QUIT command to the server.

### Centralized-Server-Implementation

The server tracks current users and the files shared from their site. Once a connection is initiated from a host,
it stores the username, connection speed, and hostname in a “users” table. After this, it acknowledges to the
host that it has received this information and then waits for the shared file descriptions to be uploaded. Once it
receives the file descriptions, it’ll be stored in a “files” table.
It also provides simple keyword search and returns the description of the remote resource. On receipt of a
keyword search, the server performs the search in the “files” table and returns the resource location of the
remote files. The resource location includes the remote hostname, port number, remote file name, and the
connection speed (the host name and connection speed is retrieved from the corresponding entry in the
“users” table). The server allows multiple clients to register and upload their descriptions at the same time. It also allows multiple hosts to query for files at the same time. It tracks the availability of the remote resources too. If a host “unregistered” from the system, the associated file descriptions and user information are deleted from the system. A screen capture of the required server side of the application is given in Figure 2.

## Notes for Developers

### Work-Flow of Server-Host Connection

1. Host-A connects to "Centralized Server" (CS) and simultaneously uploads files to share and their associated descriptions.
2. CS sends ack-message to Host-A.
3. Host-A sends a keyword to search CS's available files.
4. CS replies to Host-A with a list of the files that match the keyword search and who (which host) has them.
5. After deciding what file it wants, the *client* in Host-A requests the file to the *server* in Host-B.
6. Host-B sends the file to the *client* in Host-A.

### Centralized-Server

__Requirements:__

- Multithread, to allow clients to register and upload their description at the same time.
- Keeps track of all the available files and who has them in real-time. If a host disconnects, then it doesn't show its files.
- Provides simple keywords search, returning a list of files and their information.

#### Structure and Methods

`Main()`
- This listens for new connections from hosts on a ServerSocket, then sends the new hosts to clientHandler(), creating a new thread.

`clientHandler() class` 
- Creates a new thread for the new client (host).
- It receives the file descriptions from the host and stores them in a two-dimensional array, stored in the Centralized-Server class. We can use a function storeInfo() to do this.
- It will listen for a keyword search request from the host.
- When it receives it, it will scan the two-dimensional array and will return a list of the matching files to the host.

### Host

__Client Requirements:__

- Initially connects to Centralized-Server and sends file descriptions.
- Can request keyword searches to the CS
- Once it obtains the results from the search it can *retreive* a file from another host.

__Server Requirements:__

- Listens for file requests.
- When a file is requested, it sends the file over a TCP connection.

#### Strucure and Methods

`GUI class`
- picture in assignment PDF

`onConnect()` 
- Activated from the GUI.
- Connects to the Centralized Server using the info provided by the user on the GUI.
- Sends file descriptions to the Centralized Server.

`keywordSearch()`
- Activated from the GUI.
- Sends a ketword search request along with the keyword(s).
- Once it receives an answer, it displays the different files.

`requestFile()`
- Once the user decides what files he/she wants, this method will send a transfer request to the *server of the host* that has such file and it will download it.

`quit()`
- This stops the connection and shuts the whole host

## Development Updates

### October 31, 2017

- Javier and Louis added initial GUI implementation
- Brendon and Matthew added sample client files (XML, images, PDFs) for testing
- Brendon and Louis added UPDATE command that would execute in initial connection and upon other updates

### November 6, 2017

**Louis and Brendon** 
- On pressing button "Connect," the Hostname, Username, and Speed are sent to the Central-Server. The Central-Server sees this and reports this variables on new received connections.
- On pressing button "Connect," the file-descriptions as generated by reading the XML document of the client should be sent over the line to the Central-Server. Our code now reads from XML (though could be cleaned up). It is almost prepared to send a string over the control-line; the server is not prepared to read it.
- The "Connect" button can now only be pressed once.

### November 11, 2017

**Brendon** 
- Updated the hosts' reading from XML to token
- Added handles "wrongly named or missing XML" 
- Added handles "connection errors" 
- Now with more debugging and comments

### November 13, 2017

**Brendon**
- Central-Server thread for particular user now shows that there was an attempt to connect in the case that the user had an XML-read error, then ends that thread.
- Connecting client that has XML-read error now can repeat attempts to connect. User can fix the XML-filename while the GUI is still running, then connect.
- Took a hard peek into Javier's new FTP-Host-Server implementation. 

### November 16, 2017

**Brendon**
- Merged Javier's code with the main branch.
- Client as an FTP-Host to another client now has more framework. 
  - Because our example-case is all on one computer, each GUI that runs needs its own port for another client to FTP-connect to. 
  - Each client will now try 5 different ports as its "welcomePort" for FTP-connections.
  - Each client has multithreading for the capacity of (1) multiple FTP-connections to it at once, and (2) to allow the GUI to run separately from the main GUI.
- The CentralServer now grabs the client's IP from the connection rather than from text-input. The default "Hostname" was changed to reflect that.

### November 17, 2017

**Brendon**
- Upon new host connection, the server-log displays the host's data in a friendly way.
- More error-handling.
- Some old code cleanup.
- The GUI now sends keywords to the Central-Server.
- The Central-Server displays commands read from hosts in a way that supports multiple hosts well.
- Added some client-side handling.
  - Form must have a username to initiate ANY connection to the Central-Server.
  - Form must have SOMETHING in the keywords textfield to intiate a search.

**Brendon**
- Solved world hunger.
- More error-handling.
- On connection to central-server the server now validates that the username is unique.
- Central-Server now stores each client's FTP-Welcome-Port. Each FTP-port, one per user, may be different in consideration of how many we have running from one IP. This port number is generated to have up to five different users on one computer. THIS is the number that allows one client to FTP-download from another.
- On user-leave, the Central-Server's tables are updated.
- The server output looks extra spiffy.

**Brendon and Javier**
- The Central-Server now sends a correct string of which files match the given keyword over the line
- The Client GUI has that information of the matching keywords and can display on terminal
- Attempts to have this display on the GUI correctly

**TO DO**
1. Figure how to update the GUI with new values in that form area.
2. Implement the FTP functions for one client to another.
3. Maaaaaaaybe implement a "Disconnect" button.
4. Maaaaaaaybe validate the files from the XML document to actually exist (we had code for this from the last one).
5. Maaaaaaaybe add more interesting sample-cases.
