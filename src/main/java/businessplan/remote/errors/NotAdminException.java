package businessplan.remote.errors;

public class NotAdminException extends Exception { 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotAdminException(String username, String password) {
        super("The user with name '" + username + "', password '" + password + "' is not an admin account.");
    }
}