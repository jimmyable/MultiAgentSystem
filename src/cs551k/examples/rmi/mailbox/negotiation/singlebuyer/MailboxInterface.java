//package hello;
package cs551k.examples.rmi.mailbox.negotiation.singlebuyer;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Martin Kollingbaum
 * University of Aberdeen
 * 2018
 *
 */
public interface MailboxInterface extends Remote //An interface means anyone using this class must implement the following methods
{
  public Message receive (String agentname ) throws RemoteException ; //to recieve a message there must be an agent name
  public void send(Message message) throws RemoteException;				//to send a message there must be a message

}
