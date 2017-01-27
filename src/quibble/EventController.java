package quibble;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * The EventController class handles all methods involving event management.
 * This class provides the functionality to create or delete events.
 * The EventController extends the BaseController.
 * 
 * @author 		Gabrielle Quilliam
 * @author 		Hassan Haq
 * @version 	1.0
 * @since 		2015-10-20
 * @see 		BaseController
 */
public class EventController extends BaseController {

	/**
	 * This is a privileged transaction (only accessible for administrative users).
	 * Creates an event.
	 * The create method creates a new create transaction and gives a success message to the user.
	 * The transaction is created by calling the GetTransactionDetails method in the BaseController.
	 * 
	 * @param session			The current session data.
	 * @param r					The buffered reader to get user input.
	 * @throws IOException		Handles invalid user input.
	 * @see BaseController#GetTransactionDetails(SessionStorage, BufferedReader, boolean, boolean, String)
	 */
	public static void Create(SessionStorage session, BufferedReader r) throws IOException{
		// Call the base controller to get the transaction input
		Transaction transaction = GetTransactionDetails(session, r, true, true, "03");
		if(transaction != null){
			System.out.println(transaction.eventName + " has been created successfully.\n\n");
		}
	}
	
	/**
	 * This is a privileged transaction (only accessible for administrative users).
	 * Deletes an event.
	 * The delete method creates a new delete transaction and gives a success message to the user.
	 * The transaction is created by calling the GetTransactionDetails method in the BaseController.
	 * 
	 * @param session			The current session data.
	 * @param r					The buffered reader to get user input.
	 * @throws IOException		Handles invalid user input.
	 * @see BaseController#GetTransactionDetails(SessionStorage, BufferedReader, boolean, boolean, String)
	 */
	public static void Delete(SessionStorage session, BufferedReader r) throws IOException{
		// TODO: add confirmation message (y/n)
		// Call the base controller to get the transaction input
		Transaction transaction = GetTransactionDetails(session, r, false, false, "05");
		if(transaction != null){
			// Remove from the events list so that no further transactions can be conducted
			session.Events.remove(transaction.eventName);
			System.out.println(transaction.eventName + " has been deleted successfully.\n\n");
		}
	}


}
