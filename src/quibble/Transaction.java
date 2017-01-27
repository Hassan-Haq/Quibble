package quibble;

/**
 * The Transaction class acts as a model to store event data and a transaction code.
 * Every event will be stored on the front end as an EventData object.
 * The Transaction objects will contain:
 * <ul>
 * <li> The transaction code as a <code>String</code>. </li>
 * </ul>
 * 
 * Transaction objects will inherit (from EventData):
 * <ul>
 * <li> The event name as a <code>String</code>. </li>
 * <li> The event date as a <code>String</code>. </li>
 * <li> The number of tickets as a <code>String</code>. </li>
 * </ul>
 * 
 * @author 		Gabrielle Quilliam
 * @author 		Hassan Haq
 * @version 	1.0
 * @since 		2015-10-20
 */
public class Transaction extends EventData{
	String transactionCode;
}
