# NapServerGVSU

## Abstract

Implements a central server that connect multiple hosts and share files among hosts.

## Assignment Intro

In this project, your file transfer protocol will form the basis of a file sharing system that allows users to access a distributed data storage system based on simple keyword search. The specification of this project relies on the
principles of using the centralized directory indexing service to implement a P2P architecture. The system
consists of two parts:
- The first part is the host system, which can query the server for files using keywords. The host also has
a file transfer client and server. The ftp client allows a user to access files stored at the remote user
locations. The ftp server is responsible for providing file transfer services requested by a remote client.
- The second part of the system is the centralized server, which provides a search facility that can be
used to perform simple keyword searches. The result of the search is the location of the remote
resource.

## Host Implementation

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

## Centralized Server Implementation

The server tracks current users and the files shared from their site. Once a connection is initiated from a host,
it stores the username, connection speed, and hostname in a “users” table. After this, it acknowledges to the
host that it has received this information and then waits for the shared file descriptions to be uploaded. Once it
receives the file descriptions, it’ll be stored in a “files” table.
It also provides simple keyword search and returns the description of the remote resource. On receipt of a
keyword search, the server performs the search in the “files” table and returns the resource location of the
remote files. The resource location includes the remote hostname, port number, remote file name, and the
connection speed (the host name and connection speed is retrieved from the corresponding entry in the
“users” table). The server allows multiple clients to register and upload their descriptions at the same time. It also allows multiple hosts to query for files at the same time. It tracks the availability of the remote resources too. If a host “unregistered” from the system, the associated file descriptions and user information are deleted from the system. A screen capture of the required server side of the application is given in Figure 2. 
