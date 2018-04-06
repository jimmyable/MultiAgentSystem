package cs551k.examples.agent.mailbox.simplebuyer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @author Martin Kollingbaum
 * University of Aberdeen
 * 2018
 *
 */
public class Agent
{
  private String           myname       = null ;
  private String           hostname     = null ;
  private int              registryport = 0    ;
  private MailboxInterface mailbox      = null ;

  
  
  /**
   * @throws NotBoundException 
   * @throws RemoteException 
   * @throws MalformedURLException 
   * 
   */
  public Agent(String agentname, String hostname, int registryport) throws MalformedURLException, RemoteException, NotBoundException
  {
    this.myname       = agentname ;
    this.hostname     = hostname  ;
    this.registryport = registryport ;
    
        // Obtain the service reference from the RMI registry
        // listening at hostname:registryport.
        
        String regURL = "rmi://" + hostname + ":" + registryport + "/Mailbox";
        System.out.println( "Looking up " + regURL );

        // ===============================================
        // lookup the remote service, it will return a reference to the stub:
        
        mailbox = (MailboxInterface) Naming.lookup( regURL );
        
        System.out.println( "Agent " + myname + " connected to mailbox." );
        
        //new Thread ( receiver = new Receiver( myname, mailbox ) ).start(); ;
        
        try
        {
          
      //better_conversation() ;
          if ( myname.equalsIgnoreCase("seller") ) seller( ) ;
          else                                     buyer ( "seller", "item1" ) ;
    } 
        catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  

  
  private void buyer ( String sellername, String itemname ) throws IOException
  {
    // ===============================================
        // Console input:
    BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));
    Message        message = null ;
    double         myprice     = 0 ;
    double         myfactor    = 0.5 ;
    double         mythreshold = 0.1 ;

    System.out.print( "Start buyer, press return "); String command = in.readLine() ;

    // ask for price of item
    message = new Message (Message.ASK_PRICE, myname, sellername, itemname, 0 ) ;
    mailbox.send(message);

    while ( true )
    {
      if ((message = mailbox.receive(myname)) != null) {
        System.out.println( "\n" + "   received from " + message.getSender() + ":");
        System.out.println("Item  = " + message.getItem() );
        System.out.println("Price = " + message.getPrice() );
        
        if ( message.getMessageType() == Message.ACCEPT) {  
          System.out.println( "Seller accepts at price " + message.getPrice() );
          break ;
        }
        else if ( (message.getPrice() - myprice) < mythreshold )
        {
          System.out.println( "Buyer accepts at price " + message.getPrice() );
          message = new Message (Message.ACCEPT, myname, message.getSender(), message.getItem(), message.getPrice());
          break ;
        }
        
        myprice = myprice + ((message.getPrice() - myprice) * myfactor) ;
        
        message = new Message ( Message.TELL_BID, myname, message.getSender(), message.getItem(), myprice) ;
        mailbox.send(message);

      }
    }
  }
  
  private void seller ( ) throws IOException
  {
    // ===============================================
        // Console input:
    BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));
    Message        message = null ;
    double         myprice     = 100 ;
    double         myfactor    = 0.5 ;
    double         mythreshold = 0.1 ;

    while (true)
    {
      System.out.print( "==============> Continue seller, press return "); String command = in.readLine() ;
      
            // get all the messages in the mailbox
      // there can be multiple bidders
            // we first have to loop until something shows up in the mailbox
            // then we have to receive all the messages in there
            while ( (message = mailbox.receive(myname)) == null) ;
            do
            {
              System.out.println( "\n" + "   received from " + message.getSender() + ":");
        System.out.println("Item  = " + message.getItem() );
        System.out.println("Price = " + message.getPrice() );
        
        if ( message.getMessageType() == Message.ACCEPT) {  
          System.out.println( "Buyer " + message.getSender() + " accepts at price " + message.getPrice() );
          break ;
        }
        else if ( (myprice - message.getPrice()) < mythreshold )
        {
          System.out.println( "I accept at price " + message.getPrice() );
          message = new Message (Message.ACCEPT, myname, message.getSender(), message.getItem(), message.getPrice());
          break ;
        }
        
        myprice = myprice - ((myprice - message.getPrice()) * myfactor) ;
        
        message = new Message ( Message.TELL_BID, myname, message.getSender(), message.getItem(), myprice) ;
        mailbox.send(message);

        } 
            while ((message = mailbox.receive(myname)) != null) ;

    }
  }
  
  /**
   * @param args
   */
  public static void main(String[] args) {

        // Specify the security policy and set the security manager.
        System.setProperty( "java.security.policy", "security.policy" ) ;
        System.setSecurityManager( new SecurityManager() ) ;

        String agentname = args[0];
        String hostname  = args[1];
        int registryport = Integer.parseInt( args[2] );

        try 
        {
          // instantiate an agent from this class
          
      new Agent ( agentname, hostname, registryport ) ;
      
    } 
        catch (MalformedURLException | RemoteException | NotBoundException e)
        {
      e.printStackTrace();
    }


  }

}