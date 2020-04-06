package businessplan.remote.errors;

public class DepartmentDoesNotMatchException extends Exception { 
    /**
	 * 
	 */
	private static final long serialVersionUID = 15L;

	public DepartmentDoesNotMatchException(String userDepartment, String planDepartment) {
        super("User's department and the BusinessPlan's department have to match. User department:" + userDepartment + "/Plan department:" + planDepartment);
    };
}