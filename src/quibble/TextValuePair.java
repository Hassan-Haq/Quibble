package quibble;

/**
 * The TextValuePair class represents a tuple data structure intended on parsing success or error messages, depending on the results of validation.
 * It contains:
 * 
 * <ul>
 * <li> A message as a <code>String</code>. </li>
 * <li> A binary success code as a <code>boolean</code>, where <code>true</code> implies the result was successful. </li>
 * </ul>
 * 
 * @author 		Gabrielle Quilliam
 * @author 		Hassan Haq
 * @version 	1.0
 * @since 		2015-10-20
 */
public class TextValuePair {
	String text;
	boolean value;
}
