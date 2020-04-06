package businessplan.remote.errors;

public class ClientNotLoggedInException extends Exception { 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClientNotLoggedInException() {
        super("Client must log in using login() method beore executing any methods.");
    }
}