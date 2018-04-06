package jimmy.assesment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class Agent {
	private String myname = null;
	private String hostname = null;
	private int registryport = 0;
	private MailboxInterface mailbox = null;
	NumberFormat df = NumberFormat.getCurrencyInstance();

	public Agent(String agentname, String hostname, int registryport, int numAgents)
			throws MalformedURLException, RemoteException, NotBoundException, InterruptedException {

		this.myname = agentname;
		this.hostname = hostname;
		this.registryport = registryport;

		/////////////////////////////////
		Map<String, List<Double>> GoodsPricesSeller = new HashMap<String, List<Double>>();
		GoodsPricesSeller.put("Salmon", Arrays.asList(1000.0, 0.5, 500.0));
		GoodsPricesSeller.put("Tuna", Arrays.asList(900.0, 0.06, 300.0));
		GoodsPricesSeller.put("Haddock", Arrays.asList(950.0, 0.07, 310.0));

		List<String> Participants = new ArrayList<>();
		for (int i = 1; i < numAgents + 1; i++) {
			Participants.add("Buyer_" + i);
		}

		////////////////////////////////////////
		Random r = new Random();
		///////////////////////////////////
		Map<String, List<Double>> GoodsPricesBuyer = new HashMap<String, List<Double>>();
		GoodsPricesBuyer.put("Salmon",
				Arrays.asList(((500.0 + (700.0 - 500.0) * r.nextDouble())), (0.01 + (0.5 - 0.01) * r.nextDouble())));
		GoodsPricesBuyer.put("Tuna",
				Arrays.asList(((300.0 + (700.0 - 300.0) * r.nextDouble())), (0.01 + (0.5 - 0.01) * r.nextDouble())));
		GoodsPricesBuyer.put("Haddock",
				Arrays.asList(((350.0 + (700.0 - 350.0) * r.nextDouble())), (0.01 + (0.5 - 0.01) * r.nextDouble())));

		///////////////////////////////////
		Double Budget = (double) 1000;
		//////////////////////////////////

		String regURL = "rmi://" + hostname + ":" + registryport + "/Mailbox";
		System.out.println("Looking up " + regURL);
		mailbox = (MailboxInterface) Naming.lookup(regURL);
		System.out.println("Agent " + myname + " connected to the Auction.");

		try {

			if (myname.equalsIgnoreCase("Seller"))
				seller(GoodsPricesSeller, Participants, myname);
			else
				buyer(GoodsPricesBuyer, Budget, myname);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private void buyer(Map<String, List<Double>> GoodsPrices, Double Budget, String myname)
			throws IOException, InterruptedException {
		// ===============================================

		Message message = null;
		System.out.println("I am " + myname + " , looking to buy fish at a fair price.");
		Map<String, List<Double>> GoodsPricesCopy = GoodsPrices.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> new ArrayList(e.getValue())));
		Map<String, Integer> currentAuctions = new HashMap<String, Integer>();
		Map<String, Double> Winnings = new HashMap<String, Double>();
		while (GoodsPrices.size() > 0) {
			while (Budget > 0) {
				Thread.sleep(100);

				while ((message = mailbox.receive(myname)) != null) {

					if (message.getMessageType() == Message.INFORM_START_OF_AUCTION) {
						currentAuctions.put(message.getSender(), message.getAuctionID());
						System.out.println("I look forward to this Auction {" + message.getAuctionID() + "}");
					} else if ((message.getMessageType() == Message.CALL_FOR_PROPOSAL)
							&& (currentAuctions.containsValue(message.getAuctionID()))
							&& (GoodsPrices.containsKey(message.getItem()))) {
						if (message.getPrice() <= GoodsPrices.get(message.getItem()).get(0)) {
							Double newoffer = (message.getPrice() * (1 + GoodsPrices.get(message.getItem()).get(1)));
							if (newoffer <= GoodsPrices.get(message.getItem()).get(0)) {
								message = new Message(Message.TELL_BID, myname, message.getSender(),
										message.getAuctionID(), message.getItem(), newoffer);
								mailbox.send(message);
								System.out.println("I would like to bid (" + df.format(newoffer) + ") for the ["
										+ message.getItem() + "]");
							} else {
								message = new Message(Message.TELL_BID, myname, message.getSender(),
										message.getAuctionID(), message.getItem(),
										GoodsPrices.get(message.getItem()).get(0));
								mailbox.send(message);
								System.out.println(
										"I would like to bid (" + df.format(GoodsPrices.get(message.getItem()).get(0))
												+ ") for the [" + message.getItem() + "]");
							}
						} else {
							System.out.println("I cannot pay this much, sorry!");
						}
					} else if (message.getMessageType() == Message.END) {
						System.out.println("10/10 would auction again");
						GoodsPrices.remove(message.getItem());
						System.out.println(GoodsPrices.size());

					} else if (message.getMessageType() == Message.ACCEPT) {
						Winnings.put(message.getItem(), message.getPrice());
						System.out.println("I won [" + message.getItem() + "] for the price of ("
								+ df.format(message.getPrice()) + ")");
						Budget = Budget - (message.getPrice());

					} else {
						System.out.println(message.getSender());
						message = new Message(Message.NOT_UNDERSTOOD, myname, message.getSender());
						mailbox.send(message);
						System.out.println("I was never informed of this item!");
						System.out.println(message.getMessageType() + message.getSender());
					}
				}
				if (((message = mailbox.receive(myname)) == null) && GoodsPrices.size() <= 0) {
					break;
				}

			}

		}
		if (Winnings.size() > 0) {
			for (Map.Entry<String, Double> entry : Winnings.entrySet()) {
				String reservedItem = entry.getKey();
				Double reservedPrice = GoodsPricesCopy.get(reservedItem).get(0);
				Double actualPrice = entry.getValue();
				System.out.println("I saved " + df.format(reservedPrice - actualPrice) + " for " + reservedItem);
			}
		} else {
			System.out.println("I didn't win anything");
		}
		System.out.println("The End!");
	}

	private void seller(Map<String, List<Double>> GoodsPrices, List<String> Participants, String myname)
			throws IOException, InterruptedException {

		System.out.println("Hey Vsauce" + myname + " here");
		Message message = null;
		int AuctionID = 0;
		Map<String, Double> Sold = new HashMap<String, Double>();

		for (Map.Entry<String, List<Double>> entry : GoodsPrices.entrySet()) {
			AuctionID += 1;

			String item = entry.getKey(); 
			List<Double> prices = entry.getValue();

			double myprice = (double) prices.get(0);
			double myfactor = (double) prices.get(1);
			double myreserve = (double) prices.get(2);

			System.out.println("This is the start of the DUTCH auction for [" + item + "]");
			System.out.println("Calling in proposals for Auction: {" + AuctionID + "} for the price of ("
					+ df.format(myprice) + ")");
			for (int i = 0; i < Participants.size(); i++) {
				message = new Message(Message.INFORM_START_OF_AUCTION, myname, Participants.get(i), AuctionID);
				mailbox.send(message);
				message = new Message(Message.CALL_FOR_PROPOSAL, myname, Participants.get(i), AuctionID, item, myprice);
				mailbox.send(message);
			}
			boolean DutchAuctionON = true;
			boolean EnglishAuctionON = false;
			Message HighestOffer = null;
			List<Message> myInbox = new ArrayList<Message>();

			do {
				Thread.sleep(500);
				if ((message = mailbox.receive(myname)) == null) { 
					myprice = myprice - (myprice * myfactor);
					if (myprice < myreserve) {

						DutchAuctionON = false;
						for (int i = 0; i < Participants.size(); i++) {
							message = new Message(Message.END, myname, (String) Participants.get(i), AuctionID, item,
									myprice);
							mailbox.send(message);
						}
						;

					} else {
						System.out.println("Calling in proposals for Auction: {" + AuctionID + "} for the price of ("
								+ df.format(myprice) + ")");
						for (int i = 0; i < Participants.size(); i++) {
							message = new Message(Message.CALL_FOR_PROPOSAL, myname, Participants.get(i), AuctionID,
									item, myprice);
							mailbox.send(message);
						}
					}
				} else {
					while ((message = mailbox.receive(myname)) != null) {
						myInbox.add(message);
					}
					for (int i = 0; i < myInbox.size(); i++) {
						if ((myInbox.get(i).getMessageType()) != Message.TELL_BID) {
							myInbox.remove(i); 
						}
					}
					if (myInbox.size() == 0) { 
						myprice = myprice - (myprice * myfactor);
						if (myprice < myreserve) {
							DutchAuctionON = false; 
						} else {
							System.out.println("Calling in proposals for Auction: {" + AuctionID
									+ "} for the price of (" + df.format(myprice) + ")");

							for (int i = 0; i < Participants.size(); i++) {
								message = new Message(Message.CALL_FOR_PROPOSAL, myname, (String) Participants.get(i),
										AuctionID, item, myprice);
								mailbox.send(message);
							}
						}
					}

					if (myInbox.size() > 1) {
						Collections.sort(myInbox);
						DutchAuctionON = false;
						EnglishAuctionON = true;
						HighestOffer = myInbox.get(0);
						
					}
					if ((myInbox.size() == 1) && (myInbox.get(0).getPrice() >= myreserve)) {
						DutchAuctionON = false;
						String cursedWinner = myInbox.get(0).getSender();
						double cursedBid = myInbox.get(0).getPrice();
						System.out.println("Item [" + item + "] sold to " + cursedWinner + " for the price of ("
								+ df.format(cursedBid) + ")");
						Sold.put(item, cursedBid);
						message = new Message(Message.ACCEPT, myname, (String) cursedWinner, AuctionID, item,
								cursedBid);
						mailbox.send(message); 
						for (int i = 0; i < Participants.size(); i++) {
							message = new Message(Message.END, myname, (String) Participants.get(i), AuctionID, item,
									myprice);
							mailbox.send(message);
						}
						
					}
				}
				

			} while (DutchAuctionON);

			if (EnglishAuctionON) { 
				System.out.println("This is the start of the ENGLISH auction for [" + item + "]");
				System.out.println("Calling in proposals for Auction: {" + AuctionID + "} for the price of ("
						+ df.format(HighestOffer.getPrice()) + ")");
				for (int i = 0; i < Participants.size(); i++) {
					message = new Message(Message.CALL_FOR_PROPOSAL, myname, (String) Participants.get(i), AuctionID,
							item, HighestOffer.getPrice());
					mailbox.send(message);
					
				}
			}
			Thread.sleep(500);
			while (EnglishAuctionON) {
				Thread.sleep(500);
				if ((message = mailbox.receive(myname)) == null) {
					EnglishAuctionON = false;
					System.out.println("Item [" + item + "] sold to " + HighestOffer.getSender() + " for the price of ("
							+ df.format(HighestOffer.getPrice()) + ")");
					Sold.put(item, HighestOffer.getPrice());
					message = new Message(Message.ACCEPT, myname, (String) HighestOffer.getSender(), AuctionID, item,
							HighestOffer.getPrice());
					mailbox.send(message); 
					for (int i = 0; i < Participants.size(); i++) {
						message = new Message(Message.END, myname, (String) Participants.get(i), AuctionID, item,
								myprice);
						mailbox.send(message);
					}
				} else {
					while ((message = mailbox.receive(myname)) != null) {
						myInbox.add(message); 
					}

					for (int i = 0; i < myInbox.size(); i++) {
						if ((myInbox.get(i).getMessageType()) != Message.TELL_BID) {
							myInbox.remove(i); 
						}
					}
					Collections.sort(myInbox);
					HighestOffer = myInbox.get(0);

					myprice = myprice + (myprice * (myfactor / 5));
					if (myprice < myreserve) {
						EnglishAuctionON = false;
						System.out.println("Item not sold for the low price");
						for (int i = 0; i < Participants.size(); i++) {
							message = new Message(Message.END, myname, (String) Participants.get(i), AuctionID, item,
									myprice);
							mailbox.send(message);
						}
					}
					if (myprice < HighestOffer.getPrice()) {
						myprice = HighestOffer.getPrice();
					}
					System.out.println("Calling in proposals for Auction: {" + AuctionID + "} for the price of ("
							+ df.format(myprice) + ")");
					for (int i = 0; i < Participants.size(); i++) {
						message = new Message(Message.CALL_FOR_PROPOSAL, myname, (String) Participants.get(i),
								AuctionID, item, myprice);
						mailbox.send(message);
					}
				}

			}
		}

		
		if (Sold.size() > 0) {
			for (Map.Entry<String, Double> entry : Sold.entrySet()) {
				String reservedItem = entry.getKey();
				Double reservedPrice = GoodsPrices.get(reservedItem).get(2);
				Double actualPrice = entry.getValue();
				System.out.println(
						"I made a profit of " + df.format(actualPrice - reservedPrice) + " for " + reservedItem);
			}
		} else {
			System.out.println("I didn't win anything");
		}
		System.out.println("The End!");

	}

	public static void main(String[] args) throws InterruptedException {


		System.setProperty("java.security.policy", "security.policy");
		System.setSecurityManager(new SecurityManager());

		String agentname = args[0];
		String hostname = args[1];
		int registryport = Integer.parseInt(args[2]);
		int numAgents = Integer.parseInt(args[3]);

		try {
			// Start a new agent here, auctioneer or participant

			new Agent(agentname, hostname, registryport, numAgents);

		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}

	}

}