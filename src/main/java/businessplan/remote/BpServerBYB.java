package businessplan.remote;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;

import businessplan.main.BusinessPlan;
import businessplan.main.VMOSA;
import businessplan.remote.errors.*;;

public class BpServerBYB extends UnicastRemoteObject implements BpServer{
	private boolean isRunning = false;
	private static final long serialVersionUID = -5331251289904714278L;
	private Path businessPlanFileDir;
	private Hashtable<String, User> userInfo = new Hashtable<String, User>();//{user_id:user}
	//The hashtable below is to store business plans
	private Hashtable<String, Hashtable<Integer, BusinessPlan>> planTableByDepartment = new Hashtable<String, Hashtable<Integer, BusinessPlan>>();
	
	public static void main(String args[]) throws RemoteException, PlanDoesNotExistsException {
		//To start from the command line/by main(), 
		// pass a output directory for the first parameter (index 0)
		// pass a serverBindName for the second parameter.
		Path businessPlanFileDir = Paths.get(args[0]);
		String serverBindName = args[1];
		System.out.println("Output file directory set to:" + businessPlanFileDir);
		BpServerBYB server = new BpServerBYB("admin", "byb", Paths.get(args[0]));
		server.startServer(serverBindName);
	}
	
	public BpServerBYB(String defaultAdminName, String defaultAdminPw, Path businessPlanFileDir) throws RemoteException, PlanDoesNotExistsException {
		this.businessPlanFileDir = businessPlanFileDir;
		User admin = new User(defaultAdminName, defaultAdminPw, "ADMIN", true);
		this.userInfo.put(defaultAdminName, admin);
	};
	
	protected void loadDataFromDisk() throws PlanDoesNotExistsException
	{
		File dir = this.businessPlanFileDir.toFile();
		  File[] directoryListing = dir.listFiles();
		  if (directoryListing != null) {
			  
		    for (File inFile : directoryListing) {
		    	String filePath = inFile.toString();
		    	String extension = filePath.substring(filePath.lastIndexOf("."));
		    	if (extension.equals(".xml")) {
				      BusinessPlan businessPlan = BusinessPlan.XMLDecode(inFile);
				      this.savePlanAsAdmin(businessPlan);
		    	}
		    }
		  } else {
		    // Handle the case where dir is not really a directory.
		    // Checking dir.isDirectory() above would not be sufficient
		    // to avoid race conditions with another process that deletes
		    // directories.
		  }
		
	}
	
	protected void saveDataOnDisk() {
		//This method saves all the businessplans in files.
		//The location of the storage files is specified by this.businessPlanFileDir.
		this.planTableByDepartment.forEach((planDepartment, planTableByYear) -> {
			planTableByYear.forEach((year, businessplan)->{
				File outFile = new File(this.businessPlanFileDir.toString(), businessplan.getName()+".xml");
				businessplan.XMLEncode(outFile);
			});
		});
	};
	
	public boolean isRunning()
	{
		//This method returns if the server is running and available online.
		return this.isRunning;
	}

