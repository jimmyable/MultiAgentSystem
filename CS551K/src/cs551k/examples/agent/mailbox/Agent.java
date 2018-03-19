//implement an "Agent" as a client. The client must instantiate a security manager. 
//Before it can call methods on the remote object, it has to look it up with the Naming 
//service by its URL, which returns a reference to the remote object for the client The 
//client may look like this: 

package cs551k.examples.agent.mailbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
//import java.rmi.SecurityManager;
import java.rmi.RemoteException;




public class Agent
{

    public static void main(String args[]) throws RemoteException, IOException, NotBoundException
    {

        if (args.length < 3) {
            System.err.println( "Agent: wrong number of paramters,required: <agentname>, <host> <registryport>" ) ;
            return;
        }

       String agentname = args[0] ;
       String hostname = args[1];			//same with host and second args
       int registryport = Integer.parseInt( args[2] );	//remember the int cast, 3rd arg should be registry port
       
       System.setProperty( "java.security.policy", "security.policy" ) ;	//set the security policy to the one we have for this JVM instance
       System.setSecurityManager( new SecurityManager() ) ;					//I think this is to reset the SecurityManager with the new policy
       
       String regURL = "rmi://" + hostname + ":" + registryport + "/MailboxService"; //print out stuff to look cool
       System.out.println( "Looking up " + regURL );			//now we print out the variable to look cool
       
       MailboxInterface server = (MailboxInterface) Naming.lookup( regURL );

       // make a basic endless loop where the agent waits for console input,
       // creates a Message object, provide the agent name and the text from
       // the console and then sends it to the mailbox

       while(true)
       {
    	   BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));		//used to record input messages
           System.out.print( "Enter message:" );			//self explanatory
           String message = in.readLine() ;					//take in the line entered for input(in) as 'message'
           System.out.print( "Enter reciever:" );			//self explanatory
           String receiver = in.readLine() ;				//take in the next line that was entered
          // create the message object
           Message m = new Message(receiver, message);		//create a message obj as follows. 
          // send the message object to the mailbox
           server.send(m);									//oh wow and you can just send the 'message' to the 'mailbox', thats rad!

          while ( (m = server.receive(agentname)) == null) ;

          System.out.println ( "Message received: " + m.getMessage() ) ;
       }

    }
}
