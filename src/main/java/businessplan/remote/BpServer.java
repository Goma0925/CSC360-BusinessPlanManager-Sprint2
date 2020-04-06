package businessplan.remote;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import businessplan.main.BusinessPlan;
import businessplan.remote.errors.DepartmentDoesNotMatchException;
import businessplan.remote.errors.NotAdminException;
import businessplan.remote.errors.NotValidUserException;
import businessplan.remote.errors.PlanDoesNotExistsException;
import businessplan.remote.errors.PlanNotEditableException;

public interface BpServer extends Remote{
	public void startServer(String serverObjBindName, int port)  throws RemoteException;

	public boolean isValidAuth(String userName, String pw) throws RemoteException;;

	public BusinessPlan getPlanByYear(String userName, String password, int year) throws RemoteException, NotValidUserException, PlanDoesNotExistsException;

	public BusinessPlan createNewPlan(String userName, String password, Class businessPlanClass, String planName,
			int year)  throws RemoteException, InstantiationException, IllegalAccessException, NotValidUserException;

	public BusinessPlan savePlan(String userName, String password, BusinessPlan businessPlan)  throws RemoteException, NotValidUserException, DepartmentDoesNotMatchException, PlanDoesNotExistsException, PlanNotEditableException;

	public boolean planExists(String userName, String password, int year)  throws RemoteException, NotValidUserException;

	public User addUser(String userName, String password, User newUser) throws RemoteException, NotAdminException;

	public void turnOffEditingFor(String userName, String password, String department, int year) throws RemoteException, NotAdminException, NotValidUserException, DepartmentDoesNotMatchException, PlanDoesNotExistsException;

	public void turnOnEditingFor(String userName, String password, String department, int year) throws RemoteException, NotAdminException, NotValidUserException, DepartmentDoesNotMatchException, PlanDoesNotExistsException;
}
