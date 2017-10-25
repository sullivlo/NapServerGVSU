# NapServerGVSU

## Abstract

Implements a central server that connect multiple hosts and share files among hosts.

## Assignment Information

#### Introduction

In this project, your file transfer protocol will form the basis of a file sharing system that allows users to access a distributed data storage system based on simple keyword search. The specification of this project relies on the
principles of using the centralized directory indexing service to implement a P2P architecture. The system
consists of two parts:
- The first part is the host system, which can query the server for files using keywords. The host also has
a file transfer client and server. The ftp client allows a user to access files stored at the remote user
locations. The ftp server is responsible for providing file transfer services requested by a remote client.
- The second part of the system is the centralized server, which provides a search facility that can be
used to perform simple keyword searches. The result of the search is the location of the remote
resource.

#### Host-Implementation

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

#### Centralized-Server-Implementation

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

Big thanks to Javier for writing this up!

#### Work-Flow of Server-Host Connection

1. Host-A connects to "Centralized Server" (CS) and simultaneously uploads files to share and their associated descriptions.
2. CS sends ack-message to Host-A.
3. Host-A sends a keyword to search CS's available files.
4. CS replies to Host-A with a list of the files that match the keyword search and who (which host) has them.
5. After deciding what file it wants, the *client* in Host-A requests the file to the *server* in Host-B.
6. Host-B sends the file to the *client* in Host-A.

#### Centralized-Server

- Multithread, to allow clients to register and upload their description at the same time.
- Keeps track of all the available files and who has them in real-time. If a host disconnects, then it doesn't show its files.
- Provides simple keywords search, returning a list of files and their information.

**Structure and Methods**

__Main()__ : This listens for new connections from hosts on a ServerSocket, then sends the new hosts to clientHandler(), creating a new thread.

__clientHandler() class__ : 
- Creates a new thread for the new client (host).
- It receives the file descriptions from the host and stores them in a two-dimensional array, stored in the Centralized-Server class. We can use a function storeInfo() to do this.
- It will listen for a keyword search request from the host.
- When it receives it, it will scan the two-dimensional array and will return a list of the matching files to the host.

#### Host

**Requirements**

__Client:__

- Initially connects to Centralized-Server and sends file descriptions.
- Can request keyword searches to the CS
- Once it obtains the results from the search it can *retreive* a file from another host.

__Server:__

- Listens for file requests.
- When a file is requested, it sends the file over a TCP connection.

**Strucure and Methods**

__GUI class:__ - picture in assignment PDF

__onConnect()__ 
- Activated from the GUI.
- Connects to the Centralized Server using the info provided by the user on the GUI.
- Sends file descriptions to the Centralized Server.

__keywordSearch()__
- Activated from the GUI.
- Sends a ketword search request along with the keyword(s).
- Once it receives an answer, it displays the different files.

__requestFile()__
- Once the user decides what files he/she wants, this method will send a transfer request to the *server of the host* that has such file and it will download it.

__quit()__
- This stops the connection and shuts the whole host

