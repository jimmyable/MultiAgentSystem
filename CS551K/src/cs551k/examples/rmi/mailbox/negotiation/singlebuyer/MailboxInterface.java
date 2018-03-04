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
public interface MailboxInterface extends Remote
{
  public Message receive(String agentname) throws RemoteException ;
  public void send(Message message) throws RemoteException;

}
