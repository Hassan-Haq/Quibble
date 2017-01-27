package quibble;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

/**
 * The BaseController handles all transactions and validation of user input.
 * Both the EventController and TicketController inherit methods from this class.
 * The BaseController aims to prompt the user for input throughout all transactions, and validate this input accordingly.
 * 
 * @author 		Gabrielle Quilliam
 * @author 		Hassan Haq
 * @version 	1.0
 * @since 		2015-10-20
 */
public class BaseController {
	
	public static TextValuePair result = new TextValuePair(); // Stores the validation results
	
	/**
	 * Template to get all transaction details from the user (event name, number of tickets and date).
	 * Once the details are obtained and validated, the transaction is added to the session data's transaction list, to be processed by the daily event transaction file.
	 * 
	 * @param session				The current session data.
	 * @param r						The buffered reader to get user input.
	 * @param hasNumTickets			A flag indicating the transaction requires to get the number of tickets from the user.
	 * @param hasDate				A flag indicating the transaction requires to get the event date from the user.
	 * @param code					The transaction code. (01 - sell, 02 - return, 03 - create, 04 - add, 05 - delete)
	 * @return 						<code>Transaction</code> containing the event name, number of tickets, event date and transaction code.
	 * @throws IOException			Handles invalid user input.
	 * @see Transaction
	 */
	public static Transaction GetTransactionDetails(SessionStorage session, BufferedReader r, boolean hasNumTickets, boolean hasDate, String code) throws IOException{
		Transaction transaction = new Transaction(); // {eventName, numTickets, eventDate, transactionCode}
		// Get and validate the event name
		transaction.eventName = GetValidEventName(session, r, code);
		if(transaction.eventName.equals("cancel")){
			System.out.println("Transaction cancelled. \n");
			return null;
		}
		if(hasNumTickets){
			// Get and validate the number of tickets
			transaction.numTickets = GetValidNumTickets(session, r, transaction.eventName, code);
			if(transaction.numTickets.equals("cancel")){
				System.out.println("Transaction cancelled. \n");
				return null;
			}
		}
		if(hasDate){
			// Get and validate the event date
			transaction.eventDate = GetValidEventDate(r);
			if(transaction.eventDate.equals("cancel")){
				System.out.println("Transaction cancelled. \n");
				return null;
			}
		}
		// Set the transaction code
		transaction.transactionCode = code;
		// Store the event for the event transaction file
		session.Transactions.add(transaction);
		return transaction;
	}
	
	/**
	 * Prompts the user for an event name and validates it.
	 * If the even name is not valid, the user must provide a new name until it passes validation.
	 * Names are validated using the ValidateEventName method.
	 * 
	 * @param session				The current session data.
	 * @param r						The buffered reader to get user input.
	 * @param code					The transaction code. (01 - sell, 02 - return, 03 - create, 04 - add, 05 - delete)
	 * @return						The validated event name as a <code>String</code>.
	 * @throws IOException			Handles invalid user input.
	 */
	public static String GetValidEventName(SessionStorage session, BufferedReader r, String code) throws IOException{
		System.out.println("Please enter the name of the event: \n ");
		String eventName = r.readLine().replaceAll("\n", "").trim();
		result = ValidateEventName(session, eventName, code);
		while(result.value == false && !eventName.equals("cancel")){
			System.out.println(result.text);
			System.out.println("Please enter the name of the event: \n ");
			eventName = r.readLine().replaceAll("\n", "").trim();
			result = ValidateEventName(session, eventName, code);
		}
		return eventName;
	}
	
	/**
	 * Prompts the user for a number of tickets and validates it.
	 * If the number of tickets is not valid, the user must provide a new amount until it passes validation.
	 * The number of tickets is validated using the ValidateNumTickets method.
	 * 
	 * @param session				The current session data.
	 * @param r						The buffered reader to get user input.
	 * @param eventName				The name of the event the tickets belong to.
	 * @param code					The transaction code. (01 - sell, 02 - return, 03 - create, 04 - add, 05 - delete)
	 * @return						The validated number of tickets as a <code>String</code>.
	 * @throws IOException			Handles invalid user input.
	 */
	public static String GetValidNumTickets(SessionStorage session, BufferedReader r, String eventName, String code) throws IOException{
		System.out.println("Please enter the number of tickets: \n ");
		String numTickets = r.readLine().replaceAll("\n", "").trim();
		result = ValidateNumTickets(session, eventName, numTickets, code);
		while(result.value == false){
			System.out.println(result.text);
			System.out.println("Please enter the number of tickets: \n ");
			numTickets = r.readLine().replaceAll("\n", "").trim();
			result = ValidateNumTickets(session, eventName, numTickets, code);
		}
		return numTickets;
	}
	
