package quibble;
import java.util.*;

/**
 * The SessionStorage class is instantiated on program start and stores all session data.
 * The session storage will contain:
 * 
 * <ul>
 * <li> A flag indicating if the user is logged in or not. </li>
 * <li> A flag indicating if the user is an admin or not. </li>
 * <li> A hashtable containing each event, where the key is the event name and the value is the EventData object for the event. </li>
 * <li> An ArrayList of Transaction objects. This preserves all transactions to be processed and written to the daily event transaction file on logout. </li>
 * </ul>
 * 
 * @author 		Gabrielle Quilliam
 * @author 		Hassan Haq
 * @version 	1.0
 * @since 		2015-10-20
 */
public class SessionStorage {
	public boolean isLoggedIn;
	public boolean IsAdmin;
	public Hashtable<String, EventData> Events = new Hashtable<String, EventData>();
	public ArrayList<Transaction> Transactions = new ArrayList<Transaction>();
}
