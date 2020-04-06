package businessplan.remote.errors;

public class PlanNotEditableException extends Exception { 
    /**
	 * 
	 */
	private static final long serialVersionUID = 3L;

	public PlanNotEditableException(String planName, int planYear) {
        super("Plan with name '" + planName + "' and in year '" + planYear + "' does not exists.");
    }
}