	public void startServer(String serverBindName) throws RemoteException
	{
		System.out.println("Server started.");
		try {
			BpServerBYB server = this;
			//ConcreteRemoteObject stub = (ConcreteRemoteObject) UnicastRemoteObject.exportObject(remoteObj, 0);
			Naming.rebind(serverBindName, server);  
			int port = 9000;
			//Create a RMI registry on the server at the specified port number.
			//This registry accepts the requests from the client.
			Registry registry = LocateRegistry.createRegistry(port);
			//Assign a name for a client to reference the remote object.
			registry.rebind(serverBindName, server);
			this.isRunning = true;
            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
	};

	public boolean isValidAuth(String userName, String pw)
	{
		User user = this.userInfo.get(userName);
		if (user == null) {return false;};
		String storedPw = user.getPassword();
		if (pw.equals(storedPw)) {
			return true;
		}else {
			return false;
		}
	};
	
	public boolean isAdmin(String userName) {
		return userInfo.get(userName).isAdmin();
	}
	
	public boolean isValidAdminAuth(String userName, String pw) {
		return (this.isAdmin(userName) && this.isValidAuth(userName, pw));
	}
	
	public boolean userExists(String userName) {
		if (userInfo.containsKey(userName)) {
			return true;
		}else {
			return false;
		}
	}
	
	public User addUser(String adminUserName, String adminPw, User newUser) throws NotAdminException
	{
		if (this.isValidAdminAuth(adminUserName, adminPw)) {
			this.userInfo.put(newUser.getUserName(), newUser);
			// return the new user if a user is created successfully
			// this return statement follows the Google App Script Convention.
			return newUser;
		}else {
			throw new NotAdminException(adminUserName, adminPw);
		}
	};
	
	private BusinessPlan getPlanAsAdmin(String department, int year) throws PlanDoesNotExistsException {
		//This is a method to get a plan by department name and year.
		//It is intended to be used within this server class ONLY.
		//The purpose of this method is to avoid duplicate code.
		BusinessPlan targetPlan = this.planTableByDepartment.get(department).get(year);
		if (targetPlan == null) {
			throw new PlanDoesNotExistsException(department , year);
		}
		return targetPlan;
	}


	public BusinessPlan getPlanByYear(String userName, String pw, int year) throws NotValidUserException, PlanDoesNotExistsException
	{
		if (this.isValidAuth(userName, pw)) {
			User requester = this.userInfo.get(userName);
			String userDepartment = requester.getDepartment();
			BusinessPlan targetPlan = this.getPlanAsAdmin(userDepartment, year);
			return targetPlan;
		}else {
			throw new NotValidUserException(userName, pw);
		}
	};

	public BusinessPlan createNewPlan(String userName, String pw, Class businessPlanClass, String planName, int year) throws InstantiationException, IllegalAccessException, NotValidUserException
	{
		if (this.isValidAuth(userName, pw)) {
			User requester = this.userInfo.get(userName);
			BusinessPlan newPlan = (BusinessPlan) businessPlanClass.newInstance();
			newPlan.setName(planName);
			newPlan.setYear(year);
			newPlan.setDepartment(requester.getDepartment());
			newPlan.setIsEditable(true);
			return newPlan;
		}else {
			throw new NotValidUserException(userName, pw);
		}
	}

	public BusinessPlan savePlan(String userName, String pw, BusinessPlan businessPlan) throws NotValidUserException, DepartmentDoesNotMatchException, PlanDoesNotExistsException, PlanNotEditableException
	{
		//Throw an exception if the plan is not editable.
		if (!businessPlan.isEditable()) {
			throw new PlanNotEditableException(businessPlan.getDepartment(), businessPlan.getYear());
		};
		if (this.isValidAuth(userName, pw)) {
			User requester = this.userInfo.get(userName);
			String requesterDepartment = requester.getDepartment();
			//if the department of the business plan & user's deparment don't match, throw an exception.
			if (!requesterDepartment.equals(businessPlan.getDepartment())) {
				throw new DepartmentDoesNotMatchException(requesterDepartment, businessPlan.getDepartment());
			};
			this.savePlanAsAdmin(businessPlan);
			return businessPlan;
		}else {
			throw new NotValidUserException(userName, pw);
		}
	}
	
	private void savePlanAsAdmin(BusinessPlan businessPlan) throws PlanDoesNotExistsException {
		//This is a method to get a plan in the planTable without user authentication.
		//It is intended to be used within this server class ONLY.
		//The purpose of this method is to avoid duplicate code.
		
		String planDepartment = businessPlan.getDepartment();
		boolean departmentAlreadyExists = (this.planTableByDepartment.containsKey(planDepartment));
		Hashtable<Integer, BusinessPlan> planTableByYear = null;
		if (!departmentAlreadyExists) {
			//If a department does not exists on the plan record, create dictionaries to store their plans.
			//Here, it creates a nesting dictionary: {department: {year:businesplan}}
			planTableByYear = new Hashtable<Integer, BusinessPlan>();
			planTableByYear.put(businessPlan.getYear(), businessPlan);
			this.planTableByDepartment.put(planDepartment, planTableByYear);
			//Reset the isEditable flag to what is stored on server.
			//This prevents a normal user from switching the editable flag without permission.
			BusinessPlan existingPlan = this.getPlanAsAdmin(planDepartment, businessPlan.getYear());
			businessPlan.setIsEditable(existingPlan.isEditable());
		}else {
			planTableByYear = this.planTableByDepartment.get(businessPlan.getDepartment());
		}
		planTableByYear.put(businessPlan.getYear(), businessPlan);
	}

	public boolean planExists(String userName, String pw, int year) throws NotValidUserException
	{
		if (this.isValidAuth(userName, pw)) {
			User requester = this.userInfo.get(userName);
			String requesterDepartment = requester.getDepartment();
			boolean departmentExists = this.planTableByDepartment.containsKey(requesterDepartment);
			boolean planForTheYearExists = this.planTableByDepartment.get(requesterDepartment).containsKey(year);
			return departmentExists && planForTheYearExists;
		}else {
			throw new NotValidUserException(userName, pw);
		}
	}

	public void turnOffEditingFor(String adminUserName, String adminPw, String department, int year) throws NotAdminException, NotValidUserException, DepartmentDoesNotMatchException, PlanDoesNotExistsException
	{
		if (this.isValidAdminAuth(adminUserName, adminPw)) {
			BusinessPlan targetPlan = this.getPlanAsAdmin(department, year);
			targetPlan.setIsEditable(false);
			//If the user info is incorrect, this.savePlan raises an exception
			this.savePlanAsAdmin(targetPlan);
		}else {
			throw new NotAdminException(adminUserName, adminPw);
		}

	}

	public void turnOnEditingFor(String adminUserName, String adminPw, String department, int year) throws NotAdminException, NotValidUserException, DepartmentDoesNotMatchException, PlanDoesNotExistsException
	{
		if (this.isValidAdminAuth(adminUserName, adminPw)) {
			BusinessPlan targetPlan = this.getPlanAsAdmin(department, year);
			targetPlan.setIsEditable(true);
			//If the user info is incorrect, this.savePlan raises an exception
			this.savePlanAsAdmin(targetPlan);
		}else {
			throw new NotAdminException(adminUserName, adminPw);
		}
	}

	public boolean isEditable(String department, int year) throws PlanDoesNotExistsException
	{
		BusinessPlan targetPlan = this.getPlanAsAdmin(department, year);
		return targetPlan.isEditable();
	}
}