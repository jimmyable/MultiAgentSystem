//package hello;

package cs551k.examples.rmi.mailbox.negotiation.singlebuyer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.rmi.Naming;
import java.rmi.NotBoundException;
//import java.rmi.RemoteException;


public class Agent
{

    public static void main(String args[]) throws IOException, NotBoundException
    {

        if (args.length < 3) {
            System.err.println( "Agent: wrong number of paramters, required: <agentname>, <host> <registryport>" ) ;
            return;
        }

       String agentname = args[0];
       String host = args[1];
       int registryport = Integer.parseInt( args[2] );
       
      ///te all RMI related elements of the main()

       // make a basic endless loop where the agent waits for console input,
       // creates a Message object, provide the agent name and the text from
       // the console and then sends it to the mailbox
       System.setProperty( "java.security.policy", "security.policy" ) ;
       System.setSecurityManager( new SecurityManager() ) ;
       
       String regURL = "rmi://" + host + ":" + registryport + "/MailboxService";
       System.out.println( "Looking up " + regURL );

       // ===============================================
       // lookup the remote service, it will return a reference to the stub:

       MailboxInterface server = (MailboxInterface) Naming.lookup( regURL );

       while(true)
       {
          // read from console: look into the client example above, how to create
          // a BufferedReader for the console (System.in)

           BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));
           System.out.print( "Enter message:" );
           String message = in.readLine() ;
           System.out.print( "Enter reciever:" );
           String reciever = in.readLine() ;
          // create the message object
           Message m = new Message(reciever, message);
          // send the message object to the mailbox
           server.send(m);
          // try to receive something from the mailbox. If the mailbox
          // is empty for this client, then call receive again

          while ( (m = server.receive(agentname)) == null) ;

          System.out.println ( "Message received: " + m.getMessage() ) ;
       }

    }
}
