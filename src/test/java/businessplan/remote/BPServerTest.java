package businessplan.remote;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.RemoteException;

import org.junit.jupiter.api.Test;
import businessplan.main.*;
import businessplan.remote.errors.DepartmentDoesNotMatchException;
import businessplan.remote.errors.NotAdminException;
import businessplan.remote.errors.NotValidUserException;
import businessplan.remote.errors.PlanDoesNotExistsException;
import businessplan.remote.errors.PlanNotEditableException;

class BPServerTest
{

	@Test
	void test()
	{
		System.out.println("START: BPServer unit test");
		System.out.println("NOTE: This script requires Java 1.8 compliance.");
		String defaultAdminName = "DefaultAdminName";
		String defaultAdminPw = "defaultAdminPw";
		//You can specify the path to store the file by Path class, which saves your from dealing with unknown string errors.
		Path outputFileDirPath = Paths.get(System.getProperty("user.dir") + "/businessplan-storage/");
		BpServerBYB server = null;
		try
		{
			server = new BpServerBYB(defaultAdminName, defaultAdminPw, outputFileDirPath);
		} catch (Exception e){fail("BpServerBYB gave an exception:"+e.getMessage());};
		
		String invalidAdminName = "This user name should not exist";
		String invalidAdminPw = "This pw should not work";
		String validAdminName = defaultAdminName;
		String validAdminPw = defaultAdminPw;
		
		//Test login
		boolean adminLoginSuccess;
		boolean userExists = server.userExists(invalidAdminName);
		assertEquals(false, userExists);
		//This method just returns if a user is valid or not.
		adminLoginSuccess = server.isValidAuth(invalidAdminName, invalidAdminPw);
		assertEquals(false, adminLoginSuccess);
		adminLoginSuccess = server.isValidAuth(validAdminName, validAdminPw);
		assertEquals(true, adminLoginSuccess);
		
		//Create a new user, using the admin client.
		String userName = "This is a new user's name";
		String pw = "This is a new user's pw";
		boolean isAdmin = false;
		String department = "CSC";
		User normalUser1 = new User(userName, pw, department, isAdmin);
		User addedUser = null;
		try
		{
			addedUser = server.addUser(defaultAdminName, defaultAdminPw, normalUser1);
		} catch (NotAdminException e1)
		{
			fail("server.addUser() should successfully add a new user with an admin account.");
		}
		assertEquals(normalUser1.getUserName(), addedUser.getUserName());
		
		//Test if the user is actually added and if it is accessible
		boolean userLoginSuccess;
		userLoginSuccess = server.isValidAuth(normalUser1.getUserName(), normalUser1.getPassword()
);
		assertEquals(true, userLoginSuccess);
		
		//Check a normal account does not have admin method access.
		User newUser2 = new User("normalUser1.getUserName()2", "pw2", "department2", false);
		try {
			server.addUser(normalUser1.getUserName(), normalUser1.getPassword(), newUser2);
			fail("server.addUser() shouldn't allow a normal user to add a new user.");
		}catch(Exception e) {
			//Check if the exception is NotAdminException
			assertEquals(e.getClass(), NotAdminException.class);
		};
		
		//Test all the Client methods

		//1. Check if the script can load the business plan on the client, edit, and restore it back.
    	int yearNow = 2020;
    	String csc2020PlanStr;
    	String restored2020BpStr;
    	BusinessPlan csc2020Plan = null;
    	//Create a new instance of business plan
    	//You pass a class of the business plan you want to create.
		try
		{
			csc2020Plan = server.createNewPlan(normalUser1.getUserName(), normalUser1.getPassword()
, VMOSA.class, "New plan 2020", yearNow);
		} catch (Exception e){
			fail("server.createNewPlan() should not raise an exception with a proper user info.");
		}
		//Check if a BusinessPlan method works.
		csc2020Plan.setName("New plan 2020 revised");
		csc2020Plan.setDepartment(department);
    	csc2020PlanStr = csc2020Plan.toString();
    	
    	//Test if an invalid user cannot save a business plan.
		try {
	    	server.savePlan(invalidAdminName, invalidAdminPw, csc2020Plan);
	    	fail("server.savePlan should throw an exception when an invalid user asks to save a new plan.");
		}catch(Exception e) {}
		
		//Test that a valid user can save a business plan.
    	try
		{
			server.savePlan(normalUser1.getUserName(), normalUser1.getPassword()
, csc2020Plan);
		} catch (Exception e)
		{
			System.out.println(e.getMessage() + ":" + e.getClass());
			fail("server.savePlan should not raise an exception when a valid user is saving a plan.");
		}
    	//Restore the business plan you just created and saved.
    	BusinessPlan restored2020Bp = null;
    	try
		{
			restored2020Bp = server.getPlanByYear(normalUser1.getUserName(), normalUser1.getPassword()
, csc2020Plan.getYear());
		} catch (Exception e){}
    	restored2020BpStr = restored2020Bp.toString();
    	//Check if a the business plan is identical before saving it and after restoring
    	assertEquals(csc2020PlanStr, restored2020BpStr);
		//Check if server throws an error when an invalid user tries to create a new plan.
		try {
	    	server.getPlanByYear(invalidAdminName, invalidAdminPw, 1997);
	    	fail("server.createNewPlan() should throw an error when an invalid user asks to get a business plan.");
		}catch(Exception e) {
			//Check the thrown error is PlanDoesNotExistsException
		}
		//Check if server throws an error if business plan does not exists.
		try {
	    	server.getPlanByYear(normalUser1.getUserName(), normalUser1.getPassword()
, 1997);
	    	fail("server.getPlanByYear should throw an error when a user asks for a business plan that doesn't exists.");
		}catch(Exception e) {}
		//Check if server throws an error if user info is invalid.
		try {
	    	server.getPlanByYear(invalidAdminName, invalidAdminPw,  csc2020Plan.getYear());
	    	fail("server.getPlanByYear should throw an error when an invalid user asks for a businessplan.");
		}catch(Exception e) {}
		
    	//2. Create a new business plan and save it.
    	int yearPrev = 2019;
    	BusinessPlan csc2019Plan = null;
		try
		{
			csc2019Plan = server.createNewPlan(normalUser1.getUserName(), normalUser1.getPassword()
, VMOSA.class, "csc2019Plan", yearPrev);
		} catch (Exception e) {
			fail("server.createNewPlan() should create a new BusinessPlan instance with valid user info.");
		}
		
		//Check the plan that has not been saved(2019) does not exists on the server.
    	boolean planExists = true;
		try
		{
			planExists = server.planExists(normalUser1.getUserName(), normalUser1.getPassword()
, csc2019Plan.getYear());
		} catch (NotValidUserException e4){}
    	assertEquals(false, planExists);
    	
    	//Check if a valid user can save the plan.
    	try
		{
			server.savePlan(normalUser1.getUserName(), normalUser1.getPassword()
, csc2019Plan);
		} catch (Exception e1)
		{
			fail("server.savePlan() should raise no exceptions when a valid user is saving a plan.");
		}
    	
    	//Check if the saved business plan exists on the server.
    	try
		{
			planExists = server.planExists(normalUser1.getUserName(), normalUser1.getPassword()
, csc2020Plan.getYear());
		} catch (NotValidUserException e3){}
	    	assertEquals(true, planExists);
	    	BusinessPlan restored2019Plan = null;
		try
		{
			restored2019Plan = server.getPlanByYear(normalUser1.getUserName(), normalUser1.getPassword()
, yearPrev);
		} catch (Exception e2){}
    	//Check if the business plan stays the same before and after saving it on server.
		assertEquals(restored2019Plan.toString(), csc2019Plan.toString());
    	
    	//3.Check if an admin can turn off editing status.
		boolean csc2020PlanIsEditable = false;
		try
		{
			csc2020PlanIsEditable = server.isEditable(csc2020Plan.department, csc2020Plan.getYear());
		} catch (PlanDoesNotExistsException e2){
			fail("server.isEditable() should return a boolean when being passed the plan info that exists.");
		}
		//Check if the csc2020Plan is editable before turning the editable status off.
		assertEquals(true, csc2020PlanIsEditable);
		try
		{
			//Turn off the editable status
			server.turnOffEditingFor(defaultAdminName, defaultAdminPw, csc2020Plan.getDepartment(), csc2020Plan.getYear());
		} catch (Exception e1)
		{
			fail("server.turnOffEditingFor() should allow a valid admin user to turn off the editing status.");
		};
		
		//Check if the editable flag is now false after turning off the editable status.
		csc2020PlanIsEditable = true;
		try
		{
			csc2020PlanIsEditable = server.isEditable(csc2020Plan.getDepartment(), csc2020Plan.getYear());
		} catch (PlanDoesNotExistsException e2){}
		assertEquals(false, csc2020PlanIsEditable);
		
		// Double check if the plan is not editable by saving a plan.
		try{
			server.savePlan(normalUser1.getUserName(), normalUser1.getPassword()
, csc2020Plan);
		}catch(Exception e) {
			//Check if the exception is PlanNotEditableException
			assertEquals(e.getClass(), PlanNotEditableException.class);
		};
		
		//Check if a non-admin user cannot change the editable status.
		try
		{
			server.turnOffEditingFor(invalidAdminName, invalidAdminPw, csc2020Plan.getDepartment(), csc2020Plan.getYear());
			fail("server.turnOffEditingFor() should raise an exception when an invalid account is trying to change the edit permission.");
		} catch (Exception e){};
		try
		{
			server.turnOffEditingFor(normalUser1.getUserName(), normalUser1.getPassword()
, csc2020Plan.getDepartment(), csc2020Plan.getYear());
			fail("server.turnOffEditingFor() should raise an exception when a non-admin account is trying to change the edit permission.");
		} catch (Exception e){};
		
		//Check if an admin can turn the editing status back on.
		try
		{
			server.turnOnEditingFor(defaultAdminName, defaultAdminPw, department, yearNow);
		} catch (Exception e1)
		{
			fail("server.turnOnEditingFor() should allow a valid admin user to turn on the editing status.");
		}
		//A normal user is not allow to turn it on.
		try
		{
			server.turnOnEditingFor(normalUser1.getUserName(), normalUser1.getPassword()
, department, yearNow);
			fail("server.turnOnEditingFor() shouldn't allow a normal user to turn on the editing status.");
		} catch (Exception e1){}
		
		//Check if a user can now save(edit) the plan after the editable is turned on.
    	try
		{
			server.savePlan(normalUser1.getUserName(), normalUser1.getPassword()
, csc2019Plan);
		} catch (Exception e1)
		{
			fail("server.savePlan() should raise no exceptions when a valid user is saving a plan.");
		};
		//Save the business plans in files.
		server.saveDataOnDisk();
		//Create a new server instance and load the files back
		BpServerBYB server2 = null;
		try
		{	
			//Create a new server instance
			server2 = new BpServerBYB(defaultAdminName, defaultAdminPw, outputFileDirPath);
		} catch (Exception e1){}
		
		//Create the same user that created the csc2019Plan.
		try
		{
			//normalUser1 is defined at the beginning of the test. This user created csc2019Plan
			server2.addUser(defaultAdminName, defaultAdminPw, normalUser1);
		} catch (NotAdminException e){
			fail("server2.addUser() raised an error.");
		}
		BusinessPlan restoredCSC2019Plan = null;
		try
		{	//Load the files and get the businessPlan
			server2.loadDataFromDisk();
			restoredCSC2019Plan = server2.getPlanByYear(normalUser1.getUserName(), normalUser1.getPassword(), csc2019Plan.getYear());
		} catch (Exception e){
//			System.out.print(e.getMessage());
			fail(e.getMessage()+":server2.getPlanByYear() should be able to retrieve the CSC2019Plan.");
		};
		assertEquals(csc2019Plan.toString(), restoredCSC2019Plan.toString());


		
		//fail("implement routine saving and loading of business plans");
		System.out.println("SUCCESS: All test passed in BPServerTest");
	};
	


}
