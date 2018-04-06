package cs551k.examples.agent.mailbox.simplebuyer;

import java.io.Serializable;

/**
 * @author Martin Kollingbaum
 * University of Aberdeen
 * 2018
 *
 */

public class Message implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1;
  
  static public final int NO_MESSAGE = 0 ;
  static public final int ASK_PRICE  = 1 ;
  static public final int TELL_PRICE = 2 ;
  static public final int TELL_BID   = 3 ;
  static public final int ACCEPT     = 4 ;
  static public final int REJECT     = 5 ;
  
  private int    messageType = NO_MESSAGE ;
  
  private String sender   = null ;
  private String receiver = null ;
  private String content  = null ;
  
  // message content:
  private String item     = null ;
    private double price    = 0 ;
    //
  /**
   * 
   */
  public Message() {} 
  public Message(String receiver, String message )
  {
    this.receiver  = receiver ;
    this.content   = message ;
  }
  public Message(int messageType, String sender, String receiver, String item, double price)
  {
    this.setMessageType(messageType) ;
    this.sender      = sender ;
    this.receiver    = receiver ;
    this.item        = item ;
    this.price       = price ;
    
  }
  /**
   * @return the message
   */
  public String getContent() {
    return content;
  }
  /**
   * @param message the message to set
   */
  public void setContent(String content) {
    this.content = content;
  }
  public String getReceiver() {
    return receiver;
  }
  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }
  public int getMessageType() {
    return messageType;
  }
  public void setMessageType(int messageType) {
    this.messageType = messageType;
  }
  public String getSender() {
    return sender;
  }
  public void setSender(String sender) {
    this.sender = sender;
  }
  public String getItem() {
    return item;
  }
  public void setItem(String item) {
    this.item = item;
  }
  public double getPrice() {
    return price;
  }
  public void setPrice(double price) {
    this.price = price;
  }

}