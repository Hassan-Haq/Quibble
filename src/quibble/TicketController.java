package quibble;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * The TicketController class handles all methods involving ticket management.
 * This class provides the functionality to add, sell or return event tickets.
 * The TicketController extends the BaseController.
 * 
 * @author 		Gabrielle Quilliam
 * @author 		Hassan Haq
 * @version 	1.0
 * @since 		2015-10-20
 * @see 		BaseController
 */
public class TicketController extends BaseController{
	
	/**
	 * This is a privileged transaction (only accessible for administrative users).
	 * Adds tickets to an event.
	 * The add method creates a new add transaction and gives a success message to the user.
	 * The transaction is created by calling the GetTransactionDetails method in the BaseController.
	 * 
	 * @param session			The current session data.
	 * @param r					The buffered reader to get user input.
	 * @throws IOException		Handles invalid user input.
	 * @see BaseController#GetTransactionDetails(SessionStorage, BufferedReader, boolean, boolean, String)
	 */
	public static void Add(SessionStorage session, BufferedReader r) throws IOException {
		// Call the base controller to get the transaction input
		Transaction transaction = GetTransactionDetails(session, r, true, false, "04");
		if(transaction != null){
			System.out.println("Added " + transaction.numTickets + " tickets to " + transaction.eventName + "\n\n");
		}
	}

	/**
	 * Sell tickets to an event.
	 * The sell method creates a new sell transaction and gives a success message to the user.
	 * The transaction is created by calling the GetTransactionDetails method in the BaseController.
	 * The number of remaining tickets in the Events table in the SessionStorage is then updated to reflect the tickets sold.
	 * 
	 * @param session			The current session data.
	 * @param r					The buffered reader to get user input.
	 * @throws IOException		Handles invalid user input.
	 * @see BaseController#GetTransactionDetails(SessionStorage, BufferedReader, boolean, boolean, String)
	 * @see SessionStorage
	 */
	public static void Sell(SessionStorage session, BufferedReader r) throws IOException {
		// Call the base controller to get the transaction input
		Transaction transaction = GetTransactionDetails(session, r, true, false, "01");
		if(transaction != null){
			int numSold = Integer.parseInt(transaction.numTickets);
			int numRemaining = Integer.parseInt(session.Events.get(transaction.eventName).numTickets);
			session.Events.get(transaction.eventName).numTickets = Integer.toString(numRemaining - numSold);
			System.out.println("Sold " + transaction.numTickets + " tickets for " + transaction.eventName + "\n\n");
		}
	}
	
	/**
	 * Returns tickets to an event.
	 * The return method creates a new return transaction and gives a success message to the user.
	 * The transaction is created by calling the GetTransactionDetails method in the BaseController.
	 * The number of remaining tickets in the Events table in the SessionStorage is then updated to reflect the tickets returned.
	 * 
	 * @param session			The current session data.
	 * @param r					The buffered reader to get user input.
	 * @throws IOException		Handles invalid user input.
	 * @see BaseController#GetTransactionDetails(SessionStorage, BufferedReader, boolean, boolean, String)
	 * @see SessionStorage
	 */
	public static void Return(SessionStorage session, BufferedReader r) throws IOException {
		// Call the base controller to get the transaction input
		Transaction transaction = GetTransactionDetails(session, r, true, false, "02");
		int numReturned = Integer.parseInt(transaction.numTickets);
		int numRemaining = Integer.parseInt(session.Events.get(transaction.eventName).numTickets);
		session.Events.get(transaction.eventName).numTickets = Integer.toString(numRemaining + numReturned);
		System.out.println("Returned " + transaction.numTickets + " tickets for " + transaction.eventName + "\n\n");
	}
}
