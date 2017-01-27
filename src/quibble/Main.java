package quibble;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <h1>Quibble - Queen's Basic Event Ticketing Service</h1>
 * The following front end implementation of Quibble covers all functionality described in the specification.
 * This program is intended to be run as a console application on kiosks around Queen's campus.
 * The goal of the system is to provide a means of selling tickets for events around campus.
 * 
 * Major system components include:
 * 
 * <ul>
 * <li> Create and Delete Events </li>
 * <li> Sell or return tickets </li>
 * <li> Add more tickets to an event </li>
 * </ul>
 * 
 * Input files to the front end of the system are:
 * <ul>
 * <li>The Current Events File</li>
 * </ul>
 * 
 *  Output files from the front end of the system are:
 * <ul>
 * <li>The Daily Event Transaction File</li>
 * </ul>
 * 
 * Notes:
 * 		- Comments marked "TODO" are to be implemented at a later phase of development
 * 
 * @author 		Gabrielle Quilliam
 * @author 		Hassan Haq
 * @version 	1.0
 * @since 		2015-10-20
 */
public class Main {
	/**
	 * The main method controls the flow of the system.
	 * It begins by listening for a login transaction and will not accept any transactions until a successful login.
	 * Once logged in, the system prompts the user for a transaction and maps that transaction to the corresponding method.
	 * There are two new transaction introduced that were not specified in the requirements.
	 * These are:
	 * <ul>
	 * <li> 1. cancel - cancels a transaction. Can only be called when executing a transaction. </li>
	 * <li> 2. exit - exits the system. Can only be called when logged out.</li>
	 * </ul>
	 * 
	 * @param argv 			Unused.
	 * @throws IOException	Handles invalid user input.
	 */
	 public static void main(String argv[]) throws IOException {
		 // Initialize session object and buffered reader
		 SessionStorage session = new SessionStorage();
		 
		 BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); 
		 System.out.println("**** Welcome to Quibble! ****\n");
		 // Listen for Login
		 AccountController.login(session, in);
		 // Successful login, listen for other transactions
		 while(session.isLoggedIn){
			 System.out.println("Enter a transaction code to continue: \n");
			 String transaction = in.readLine().replaceAll("\n", "");
			 // Parse the transaction code and route to corresponding method
			 switch(transaction){
			 	case "create":
			 		if(session.IsAdmin){
			 			EventController.Create(session, in);
			 		}
			 		else{
			 			System.out.println("Invalid permissions to use this transaction. Must be an admin user.\n\n");
			 		}
			 		break;
			 	case "add":
			 		if(session.Events.isEmpty()){
			 			System.out.println("No existing events. Please try again later. \n\n");
			 			break;
			 		}
			 		if(session.IsAdmin){
			 			TicketController.Add(session, in);
			 		}
			 		else{
			 			System.out.println("Invalid permissions to use this transaction. Must be an admin user.\n\n");
			 		}
			 		break;
			 	case "delete":
			 		if(session.Events.isEmpty()){
			 			System.out.println("No existing events. Please try again later. \n\n");
			 			break;
			 		}
			 		if(session.IsAdmin){
			 			EventController.Delete(session, in);
			 		}
			 		else{
			 			System.out.println("Invalid permissions to use this transaction. Must be an admin user.\n\n");
			 		}
			 		break;
			 	case "sell":
			 		if(session.Events.isEmpty()){
			 			System.out.println("No existing events. Please try again later. \n\n");
			 			break;
			 		}
			 		TicketController.Sell(session, in);
			 		break;
			 	case "return":
			 		if(session.Events.isEmpty()){
			 			System.out.println("No existing events. Please try again later. \n\n");
			 			break;
			 		}
			 		TicketController.Return(session, in);
			 		break;
			 	case "login":
			 		System.out.println("Already logged in!\n\n");
			 		break;
			 	case "logout":
			 		AccountController.logout(session);
			 		// TODO: get the event transaction file and send it to the back end
			 		break;
			 	default:
			 		System.out.println("Invalid transaction. Please try again. \n");
			 		break;
			 }
		 }
		 // Recursively call the main method after logout.
		 main(argv);	
	 }
}
