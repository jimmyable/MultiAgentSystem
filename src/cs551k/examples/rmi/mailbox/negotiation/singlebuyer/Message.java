//package hello
package cs551k.examples.rmi.mailbox.negotiation.singlebuyer; //declare the package

import java.io.Serializable;

/**
 * @author Martin Kollingbaum
 * University of Aberdeen
 * 2018
 *
 */

public class Message implements Serializable		//do public class
{
  private String receiver = null ;					//initilaise reciever
  private String message  = null ;					//initiliase message
  /**
   * 
   */
  public Message() {} 								//empty message constructor
  public Message(String receiver, String message )	//message constructor that takes in message and reciever
  {
    this.receiver  = receiver ;						//if this is how the object is called then this.that
    this.message   = message ;
  }
  
  /**
   * @return the message
   */
  public String getMessage() {						//call this method to display the actual "message"
    return message;
  }
  /**
   * @param message the message to set
   */
  public void setMessage(String message) {		//call this method to set a "message", requires a message to be fed in
    this.message = message;
  }
  public String getReceiver() {					//call this method to get the "reciever" name
    return receiver;
  }
  public void setReceiver(String receiver) {	//call this method to set the "reciever" name
    this.receiver = receiver;
  }
  

}
