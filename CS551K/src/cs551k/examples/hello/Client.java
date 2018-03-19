//Forth, we implement the client. The client also must instantiate a security manager. 
//Before it can call methods on the remote object, it has to look it up with the Naming service by its URL,
//which returns a reference to the remote object for the client The client may look like this: 

package cs551k.examples.hello;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.rmi.Naming;
//import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;


public class Client
{
    public static void main(String args[]) throws RemoteException		//the main() method is only run once - 
    																	//when this class is loaded and started 
    																	//for execution; in main(), the following 
    																	//actions are performed: 
    {
        if (args.length < 2) {
            System.err.println( "Client: wrong number of paramters,required: <host> <port>" ) ;
            return;
        }

        // the client must know where the rmiregistry resides that knows the requested
        // remote object - hostname and registryport have to provided as commandline
        // parameters
        
        String hostname = args[0];
        int registryport = Integer.parseInt( args[1] );

        // Specify the security policy and set the security manager.
        System.setProperty( "java.security.policy", "security.policy" ) ;		//create a security manager, that
        System.setSecurityManager( new SecurityManager() ) ;					//access the security policy file
        
        try 
        {
            // Obtain the service reference from the RMI registry
            // listening at hostname:registryport.
            
            String regURL = "rmi://" + hostname + ":" + registryport + "/HelloService";
            System.out.println( "Looking up " + regURL );		//create a string regURL representing a unique 
            													//identification for the remote object 

            // ===============================================
            // lookup the remote service, it will return a reference to the stub:
            
            HelloInterface server = (HelloInterface) Naming.lookup( regURL );		//look up the remote object
            																		//with regURL at the given rmiregistry


            // ===============================================
            // User Interaction:
            // Prompt the user to type in a message to be sent to the remote service:

            BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));
            System.out.print( "Enter message:" );
            String message = in.readLine() ;


            // ===============================================
            // Invoke the remote method
            
            String answer = server.receiveMessage ( message ) ;		//call the remote object method
            
            // ===============================================
            // and print out the result.
            
            System.out.println( answer );		//print out the return message

            // ===============================================
        
        }
        catch (java.io.IOException e) {
            System.err.println( "I/O error." );
            System.err.println( e.getMessage() );
        }
        catch (java.rmi.NotBoundException e) {
            System.err.println( "Server not bound." );
            System.err.println( e.getMessage() );
        }
    }
}