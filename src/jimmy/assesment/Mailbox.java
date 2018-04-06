package jimmy.assesment;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;

public class Mailbox implements MailboxInterface
{
  int count =0;
private ArrayList<Message> messageList = new ArrayList<Message>() ;
  

  public Mailbox() 
  { 
    System.out.println( "Mailbox> started." );
  }


  @Override
  public synchronized void send(Message message) throws RemoteException
  {
	  count+=1;
    System.out.println ("Mail"+count+"> in send: agent " + message.getSender() + " deposits message, with type" + message.getMessageType() ) ;
    
    messageList.add( message ) ;
  }

  @Override
  public synchronized Message receive(String agentname) throws RemoteException
  {
    Iterator<Message> it = messageList.iterator() ;
    
    while (it.hasNext() ) {
      Message m = (Message) it.next();
      if ( m.getReceiver().equals(agentname) )
      {
        System.out.println("Mail"+count+"> in receive: message for agent " + agentname + " found" );
        it.remove();
        return m ;
        
      }
        
    }
    
    return null ;
  }

  /**
   * @return the agentList
   */
  public ArrayList getMessageList() throws RemoteException
  {
    return messageList ;
  }

  
  /**
   * @param args
   */
  public static void main(String[] args) 
  {
        // Specify the security policy and set the security manager.
        System.setProperty( "java.security.policy", "security.policy" ) ;
        System.setSecurityManager( new SecurityManager() ) ;
        try
        {
          String hostname = (InetAddress.getLocalHost()).getCanonicalHostName() ;
          int registryport = Integer.parseInt( args[0] ) ;
          int serverport = Integer.parseInt( args[1] ) ;
      
          System.setProperty( "java.security.policy", "security.policy" ) ;
          System.setSecurityManager( new SecurityManager() ) ;

          String regURL = "rmi://" + hostname + ":" + registryport + "/Mailbox";
            System.out.println( "Registering " + regURL );
            
            Mailbox mailbox = new Mailbox();
            MailboxInterface mailboxstub = (MailboxInterface)UnicastRemoteObject.exportObject( mailbox, serverport );

      Naming.rebind( regURL, mailboxstub );
    } 
        catch (RemoteException | MalformedURLException | UnknownHostException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}