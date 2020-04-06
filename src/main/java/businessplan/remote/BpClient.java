package businessplan.remote;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import businessplan.main.BusinessPlan;
import businessplan.remote.errors.ClientNotLoggedInException;
import businessplan.remote.errors.DepartmentDoesNotMatchException;
import businessplan.remote.errors.NotAdminException;
import businessplan.remote.errors.NotValidUserException;
import businessplan.remote.errors.PlanDoesNotExistsException;
import businessplan.remote.errors.PlanNotEditableException;

public class BpClient
{
	private BpServer server = null;
	private String userName;
	private String password;
	private boolean isLoggedIn = false;

	public BpClient(String serverHostname, String serverName, int serverHostPort) throws Exception {
    	//Eg: serverHostname = "127.0.0.1";
		// 	  serverName = "test-server"  A name for a client to reference a server.
		//    hostPort = 3000
        try {
    	    Registry registry = LocateRegistry.getRegistry(serverHostname, serverHostPort);
    	    BpServer stub = (BpServer)registry.lookup(serverName);
            this.server = stub;
        } catch (Exception e) {
            throw e;
        }
	}
	
	public boolean login(String userName, String pw) throws RemoteException
	{
		if (this.server.isValidAuth(userName, pw)) {
			this.userName = userName;
			this.password = pw;
			this.isLoggedIn = true;
			return true;
		}else {
			return false;
		}
	}
	
	public boolean isLoggedIn() {
		return this.isLoggedIn;
	}

	public BusinessPlan getPlanByYear(int year) throws NotValidUserException, PlanDoesNotExistsException, ClientNotLoggedInException, RemoteException
	{
		if (this.isLoggedIn()) {
			return this.server.getPlanByYear(this.userName, this.password, year);
		}else {
			throw new ClientNotLoggedInException();
		}
	}
	
	public BusinessPlan createNewPlan(Class<?> businessPlanClass, String planName, int year) throws InstantiationException, IllegalAccessException, NotValidUserException, ClientNotLoggedInException, RemoteException
	{
		if (this.isLoggedIn()) {
			return this.server.createNewPlan(this.userName, this.password, businessPlanClass, planName, year);
		}else {
			throw new ClientNotLoggedInException();
		}
	}

	public BusinessPlan savePlan(BusinessPlan businessPlan) throws NotValidUserException, DepartmentDoesNotMatchException, PlanDoesNotExistsException, PlanNotEditableException, ClientNotLoggedInException, RemoteException
	{
		if (this.isLoggedIn()) {
			return this.server.savePlan(this.userName, this.password, businessPlan);
		}else {
			throw new ClientNotLoggedInException();
		}
	}
	
	public boolean planExists(int year) throws ClientNotLoggedInException, NotValidUserException, RemoteException{
		if (this.isLoggedIn()) {
			return this.server.planExists(this.userName, this.password, year);
		}else {
			throw new ClientNotLoggedInException();
		}
	}

	public User addUser(User newUser) throws ClientNotLoggedInException, NotAdminException, RemoteException
	{
		if (this.isLoggedIn()) {
			 return this.server.addUser(this.userName, this.password, newUser);
		}else {
			throw new ClientNotLoggedInException();
		}
	}

	public void turnOffEditingFor(String department, int year) throws ClientNotLoggedInException, NotAdminException, NotValidUserException, DepartmentDoesNotMatchException, PlanDoesNotExistsException, RemoteException
	{
		if (this.isLoggedIn()) {
			this.server.turnOffEditingFor(this.userName, this.password, department, year);
		}else {
			throw new ClientNotLoggedInException();
		}		
	}
	
	public void turnOnEditingFor(String department, int year) throws NotAdminException, NotValidUserException, DepartmentDoesNotMatchException, PlanDoesNotExistsException, ClientNotLoggedInException, RemoteException
	{
		if (this.isLoggedIn()) {
			this.server.turnOnEditingFor(this.userName, this.password, department, year);
		}else {
			throw new ClientNotLoggedInException();
		}			
	}

}
