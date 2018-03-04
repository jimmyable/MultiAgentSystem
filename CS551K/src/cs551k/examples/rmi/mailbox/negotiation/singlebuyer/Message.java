//package hello;
package cs551k.examples.rmi.mailbox.negotiation.singlebuyer;

import java.io.Serializable;

/**
 * @author Martin Kollingbaum
 * University of Aberdeen
 * 2018
 *
 */

public class Message implements Serializable
{
  private String receiver = null ;
  private String message  = null ;
  /**
   * 
   */
  public Message() {} 
  public Message(String receiver, String message )
  {
    this.receiver  = receiver ;
    this.message   = message ;
  }
  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }
  /**
   * @param message the message to set
   */
  public void setMessage(String message) {
    this.message = message;
  }
  public String getReceiver() {
    return receiver;
  }
  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

}
