package cs551k.examples.rmi.mailbox.negotiation.singlebuyer;

import java.net.InetAddress;
import java.rmi.Naming;



import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Martin Kollingbaum
 * University of Aberdeen
 * 2018
 */
public class Mailbox implements MailboxInterface	//hello darkness my old friend
{
  private ArrayList<Message> messageList = new ArrayList<Message>(); //create a new arraylist that stores messages

  
  public Mailbox() {		//we got a public class over here, MAILBOX
    // TODO Auto-generated constructor stub QUE?
  }


  @Override			
  public synchronized void send(Message message) throws RemoteException {
    messageList.add( message ) ;
    
  }

  @Override
  public synchronized Message receive(String agentname) throws RemoteException {
    Iterator<Message> it = messageList.iterator() ;
    
    while (it.hasNext() ) {
      Message m = it.next();
      if ( m.getReceiver().equals(agentname) ) {
        it.remove();
        return m ;
      }
        
    }
        
    return null ;
  }

  /**
   * @return the agentList
   */
  public ArrayList<Message> getMessageList() throws RemoteException
  {
    return messageList ;
  }

  
  /**
   * @param args
   */
  public static void main(String[] args) //here be the main method, takes in two args
  {
    // look at the server example and fill in all the required statements to register
    // a mailbox remote object with the rmiregistry
	  
	  if (args.length < 2) {	//if not given 2 args throw error
          System.err.println( "Server: wrong number of paramters,required: <registryport> <serverport>" ) ;
          return;
     }
     try //else
     {
        String hostname = (InetAddress.getLocalHost()).getCanonicalHostName() ; //get hostname using magic
        int registryport = Integer.parseInt( args[0] ) ;						//set first arg as the registry port
        int serviceport = Integer.parseInt( args[1] );							//set second arg as the service port

        System.out.println ( "Server> hostname = " + hostname ) ;				//print out the magic hostname
        System.out.println ( "Server> registry port = " + registryport ) ;		//print out the ports that we just initiated
        System.out.println ( "Server> service port = " + serviceport ) ;		//same with the other port

        System.setProperty( "java.security.policy", "security.policy" ) ;		//java security stuff to mess about with rmi etc.
        System.setSecurityManager( new SecurityManager() ) ;

        // ===============================================
        // this instantiates the remote object
        Mailbox mailbox = new Mailbox() ;										//now we are going to make the actual object
        //
        // ===============================================

        // export remote object at a particular port
        // if serviceport == 0, then Java will find the next available port
        MailboxInterface stub = (MailboxInterface) UnicastRemoteObject.exportObject(mailbox, serviceport); //link up the mailbox to the interface using stub
        // register the remote object with the rmiregistry with the object's stub
        String regURL = "rmi://" + hostname + ":" + registryport + "/MailboxService"; //print out stuff to look cool
        Naming.rebind(regURL, stub) ;												//do stuff to find remote location or summin

        System.out.println ( "Server ready." ) ;									//WE READY
     }
     catch (java.net.UnknownHostException e)
     {
        System.err.println( "Cannot get local host name." );
        System.err.println( e.getMessage() );
     }
     catch (java.io.IOException e)
     {
        System.err.println( "Failed to register." );
        System.err.println( e.getMessage() );
     }

  

    if (args.length < 2) {
           System.err.println( "Server: wrong number of paramters, required: <registryport> <mailboxport>" ) ;
           return;
    }

    // etc. ... for you to complete
  }
}
