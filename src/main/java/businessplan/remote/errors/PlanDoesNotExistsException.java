package businessplan.remote.errors;

public class PlanDoesNotExistsException extends Exception { 
    /**
	 * 
	 */
	private static final long serialVersionUID = 64L;

	public PlanDoesNotExistsException(String department, int planYear) {
        super("Plan with department '" + department + "' and in year '" + planYear + "' is currently not allowed to edit");
    }
}