	/**
	 * Prompts the user for an event date and validates it.
	 * If the date is not valid, the user must provide a new date until it passes validation.
	 * The date is validated using the ValidateEventDate method.
	 * 
	 * @param r						The buffered reader to get user input.
	 * @return						The validated event date as a <code>String</code>.
	 * @throws IOException			Handles invalid user input.
	 */
	public static String GetValidEventDate(BufferedReader r) throws IOException{
		System.out.println("Please enter the date of the event (YYMMDD): \n ");
		String eventDate = r.readLine().replaceAll("\n", "").trim();
		result = ValidateEventDate(eventDate);
		while(result.value == false){
			System.out.println(result.text);
			System.out.println("Please enter the date of the event (YYMMDD): \n ");
			eventDate = r.readLine().replaceAll("\n", "").trim();
			result = ValidateEventDate(eventDate);
		}
		return eventDate;
	}
	
	private static TextValuePair ValidateEventName(SessionStorage session, String eventName, String transactionCode){
		// Must be at most 20 characters and unique
		TextValuePair result = new TextValuePair();
		if(eventName.length() > 20){
			result.text = "Invalid event name. Must be under 20 characters.\n";
			result.value = false;
			return result;
		}
		if(transactionCode.equals("03") && session.Events.get(eventName) != null) {
			result.text = "Invalid event name. Event already exists.\n";
			result.value = false;
			return result;
		}
		if(!transactionCode.equals("03") && session.Events.get(eventName) == null){
			result.text = "Invalid event name. Event does not exist.\n";
			result.value = false;
			return result;
		}
		result.value = true;
		return result;
	}
	
	private static TextValuePair ValidateNumTickets(SessionStorage session, String eventName, String numTickets, String transactionCode){
		if(numTickets.equals("cancel")){
			result.value = true;
			return result;
		}
		TextValuePair result = new TextValuePair();
		int num = 0;
		try{
			num = Integer.parseInt(numTickets);
		}
		catch(NumberFormatException ex){
			result.text = "Invalid number of tickets. Must be an integer between 0 and 99999, inclusive.\n";
			result.value = false;
			return result;
		}
		
		// Check no negatives or not greater than 99999
		if((num < 0 || num > 99999)){
			result.text = "Invalid number of tickets. Must be between 0 and 99999, inclusive.\n";
			result.value = false;
			return result;
		}
		// No further validation needed for create
		if(transactionCode.equals("03")){
			result.value = true;
			return result;
		}
		// Case for selling to an admin
		int remainingTickets = Integer.parseInt(session.Events.get(eventName).numTickets);

		// Number of tickets being sold cannot exceed tickets remaining
		if(transactionCode.equals("01") && (remainingTickets - num < 0)){
			result.text = "Error. Not enough tickets remaining.\n";
			result.value = false;
			return result;
		}
		// Number of tickets being added or returned cannot make remaining amount exceed 99999
		if((transactionCode.equals("02") || transactionCode.equals("04")) && (remainingTickets + num > 99999)){
			result.text = "Error. Number of tickets cannot exceed 99999.\n";
			result.value = false;
			return result;
		}
		if(!session.IsAdmin){
			// Only transactions that reach here are sales sell or sales return
			if(num < 1 || num > 8){
				result.text = "Invalid number of tickets. Must be between 0 and 8 inclusive.\n";
				result.value = false;
				return result;
			}
		}
			
		result.value = true;
		return result;
	}
	
	private static TextValuePair ValidateEventDate(String eventDate){
		if(eventDate.equals("cancel")){
			result.value = true;
			  }     
		Date date = null;
		try {
		    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");;
		    formatter.setLenient(false);
		    date = formatter.parse(eventDate);
		    formatter = new SimpleDateFormat("yyMMdd");
		    eventDate = formatter.format(date);
		} catch (ParseException e) { 
		    e.printStackTrace();
		}
			return result;
		}
		// Must be between tomorrow and two years from today, inclusive
		TextValuePair result = new TextValuePair();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		String currentDate = dateFormat.format(date);
		//TODO: validate event date
		if(eventDate == null){
			result.text = "Invalid date. Must be between tomorrow and two years from today, inclusive.\n";
			result.value = false;
			return result;
		}
		
		result.value = true;
		return result;
	}

}
