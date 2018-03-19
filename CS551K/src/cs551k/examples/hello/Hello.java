//we specify the remote object, that implements this interface

package cs551k.examples.hello;

import java.rmi.RemoteException;

public class Hello implements HelloInterface
{
   public Hello() throws RemoteException
   {

   }

   public String receiveMessage ( String s) throws RemoteException		//the method be be called remotely is receiveMessage ( String s )
   {
      return s.toUpperCase() + " BACK !!" ;
   }

}