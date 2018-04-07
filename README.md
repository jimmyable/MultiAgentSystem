# Dutch and English Auctioning System
This java program will initaite Buyer and Seller agents which can communicate and carry out auctioning of fish, like in a Fishmarket.

I assume you are using a standard bash terminal, and have Java installed(preferably version 8)
To run the program:

1. Clone the repo
2. While inside the repo make sure you can executing bash scripts by setting ```chmod +x Runme.sh```
3. Now you can run the bash file, e.g. ```./Runme.sh 3``` which initiates 3 different Buyer Agents

For test cases, one can input different number for the number of agents as shown in step 3 and see how it affects the number of messages exchanged and price of goods sold. It is worthy to note that each buyer agent has randomly intilised price evaluations so many tests is preferable to get an average.

## Screenshots:
**Mailbox**
>Shows the number of mails recieved or sent
>Shows the types of messages exchanged
>Shows the Sender(deposits) and Receiver of messages

Message types are as follows:
- NOT_UNDERSTOOD (type0)
- END (type2)
- TELl_BID (type3)
- ACCEPT (type4)
- INFORM_START_OF_AUCTIOB (type6)
- CALL_FOR_PROPOSAL (type7)

![Alt text](screenshots/Mailbox.png?raw=true "Mailbox")

**Seller**
>Some print statements informing messages statements
>Auction ID is reported
>Item names and prices are reported
>Type of Auction is displayed
>Profits are calculated and displayed

Seller.pic


**Buyers**
>Some print messages indicating the response of the Agents
>Displays the offer and item when a bid is placed
>Notification of winning item and price
>Money saved comapring with reserve price is also shown
