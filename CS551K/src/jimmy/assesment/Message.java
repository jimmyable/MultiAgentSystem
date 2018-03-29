package jimmy.assesment;

import java.io.Serializable;



public class Message implements Serializable
{

  private static final long serialVersionUID = 1;
  
  static public final int NO_MESSAGE = 0 ;
  static public final int ASK_PRICE  = 1 ;
  static public final int TELL_PRICE = 2 ;
  static public final int TELL_BID   = 3 ;
  static public final int ACCEPT     = 4 ;
  static public final int REJECT     = 5 ;
  static public final int INFORM_START_OF_AUCTION     = 6 ;
  static public final int CALL_FOR_PROPOSAL     = 7 ;
  
  private int    messageType = NO_MESSAGE ;
  
  private String sender   = null ;
  private String receiver = null ;
  private String content  = null ;
  
  // message content:
  private String item     = null ;
    private double price    = 0 ;

	private int auctionID;

  public Message() {} 
  public Message(String receiver, String message )
  {
    this.receiver  = receiver ;
    this.content   = message ;
  }
  public Message(int messageType, String sender, String receiver, int auctionID,String item, double price)
  {
    this.setMessageType(messageType) ;
    this.sender      = sender ;
    this.receiver    = receiver ;
    this.item        = item ;
    this.price       = price ;
    
  }
  public Message(int messageType, String sender, String receiver, int auctionID) {
	  this.setMessageType(messageType) ;
	  this.sender      = sender ;
	  this.receiver    = receiver ;
	  this.auctionID         = auctionID ;
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