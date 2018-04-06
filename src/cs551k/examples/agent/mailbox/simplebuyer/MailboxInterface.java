//we specify the Interface that defines the set of remote methods one or more
//class specifications will implement. We call this interface the "MailboxInterface",
//as it defines an interface to a particular remote service:

package cs551k.examples.agent.mailbox.simplebuyer;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Martin Kollingbaum
 * University of Aberdeen
 * 2018
 *
 */
public interface MailboxInterface extends Remote		//This interface makes two remote methods available: send and receive. 
{
  public Message receive (String agentname) throws RemoteException ;
  public void send(Message message) throws RemoteException;

}