//package hello;

package cs551k.examples.rmi.mailbox.negotiation.singlebuyer; //the package 

import java.io.BufferedReader;		//load the imports
import java.io.IOException;
import java.io.InputStreamReader;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;	//does this really not get used?


public class Agent		//declare the public class
{

    public static void main(String args[]) throws IOException, NotBoundException	//make the main method of the public class, The method takes in cmd line args
    {

        if (args.length < 3) {	//if less than 3 args come in
            System.err.println( "Agent: wrong number of paramters, required: <agentname>, <host> <registryport>" ) ;	//send out this error 
            return;	//gotta return back to the main thread?
        }

       String agentname = args[0];		//right now we can set variable "agentname" to the first arg
       String host = args[1];			//same with host and second args
       int registryport = Integer.parseInt( args[2] );	//remember the int cast, 3rd arg should be registry port
       
      ///the all RMI related elements of the main()

       //TODO make a basic endless loop where the agent waits for console input,
       //TODO creates a Message object, provide the agent name and the text from
       //TODO the console and then sends it to the mailbox
       System.setProperty( "java.security.policy", "security.policy" ) ;	//set the security policy to the one we have for this JVM instance
       System.setSecurityManager( new SecurityManager() ) ;					//I think this is to reset the SecurityManager with the new policy
       
       String regURL = "rmi://" + host + ":" + registryport + "/MailboxService";	//create var regURL for giggles
       System.out.println( "Looking up " + regURL );			//now we print out the variable to look cool

       // ===============================================
       // lookup the remote service, it will return a reference to the stub:

     //Did you know you dont have to import java files in the same package
       MailboxInterface server = (MailboxInterface) Naming.lookup( regURL ); //we make a server that is a MailboxInterface obj,using naming lookup we make the object ref, i think

       while(true)		//begin the endless loop
       {
          // read from console: look into the client example above, how to create
          // a BufferedReader for the console (System.in)

           BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));		//used to record input messages
           System.out.print( "Enter message:" );			//self explanatory
           String message = in.readLine() ;					//take in the line entered for input(in) as 'message'
           System.out.print( "Enter reciever:" );			//self explanatory
           String reciever = in.readLine() ;				//take in the next line that was entered
          // create the message object
           Message m = new Message(reciever, message);		//create a message obj as follows. 
          // send the message object to the mailbox
           server.send(m);									//oh wow and you can just send the 'message' to the 'mailbox', thats rad!
          // try to receive something from the mailbox. If the mailbox
          // is empty for this client, then call receive again

          while ( (m = server.receive(agentname)) == null) ; //wait to recieve something back. it has to have this agents name on it for the reciever part

          System.out.println ( "Message received: " + m.getMessage() ) ; //if message to display, then do so
       }

    }
}
