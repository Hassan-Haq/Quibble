package quibble;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * The AccountController manages all account-related methods and functionality prior to login and after logout. 
 * The goal of the AccountController is to start and end all front end sessions.
 * The account controller also handles reading in the current events file and writing out the daily event transaction file.
 * 
 * @author 		Gabrielle Quilliam
 * @author 		Hassan Haq
 * @version 	1.0
 * @since 		2015-10-20
 */
public class AccountController {
	
	/**
	 * The login method listens for a login transaction.
	 * If a login transaction is successfully read, it then prompts for a session type (sales or admin).
	 * If a valid session type is read, the session flags `isAdmin` and `isLoggedIn` are set accordingly.
	 * The current events file is read in from ./src/InputFiles/CurrentEventsFile.txt and is mapped to the session data Events hashtable.
	 * A welcome message is also displayed to the corresponding user type.
	 * 
	 * @param session		The current session data.
	 * @param r				The buffered reader to get user input.
	 * @throws IOException	Handles invalid user input.
	 */
	public static void login(SessionStorage session, BufferedReader r) throws IOException{
		// TODO : handle all io exceptions with try catch statements!
		// Get login transaction
		System.out.println("Please login to continue: \n ");
		String login = r.readLine().replaceAll("\n", "");
		while(!login.equals("login")){
			// Temp to exit
			if(login.equals("exit")){
				System.out.println("Thank you for using Quibble. Goodbye!\n");
				System.exit(0);
			}
			System.out.println("Invalid transaction. Please login to continue: \n ");
			login = r.readLine().replaceAll("\n", "");
		}
		
        // Get session type specification
		System.out.println("Please select a session type: \n      1. Sales (retail operator mode) \n      2. Admin (privileged user mode) \n");
		String sessionType = r.readLine().replaceAll("\n", "").trim();
		while(!(sessionType.equals("sales")) && !(sessionType.equals("admin")) && !(sessionType.equals("1")) && !(sessionType.equals("2"))){
			System.out.println("Invalid transaction. \nPlease select a session type: \n      1. Sales (retail operator mode) \n      2. Admin (privileged user mode) \n ");
			sessionType = r.readLine().replaceAll("\n", "").trim();
		}
		
		// Parse session type and set flags accordingly
		if(sessionType.equals("admin") || sessionType.equals("2")){
			System.out.println("Welcome to Quibble, Admin User!");
			session.IsAdmin = true;
		}
		else{
			System.out.println("Welcome to Quibble, Sales User!");
			 session.IsAdmin = false;
		}
		
        ReadCurrentEventsFile(session);
		session.isLoggedIn = true;	
	}
	
	/**
	 * The logout method ends a front end session and writes out the event transaction file.
	 * This file is written to /src/InputFiles/DailyEventTransactionFile.txt
	 * The logout method then sets the session flag `isLoggedIn` to false, thus ending the session.
	 * The method also clears the events hash table to prevent data exposure when not logged in.
	 * 
	 * @param session		The current session data.
	 */
	public static void logout(SessionStorage session) {
		try {
			WriteEventTransactionFile(session);
			session.Events.clear();
			session.isLoggedIn = false;
		}
		catch(FileNotFoundException ex){
			System.err.println("Error. File not found. " + ex.getMessage());
			System.exit(-1);
		}
		catch(IOException ex){
			System.err.println("Error. IO Exception. " + ex.getMessage());
			System.exit(-1);
		}
	}
	
