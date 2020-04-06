package businessplan.remote.errors;

public class NotValidUserException extends Exception { 
    /**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	public NotValidUserException(String username, String password) {
        super("The user with name '" + username + "', password '" + password + "' is not a valid account.");
    }
}