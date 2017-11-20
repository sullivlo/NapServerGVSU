# NapServer - A Written Report

## Rough Draft

From the assignment:

> The programs should be submitted in two formats, a paper version, and a compressed electronic version that contains all of your source codes. Email [the professor] the programs as an attachments at elsaidm@gvsu.edu

> You must indicate the project completion percentage. The project completion percentage describes how complete is your project work and meets the requirements.

His Example Report includes a simple UDP diagram that shows some interaction between the Central-Server and the Clients.

## Napster Redesign

The system that we have designed is a loosely mirrored example of the Napster system--a peer-to-peer file-sharing system. The basic architecture is that of a main-server that handles connections of clients that send the main-server their information of which files they are making available to other clients. This allows other clients to be able to search the main-server for available files, to then connect to the other client individually to manage downloading the files from that client. It is important to note that the main-server acts simply as a middle-man that handles simple tables of user meta-data so that the users can act accordingly.

The follow are some critical notes on Napster, orginally "Rhapsody." Here's a quote from Wikipedia on [Napster](https://en.wikipedia.org/wiki/Napster).

> Initially, Napster was envisioned as an independent peer-to-peer file sharing service by Shawn Fanning. The service operated between June 1999 and July 2001.[10] Its technology allowed people to easily share their MP3 files with other participants.[11] Although the original service was shut down by court order, the Napster brand survived after the company's assets were liquidated and purchased by other companies through bankruptcy proceedings.

> Later companies and projects successfully followed its P2P file sharing example such as Gnutella, Freenet, Kazaa, BearShare, and many others.

## Report Overview

More specifically this document serves to explain the functionality of the system with full detail. Our document will do this by (1) showing screen captures of example run-throughs, (2) having discussion of the basic logic of the system and its individual modules, and (3) further reflecting on issues found and resolved over the course of the project.

## Design

Image of UPD-Diagram Here!

## Features

The following are notable features that we have implemented to this system:
- Our *NapServer* features a GUI interface generated with javax.swing.
- The Client-GUI allows the user to connect with a chosen username, which is constrained by the Central-Server to be unique.
- The Central-Server stores an ever-changing list of available files based on current connected users.
- The connections between Central-Server and its Clients are setup to have full availability of error-handling (if desired in future updates, we could allow the Client-GUI to fully show disconnects and errors, number of connected users, total amount of available data).
- The Client-GUI constrains the user to have a properly-named XML document.

## Problems and Resolution

In a certain way its hard to see the problems as problems once they are resolved. The following is a reflection on a few of the resolved (and unresolved) problems that came up in development.

**Making Sure the Code is Understood**

There was a subtle difficulty in making sure the entire group had full understanding of the code at a given moment. This may have given friction to allow for individuals to feel that they could work on it on their own. With frequent, critical, updates--that were also mostly the brainchild of any one developer at a given time--it was difficult to be sure that everyone was on the page at the same time. To handle this, there were lots of in-code comments and an implemented area to track developer developments on the Github page.  

**Finding Time to Meet for Coding**

The timing of this project in the semester had it competing for attention with projects from other classes. with the added social pressure of any group-project, we certainly all found the time to meet and regularly inquired on new times to meetup through email. Even though I (Brendon) found our meetups fruitful and regular enough to be productive, it still seemed like there was some difficulty in getting everyone together at one time in crucial moments.

**Finding Proper Dynamic to Resolve Design Conflicts**

First, I should say any conflicts were resolved and that all the involved developers were incredibly inclined to reduce tensions and find resolve. It was a wonderful group.

There were a couple dillemas in design choice that surfaced. A notable one was how to implement a "Quit" function. This may have been instructed as an allocated creative choice, but by the writing and sample image of the assignment document it was unclear between us if a "Disconnect" button needed to exist, or if a typed "Quit" in the input-command area of the FTP window should disconnect the user from the Central-Server. With full intention of following the required functionality, this was forced as an issue. Some more resolve may have come from directly inquiring from the professor, but this was feared to come with a "I told you so" attached feeling. We came to decide to have the only way to disconnect from the entire system was by exiting the main GUI. 

**Complications in Use of so Many Classes**

I should note that a proper UDP-diagram early in development to solidify developer's potential choices in code may have allowed for more individual-work to happen due to a knowledge of their conductivity with the center headspace. It was hard to foresee the variability in coding styles and knowledge of classes between developers, noting that one may be hesitant to put themselves in a position to admit lack of knowledge.

The many interacting classes in parallel with the many interacting systems made it difficult to code and have fruitful communication. Both developers in any conversation had to have full understanding of the entire link to be able to debug well.

## Summary

This project has been a wonderous exercise of several things: 
- In classes and objects in Java.
- In small-group code-management with Github, email-chains, and code checkpoints.
- In early team project-design.
- In handling ports, streams, and multi-threading.
- In many many cases, writing much-needed error-handling.

There were many long afternoons spent with two to three guys typing out code and debugging time away. This project took much longer than expected. I am confident in saying that each of us have come to be much more comfortable with the code and the needed-headspace to complete projects at this scope.

## Developers

- Louis Sullivan
- Javier Ramirez-Moyano
- Matthew Schuch
- Brendon Murthum