	/**
	 * The WriteEventTransactionFile method maps the list of transactions to the Daily Event Transaction File.
	 * For each transaction in the session data, a line in the file is written containing the event name, date, num. tickets and transaction code.
	 * All lines must be 36 characters long, with each component separated by a space. 
	 * The event name must be 20 characters long with trailing whitespace should the name be under 20 characters.
	 * The number of tickets must be 5 characters long with trailing zeros should it be under 5 characters long.
	 * The event date is validated on creation and is therefore already the correct format.
	 * The transaction code is already in the correct format.
	 * 
	 * 
	 * @param session			The current session data.
	 * @throws IOException		Handles invalid file input.
	 */
	private static void WriteEventTransactionFile(SessionStorage session) throws IOException{
		String workingDirectory = System.getProperty("user.dir");
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
	              new FileOutputStream(workingDirectory + File.separator + "src" + File.separator + "OutputFiles" + File.separator + "DailyEventTransactionFile.txt"), "utf-8"))) {
			for(int i = 0; i < session.Transactions.size(); i++){
				Transaction currentTransaction = session.Transactions.get(i);
				currentTransaction = ParseTransaction(currentTransaction);
				writer.write(currentTransaction.transactionCode + " " + currentTransaction.eventName + " " + currentTransaction.eventDate + " " + currentTransaction.numTickets + System.lineSeparator());
			}
			// Add the end of file line
			writer.write("00 00000000000000000000 000000 00000");
			
			writer.close();
		}
		catch(FileNotFoundException ex){
			System.err.println("Error. File not found. " + ex.getMessage());
			System.exit(-1);
		}
		catch(IOException ex){
			System.err.println("Error. IO Exception. " + ex.getMessage());
			System.exit(-1);
		}
		catch(Exception ex){
			System.err.println("Error. Exception. " + ex.getMessage());
			System.exit(-1);
		}
	}
	
	/**
	 * The ParseTransaction method takes a transaction and converts it to the format specified in the requirements for the Daily Event Transaction File.
	 * All names must have trailing whitespace should they be less than 20 characters.
	 * All numeric values should have trailing zeros.
	 * The transaction string must total to 36 characters plus the new line.
	 * 
	 * @param t					The transaction being parsed.
	 * @return					A parsed transaction with a valid name, date and number of tickets.
	 */
	private static Transaction ParseTransaction(Transaction t){
		// Parse the event name and format it
		int nameLength = t.eventName.length();
		StringBuilder sb = new StringBuilder(20 - nameLength);
	    while(sb.length() < 20 - nameLength) {
	        sb.append(" ");
	    }
	    t.eventName = sb.append(t.eventName).toString();
	    
	    // Parse the number of tickets and format it
	    if(t.numTickets == null){
	    	t.numTickets = "00000";
	    }
	    int numLength = t.numTickets.length();
	    StringBuilder s = new StringBuilder(5 - numLength);
	    while(s.length() < 5 - numLength){
	    	s.append("0");
	    }
	    t.numTickets = s.append(t.numTickets).toString();
	    
	    // Parse the date if null
	    if(t.eventDate == null){
	    	t.eventDate = "000000";
	    }
	    
		return t;
	}

	/**
	 * The ReadCurrentEventsFile method creates a stream to read in the Current Events file and maps it to the session data.
	 * Events are stored in a hash table within the session data, where the key is the event name and the value is the Event Data object.
	 * The event data is mapped using the ParseEvent method.
	 * 
	 * @param session			The current session data.
	 */
	private static void ReadCurrentEventsFile(SessionStorage session){
		Stream<String> stream = null;
		try{
			String workingDirectory = System.getProperty("user.dir");
			// Open the stream
			stream = Files.lines(Paths.get(workingDirectory + File.separator + "src" + File.separator + "InputFiles" + File.separator + "CurrentEventsFile.txt"));
			stream.filter(l -> (l.substring(0, 20).trim() != "END")).forEach(l -> session.Events.put(l.substring(0, 20).trim(), ParseEvent(session, l)));
	        //System.out.println(session.Events);
			
		}
		catch(FileNotFoundException ex){
			System.err.println("Error. File not found. " + ex.getMessage());
			System.exit(-1);
		}
		catch(IOException ex){
			System.err.println("Error. IO Exception. " + ex.getMessage());
			System.exit(-1);
		}
		catch(NullPointerException ex){
			System.err.println("Error. Null Pointer. " + ex.getCause());
			System.exit(-1);
		}
		catch(Exception ex){
			System.err.println("Error. Exception. " + ex.getMessage());
			System.exit(-1);
		}  
		finally{
			stream.close();
		}
	}
	
	/**
	 * The ParseEvent method takes a line from the current events file and maps it to an EventData object.
	 * The event name is the first 20 characters of the file line.
	 * The number of tickets is the last 5 characters of the file line.
	 * The event date is not included.
	 * This object is later added to the Events hash table in the session data.
	 * 
	 * @param session			The current session data.
	 * @param currentEvent		The line from the Current Events file being converted to an EventData object.
	 * @return					An EventData object containing the data from the current events file.
	 */
	private static EventData ParseEvent(SessionStorage session, String currentEvent){
		EventData event = new EventData();
        // Parse the event string from the file
        event.eventName = currentEvent.substring(0, 20).trim();
        event.numTickets = currentEvent.substring(21, 25).replaceFirst("^0+(?!$)", "");
        return event;
	}
}


