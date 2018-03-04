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
 *
 */
public class Mailbox implements MailboxInterface
{
  private ArrayList<Message> messageList = new ArrayList<Message>() ;

  /**
   * 
   */
  public Mailbox() {
    // TODO Auto-generated constructor stub
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
  public static void main(String[] args) 
  {
    // look at the server example and fill in all the required statements to register
    // a mailbox remote object with the rmiregistry
	  
	  if (args.length < 2) {
          System.err.println( "Server: wrong number of paramters,required: <registryport> <serverport>" ) ;
          return;
     }
     try
     {
        String hostname = (InetAddress.getLocalHost()).getCanonicalHostName() ;
        int registryport = Integer.parseInt( args[0] ) ;
        int serviceport = Integer.parseInt( args[1] );

        System.out.println ( "Server> hostname = " + hostname ) ;
        System.out.println ( "Server> registry port = " + registryport ) ;
        System.out.println ( "Server> service port = " + serviceport ) ;

        System.setProperty( "java.security.policy", "security.policy" ) ;
        System.setSecurityManager( new SecurityManager() ) ;

        // ===============================================
        // this instantiates the remote object
        Mailbox mailbox = new Mailbox() ;
        //
        // ===============================================

        // export remote object at a particular port
        // if serviceport == 0, then Java will find the next available port
        MailboxInterface stub = (MailboxInterface) UnicastRemoteObject.exportObject(mailbox, serviceport) ;
        // register the remote object with the rmiregistry with the object's stub
        String regURL = "rmi://" + hostname + ":" + registryport + "/MailboxService";
        Naming.rebind(regURL, stub) ;

        System.out.println ( "Server ready." ) ;
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
