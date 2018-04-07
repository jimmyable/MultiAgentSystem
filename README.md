# Dutch and English Auctioning System
This java program will initiate Buyer and Seller agents which can communicate with eachother and carry out auctioning of fish, similair to a Fishmarket.

I assume you are using a standard bash terminal, and have Java installed(preferably version 8)
To run the program:

1. **Clone this repo**
2. **While inside the repo make sure you can executing bash scripts by setting** ```chmod +x Runme.sh```
3. **Now you can run the bash file, e.g.** ```./Runme.sh 3``` **which initiates 3 different Buyer Agents**

For test cases, you can input different number of agents as shown in step 3 and see how it affects the number of messages exchanged and price of goods sold. It is worthy to note that each buyer agent has randomly intilised price evaluations so many tests is preferable to get some averagr results.

## Screenshots:
### Mailbox
* Shows the number of mails recieved or sent
* Shows the types of messages exchanged
* Shows the Sender(deposits) and Receiver of messages

Message types are as follows:
1. NOT_UNDERSTOOD (type0)
2. END (type2)
3. TELl_BID (type3)
4. ACCEPT (type4)
5. INFORM_START_OF_AUCTION (type6)
6. CALL_FOR_PROPOSAL (type7)

![Alt text](screenshots/Mailbox.png?raw=true "Mailbox")
---
### Seller
* Some print statements informing messages statements,
* Auction ID is reported,
* Item names and prices are reported
* Type of Auction is displayed
* Profits are calculated and displayed

![Alt text](screenshots/Seller.png?raw=true "Seller")

---
### Buyers
* Some print messages indicating the response of the Agents
* Displays the offer and item when a bid is placed
* Notification of winning item and price
* Money saved comapring with reserve price is also shown

![Alt text](screenshots/Buyer1.png?raw=true "Buyer_1")
![Alt text](screenshots/Buyer2.png?raw=true "Buyer_2")
![Alt text](screenshots/Buyer3.png?raw=true "Buyer_3")
