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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Agent {
	private String myname = null;
	private String hostname = null;
	private int registryport = 0;
	private MailboxInterface mailbox = null;

	
	
	public Agent(String agentname, String hostname, int registryport)
			throws MalformedURLException, RemoteException, NotBoundException, InterruptedException {
		
		this.myname = agentname;
		this.hostname = hostname;
		this.registryport = registryport;
		
		/////////////////////////////////
		List AA = new ArrayList<>(Arrays.asList("salmon", 1000, 0.05, 500.0));
		List AB = new ArrayList<>(Arrays.asList("mackrel", 900, 0.06, 300.0));
		List GoodsPricesSeller = new ArrayList();
		GoodsPricesSeller.addAll(AA);
		GoodsPricesSeller.addAll(AB);
		///////////////////////////////////////
		List Participants = new ArrayList<>(Arrays.asList("buyer"));
		Participants.add("buyer2");
		////////////////////////////////////////
		///////////////////////////////////
		List BA = new ArrayList<>(Arrays.asList("salmon", 900.0, 0.1));
		List BB = new ArrayList<>(Arrays.asList("mackerel",950.0, 0.1));
		List GoodsPricesBuyer = new ArrayList();
		GoodsPricesBuyer.addAll(BA);
		GoodsPricesBuyer.addAll(BB);
		///////////////////////////////////
		Double Budget1 = (double) 1000;
		//////////////////////////////////


		String regURL = "rmi://" + hostname + ":" + registryport + "/Mailbox";
		System.out.println("Looking up " + regURL);
		mailbox = (MailboxInterface) Naming.lookup(regURL);
		System.out.println("Agent " + myname + " connected to mailbox.");

		try {

			if (myname.equalsIgnoreCase("seller"))
				seller(GoodsPricesSeller, Participants, myname);
			else
				buyer(GoodsPricesBuyer, Budget1, myname);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private void buyer(List GoodsPrices, Double Budget, String myname) throws IOException, InterruptedException {
		// ===============================================

		Message message = null;

		String Good = (String) GoodsPrices.get(0);
		Double myreserve = (Double) GoodsPrices.get(1);
		Double myfactor = (Double) GoodsPrices.get(2);

		Double myBudget = (Double) Budget;

		Map<String, Integer> currentAuctions = new HashMap<String, Integer>();

		while (Budget > 0) {
			Thread.sleep(1000);
			List<Message> myInbox = new ArrayList<Message>();

			while ((message = mailbox.receive(myname)) != null) {
				myInbox.add(message);
			}
			for (int i = 0; i < myInbox.size(); i++) {
				if (myInbox.get(i).getMessageType() == Message.INFORM_START_OF_AUCTION) {
					currentAuctions.put(myInbox.get(i).getSender(), myInbox.get(i).getAuctionID());
				}
				if (myInbox.get(i).getMessageType() == Message.CALL_FOR_PROPOSAL) {
					if (currentAuctions.containsValue(myInbox.get(i).getAuctionID)) {
						if (GoodsPrices.contains(myInbox.get(i).getItem())) {
							if (myInbox.get(i).getPrice() <= myreserve) {
								Double newoffer = (myInbox.get(i).getPrice() * (1 + myfactor));
								if (newoffer <= myreserve) {
									message = new Message(Message.TELL_BID, myname, myInbox.get(i).getSender(),
											myInbox.get(i).getAuctionID(), newoffer);
									mailbox.send(message);
								} else {
									message = new Message(Message.TELL_BID, myname, myInbox.get(i).getSender(),
											myInbox.get(i).getAuctionID(), myreserve);
									mailbox.send(message);
								}
							} else {
								// we have been outbid
							}
						}
					} else {
						message = new Message(Message.NOT_UNDERSTOOD, myname, myInbox.get(i).getSender(),
								myInbox.get(i).getAuctionID());
						mailbox.send(message);
					}
				}
				if (myInbox.get(i).getMessageType() == Message.REJECT) {
					// do fuck all
				}
				if (myInbox.get(i).getMessageType() == Message.ACCEPT) {
					Budget = Budget - (myInbox.get(i).getPrice());
				} else {
					myInbox.clear();
				}

			}

		}
	}

	private void seller(List<Object> GoodsPrices, List<Object> Participants, String myname)
			throws IOException, InterruptedException {
		
		System.out.println("Hey VSAUCE "+ myname+ " here");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		Message message = null;
		String item = (String) GoodsPrices.get(0);
		double myprice = (int) GoodsPrices.get(1);
		double myfactor = (double) GoodsPrices.get(2);
		double myreserve = (double) GoodsPrices.get(3);

		// TODO AucitonID=0 part
		int AuctionID = 1;
		// TODO for each item in GoodsPrice part
		// TODO init (Bidder,bid) = (null,null)

		for (int i = 0; i < Participants.size(); i++) {
			message = new Message(Message.INFORM_START_OF_AUCTION, myname, (String) Participants.get(i), AuctionID); // do
																														// i
																														// really
																														// need
																														// this?
			mailbox.send(message);
			message = new Message(Message.CALL_FOR_PROPOSAL, myname, (String) Participants.get(i), AuctionID, item,
					myprice);
			mailbox.send(message);
		}
		boolean DutchAuctionON = true;
		boolean EnglishAuctionON = false;
		Double HighestOffer = (double) 0;
		List<Message> myInbox = new ArrayList<Message>();

		do { // i could make this a DO, WHILE, check while ducthauctionON at the bottom
			//System.out.print("==============> Continue seller, press return ");
			//String command = in.readLine(); // maybe i need to cancel this
			Thread.sleep(4000);

			if ((message = mailbox.receive(myname)) == null) { // IF mailbox is empty after 4 seconds
				myprice = myprice - (myprice * myfactor);
				if (myprice < myreserve) {
					DutchAuctionON = false; // TODO i might need to return or something here, i can't just continue to
											// the next instruction
				} else
					for (int i = 0; i < Participants.size(); i++) {
						message = new Message(Message.CALL_FOR_PROPOSAL, myname, (String) Participants.get(i),
								AuctionID, item, myprice);
						mailbox.send(message);
						
					}
			}

			while ((message = mailbox.receive(myname)) != null) {
				myInbox.add(message);
			}
			for (int i = 0; i < myInbox.size(); i++) {
				if ((myInbox.get(i).getMessageType()) != Message.TELL_BID) {
					myInbox.remove(i); // possible problems becase im looping and removing over the same list
				}
			}
			if (myInbox.size() > 1) { // now if more than 1 messages then we go to english autionc yah?
				DutchAuctionON = false;
				EnglishAuctionON = true;
				ArrayList<Double> iter = new ArrayList<Double>();
				for (int i = 0; i < myInbox.size(); i++) {
					iter.add(myInbox.get(i).getPrice());
				}
				Collections.sort(iter);
				HighestOffer = iter.get(0);
			} else {
				DutchAuctionON = false;
				String cursedWinner = myInbox.get(0).getSender();
				double cursedBid = myInbox.get(0).getPrice();
				message = new Message(Message.ACCEPT, myname, (String) cursedWinner, AuctionID, cursedBid);// TODO Send
				mailbox.send(message);																							// an accept
																											// message
																											// to the
																											// rest
				// message = new Message (Message.REQPayment, myname, (String) cursedWinner,
				// AuctionID, cursedBid );
				// List loosers = ((Object) Participants).clone();
				// loosers.remove(cursedWinner); //the list of loosers

				// TODO Send messages informing the loosers of no bids
				// TODO Send messages to loosers annoucing the winner
			}

		} 
		while (DutchAuctionON);
		
		if (EnglishAuctionON = true) { // and check if highestoffer =/= 0
			for (int i = 0; i < Participants.size(); i++) {
				message = new Message(Message.INFORM_START_OF_AUCTION, myname, (String) Participants.get(i), AuctionID); // do
																															// i
																															// really
																															// need
																															// this?
				mailbox.send(message);
				message = new Message(Message.CALL_FOR_PROPOSAL, myname, (String) Participants.get(i), AuctionID, item,
						HighestOffer);
				mailbox.send(message);
			}
		}

		while (EnglishAuctionON = true) {
			Thread.sleep(4000);
			if ((message = mailbox.receive(myname)) == null) {
				EnglishAuctionON = false;
				// TODO accept the previous bid
			}
			while ((message = mailbox.receive(myname)) != null) {
				myInbox.add(message);
			}

			for (int i = (myInbox.size() - 1); i > myInbox.size(); i--) { // this looks a fuck up, use descending order
																			// to not miss anything
				if ((myInbox.get(i).getMessageType()) != Message.TELL_BID) {
					myInbox.remove(i); // possible problems becase im looping and removing over the same list
				}
			}
			Map<String, Double> hm = new HashMap<String, Double>();
			for (int i = 0; i < myInbox.size(); i++) {
				hm.put(myInbox.get(i).getSender(), myInbox.get(i).getPrice());
			}
			List values = new ArrayList(hm.values());
			Collections.sort(values);
			HighestOffer = (Double) values.get(0);
			// TODO reject other peoples offers

			myprice = myprice + (myprice * (myfactor / 2));
			if (myprice < myreserve) { // will this ever happen tho?
				EnglishAuctionON = false;
			}
			if (myprice < HighestOffer) {
				myprice = HighestOffer;
			}
			for (int i = 0; i < Participants.size(); i++) {
				message = new Message(Message.CALL_FOR_PROPOSAL, myname, (String) Participants.get(i), AuctionID, item,
						myprice);
			}

		}

	}

	
	public static void main(String[] args) throws InterruptedException {

		// Specify the security policy and set the security manager.
		System.setProperty("java.security.policy", "security.policy");
		System.setSecurityManager(new SecurityManager());

		String agentname = args[0];
		String hostname = args[1];
		int registryport = Integer.parseInt(args[2]);

		try {
			//Start a new agent here, auctioneer or participant

			new Agent(agentname, hostname, registryport);

		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}

	}

}