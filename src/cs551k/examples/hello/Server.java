package cs551k.examples.hello;		//usual imports that we need

import java.net.InetAddress;
import java.rmi.Naming;
import java.lang.SecurityManager;
import java.rmi.server.UnicastRemoteObject;

public class Server
{
   public Server() {
      
   }

  
   public static void main ( String args[] )		//the main() method is only run once when this class is 
   													//loaded and started for execution; in main(), the following
   													//actions are performed: 
   {
      if (args.length < 2) {
           System.err.println( "Server: wrong number of paramters,required: <registryport> <serverport>" ) ;
           return;
      }
      try
      {
         String hostname = (InetAddress.getLocalHost()).getCanonicalHostName() ;		//Retrieve the hostname of your computer
         int registryport = Integer.parseInt( args[0] ) ;
         int serviceport = Integer.parseInt( args[1] );

         System.out.println ( "Server> hostname = " + hostname ) ;
         System.out.println ( "Server> registry port = " + registryport ) ;
         System.out.println ( "Server> service port = " + serviceport ) ;

         System.setProperty( "java.security.policy", "security.policy" ) ;
         System.setSecurityManager( new SecurityManager() ) ;		//create a security manager, that access the security policy file
        
         // ===============================================
         // this instantiates the remote object
         Hello hello = new Hello() ;		//Instantiate the most important part - the server object
         //
         // ===============================================

         // export remote object at a particular port
         // if serviceport == 0, then Java will find the next available port
         HelloInterface stub = (HelloInterface) UnicastRemoteObject.exportObject(hello,serviceport) ;		//export the server object to a
         																									//particular port, this creates a 
         																									//stub for this object that then 
         																									//can be handed over to the rmiregistry
         // register the remote object with the rmiregistry with the object's stub
         String regURL = "rmi://" + hostname + ":" + registryport + "/HelloService";
         Naming.rebind(regURL, stub) ;		//bind this identifier to the server stub and submit it to the registry
        
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
      	//The server runs only once and it's only purpose is to register and export remote objects
      
      //Please note: you could also move the main() method of this class Server directly into class 
      //"Hello" - as main() is declared static, it is run only when the class "Hello" is loaded, which 
      //then leads, in exactly the same fashion, to the instantiation of a remote object based on class 
      //"Hello", its export and registration with the rmiregistry.


   }
}