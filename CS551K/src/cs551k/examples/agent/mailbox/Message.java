//we specify a simple "Message" class, that carries two parameters 
//- the name of a sender of the message, and the message itself. 

package cs551k.examples.agent.mailbox;

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
  public Message() {} 		//Constructor1
  public Message(String receiver, String message )		//Constructor2
  {
    this.receiver  = receiver ;
    this.message   = message ;
  }
  /**
   * @return the message
   */
  public String getMessage() {		//getter
    return message;
  }
  /**
   * @param message the message to set
   */
  public void setMessage(String message) {		//setter
    this.message = message;
  }
  public String getReceiver() {		
    return receiver;
  }
  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

}