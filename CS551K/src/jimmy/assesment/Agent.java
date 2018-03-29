package jimmy.assesment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Agent {
	private String myname = null;
	private String hostname = null;
	private int registryport = 0;
	private MailboxInterface mailbox = null;

	/**
	 * @throws NotBoundException
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Agent(String agentname, String hostname, int registryport)
			throws MalformedURLException, RemoteException, NotBoundException {
		this.myname = agentname;
		this.hostname = hostname;
		this.registryport = registryport;

		List Good = new ArrayList<>(Arrays.asList("salmon", 1000, 0.05, 500));
		List GoodsPrices = new ArrayList();
		GoodsPrices.addAll(Good);

		List Participants = new ArrayList<>(Arrays.asList("buyer1"));

		// Obtain the service reference from the RMI registry
		// listening at hostname:registryport.

		String regURL = "rmi://" + hostname + ":" + registryport + "/Mailbox";
		System.out.println("Looking up " + regURL);

		// ===============================================
		// lookup the remote service, it will return a reference to the stub:

		mailbox = (MailboxInterface) Naming.lookup(regURL);

		System.out.println("Agent " + myname + " connected to mailbox.");

		// new Thread ( receiver = new Receiver( myname, mailbox ) ).start(); ;

		try {

			// better_conversation() ;
			if (myname.equalsIgnoreCase("seller"))
				seller(GoodsPrices, Participants, myname);
			else
				buyer("seller", "item1");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private void buyer(String sellername, String itemname) throws IOException {
		// ===============================================
		// Console input:
		
		//TODO make a new goods thingy for buyers
		//TODO send the info here (Good, HighestPrice, Increment)
		//TODO Add teh goods list
		
		//TODO Add budget  = 100
		
		//TODO While budget > 0:
		
				//TODO while check if inbox not empty
						//TODO pop up messages and save them to myInbox
						
				//TODO for every message in inbox
						//TODO if message = type INFORM OF AUCTION
						//TODO Add it to list of auctions (AuctioneerID, Auction ID)
						//TODO make a list of acutions?
				
				
						//TODO if message type is call for proposal containing (AuctioneerID, my ID, AuctionID, Good, Price)
								//TODO IF the aucitoneerID, AUctionID not in the lest above
										//TODO send a "not understood" message cos we havent heard the inform of auction!
								
								//TODO ELSE 
										//TODO IF (Good, Highest Price, Increase) is in my GoodsPriceBox
												//TODO IF Price is =< Highest Price
														//TODO IF Price + (Price*Inrease) =< Highest Price
																//TODO then do Offer = Price+ (Price*Incremenet)
		
														//TODO Else send a proposal of the highest Offer
														//TODO send(propsal(HighestOffer))
												
												//TODO ELSE
												//return null because we outbid/not interested
		
						//TODO IF message type = reject
						//TODO return null nothing to do here
		
						//TODO If message type = accept
						//TODO Budget = Budget - offer		//update the budget
		
						//TODO If message type =  inform? (no bids)
						//TODO return null cos wut?
		
		//END
								
				//TODO else do (offer price and real p
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		Message message = null;
		double myprice = 0;
		double myfactor = 0.5;
		double mythreshold = 0.1;

		System.out.print("Start buyer, press return ");
		String command = in.readLine();

		// ask for price of item
		// message = new Message (Message.ASK_PRICE, myname, sellername, itemname, 0 ) ;
		mailbox.send(message);

		while (true) {
			if ((message = mailbox.receive(myname)) != null) {
				System.out.println("\n" + "   received from " + message.getSender() + ":");
				System.out.println("Item  = " + message.getItem());
				System.out.println("Price = " + message.getPrice());

				if (message.getMessageType() == Message.ACCEPT) {
					System.out.println("Seller accepts at price " + message.getPrice());
					break;
				} else if ((message.getPrice() - myprice) < mythreshold) {
					System.out.println("Buyer accepts at price " + message.getPrice());
					// message = new Message (Message.ACCEPT, myname, message.getSender(),
					// message.getItem(), message.getPrice());
					break;
				}

				myprice = myprice + ((message.getPrice() - myprice) * myfactor);

				// message = new Message ( Message.TELL_BID, myname, message.getSender(),
				// message.getItem(), myprice) ;
				mailbox.send(message);

			}
		}
	}

private void seller (List<Object> GoodsPrices, List<Object> Participants, String myname) throws IOException, InterruptedException
  {
    
	  
	  
    BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));
    Message        message = null ;
    String         item     =  (String) GoodsPrices.get(0);
    double         myprice     =  (int) GoodsPrices.get(1);
    double         myfactor    = (double) GoodsPrices.get(2);
    double         myreserve = (double) GoodsPrices.get(3);

    //TODO AucitonID=0 part
    int AuctionID = 1;
    //TODO for each item in GoodsPrice part
    //TODO init (Bidder,bid) = (null,null)
    
    for (int i = 0; i < Participants.size(); i++) {
    	message = new Message ( Message.INFORM_START_OF_AUCTION, myname, (String)Participants.get(i), AuctionID);		//do i really need this?
        mailbox.send(message);
        message = new Message ( Message.CALL_FOR_PROPOSAL, myname, (String)Participants.get(i), AuctionID, item, myprice );
        mailbox.send(message);
												  }
    boolean DutchAuctionON = true;
    
    while (DutchAuctionON) {			//i could make this a DO, WHILE, check while ducthauctionON at the bottom
    	System.out.print( "==============> Continue seller, press return "); String command = in.readLine();		//maybe i need to cancel this
    	Thread.sleep(4000);
      
      	if ((message = mailbox.receive(myname)) == null) 		{		//IF mailbox is empty after 4 seconds
      			myprice = myprice- (myprice*myfactor);
      			if (myprice < myreserve) {
      					DutchAuctionON = false;		//TODO i might need to return or something here, i can't just continue to the next instruction
      									 }
      			else for (int i = 0; i < Participants.size(); i++)   {
      			message = new Message ( Message.CALL_FOR_PROPOSAL, myname, (String)Participants.get(i), AuctionID, item, myprice );
      															}
      															}
      

      	List<Message> myInbox = new ArrayList<Message>();
      	while ( (message = mailbox.receive(myname)) != null) {
      			myInbox.add( message);
      			}
      	for (int i = 0; i < myInbox.size(); i++) {
      		if ((myInbox.get(i).getMessageType()) != Message.TELL_BID) {
      			myInbox.remove(i);			//possible problems becase im looping and removing over the same list
      		}
      	}
      	if (myInbox.size() > 1) {		//now if more than 1 messages then we go to english autionc yah?
      		DutchAuctionON = false;
      		boolean EnglishAuctionON = true;
      	}
      	else {
      		DutchAuctionON = false;
      		String cursedWinner = myInbox.get(0).getSender();
      		double cursedBid = myInbox.get(0).getPrice();
      	}

      		///ENGLISH AUCTION
      	//TODO init top bid
      	//TODO IF EnlgishAuctionON = true
		      	//TODO Send inform of auction again same Auction ID
		      	//TODO search though myInbox to find the highest offer
		      	//TODO send call for proposal(Good Highest Offer)
      			
      			//TODO While EnlgishAuctionON = true
      					//TODO pop up all the messages like before
      					//TODO Append them to the myInbox shit
      	
      					//TODO IF check if messages are empty for second round
      							//TODO ENlgishAuctionON = false
      							//TODO If topbid >= my Reserve	
      									//TODO send accept ma nibba (MyID, Bidder, AuctionID, Bid)
      									//TODO remove the succesful bidder from participants
      									//TODO send a request payment to the winner
      	
      							//TODO for all the other peeps left in participants
      							//TODO tell em there were no bids this round and thus
      							//TODO this nibba won, cue inform(this.nibba(winner))

      					//TODO ELSE loop though and sort out the decent bids like the ones that are legit and greater/equal to asking price
      	
      							//TODO Pick the highest Bidder and store them
      							//TODO For everyone else tell send a reject message
      							
      							//TODO Increase Price by the increment
      							//TODO IF Check if price increased is less than the highest bid
      									//TODO in which case use the highest bid = price
      							//TODO for everyone else send a new proposal with the new price
      	
    }
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Specify the security policy and set the security manager.
		System.setProperty("java.security.policy", "security.policy");
		System.setSecurityManager(new SecurityManager());

		String agentname = args[0];
		String hostname = args[1];
		int registryport = Integer.parseInt(args[2]);

		try {
			// instantiate an agent from this class

			new Agent(agentname, hostname, registryport);

		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}

	}

}