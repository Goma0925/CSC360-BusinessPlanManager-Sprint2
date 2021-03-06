package businessplan.remote;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import org.junit.jupiter.api.Test;

import businessplan.main.BusinessPlan;
import businessplan.main.VMOSA;
import businessplan.remote.errors.ClientNotLoggedInException;
import businessplan.remote.errors.DepartmentDoesNotMatchException;
import businessplan.remote.errors.NotAdminException;
import businessplan.remote.errors.NotValidUserException;
import businessplan.remote.errors.PlanDoesNotExistsException;
import businessplan.remote.errors.PlanNotEditableException;

class BPClientTest
{
	@Test
	void test()
	{
		System.out.println("START: BpClientTest unit test");
		//To test the network capability, start a server locally.
		String serverObjBindName = "test-server";
		String localServerHostname = "127.0.0.1";
		String defaultAdminName = "DEFAULT_ADMIN_NAME";
		String defaultAdminPw = "DEFAULT_ADMIN_PW";
		//The directory path for the SERVER to store business plans.
		Path outputFileDirPath = Paths.get(System.getProperty("user.dir") + "/businessplan-storage-BPClientTest/");
		int autoFileSaveInterval = 12000;//2 minute
		int serverPortNumber = 8000;
		BpServer bpServer = null;
		//Create a server
		try{
			bpServer = new BpServerBYB(defaultAdminName, defaultAdminPw, outputFileDirPath, autoFileSaveInterval);
		} catch (Exception e) {
			System.out.print(e.getMessage());
			fail("BpServerBYB could not initialize.");
		}
		//Start running the server online.
		try{
			bpServer.startServer(serverObjBindName, serverPortNumber);
		} catch (Exception e) {
			fail("bpServer.startServer() raised an exception.");
		};
		
		//Start client program that connects to the server.
		BpClient adminClient = null;
		try{
			adminClient = new BpClient(localServerHostname, serverObjBindName, serverPortNumber);
		} catch (Exception e)
		{
			System.out.print(e.getMessage());
			fail("Problem occured while a client connecting to a server.");
		};
		String invalidUserName = "This user name should not exist";
		String invalidUserPw = "This pw should not work";

		//Test login
		boolean adminLoginSuccess = false;
		try
		{
			adminLoginSuccess = adminClient.login(invalidUserName, invalidUserPw);
		} catch (RemoteException e2)
		{
			fail("adminClient.login(invalidUserName, invalidUserPw) raised an exception");
		}
		assertEquals(false, adminLoginSuccess);
		try
		{
			adminLoginSuccess = adminClient.login(defaultAdminName, defaultAdminPw);
		} catch (Exception e)
		{
			fail("adminClient.login(invalidUserName, invalidUserPw) raised an exception");
		}
		assertEquals(true, adminLoginSuccess);
		
		//Create a new user, using the admin client.
		String userName = "This is a new user's name";
		String pw = "This is a new user's pw";
		boolean isAdmin = false;
		String department = "CSC";
		User newUser1 = new User(userName, pw, department, isAdmin);
		try
		{
			adminClient.addUser(newUser1);
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			fail("An admin client should be able to add a new user without an exception.");
		}
		
		//Test if the user is actually added and if it is accessible
		BpClient normalClient = null;
		try{
			normalClient = new BpClient(localServerHostname, serverObjBindName, serverPortNumber);
		} catch (Exception e) {
			fail("BpServerBYB could not initialize.");
		}
		boolean userLoginSuccess = false;
		try
		{
			userLoginSuccess = normalClient.login(userName, pw);
		} catch (Exception e)
		{
			fail("normalClient.login did not work");
		}
		assertEquals(true, userLoginSuccess);
		
		//Check a normal account does not have admin method access.
		try {
			User newUser2 = new User("userName2", "pw2", "department2", false);
			normalClient.addUser(newUser2);
			fail("BpClient.addUser() should not allow an operation");
		}catch(Exception e) {};
		try {
			normalClient.turnOffEditingFor("department2", 2020);
			fail("BpClient.addUser() should not allow an operation");
		}catch(Exception e) {};
		
		//Test all the Client methods
		//1. Check if the script can load the business plan on the client, edit, and restore it back.
		int year = 2020;
		BusinessPlan bp = null;
		String bpName = "bp";
		BusinessPlan restoredBp =null;
		String bpStr;
		String restoredBpStr;
		try
		{
			bp = normalClient.createNewPlan(VMOSA.class, bpName, year);
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			fail("A normal client should be able to create a new businessplan.");
		}
		//Make a change
		bp.setName("bp revised");
		bp.setDescription("Description added");
		//save the business plan
		try
		{
			normalClient.savePlan(bp);
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			fail("A normal client should be able to save a new businessplan.");
		}
		//Restore a business plan
		try
		{
			restoredBp = normalClient.getPlanByYear(bp.getYear());
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			fail("A normal client should be able to get a businessplan by year.");
		};
		//Make sure if the restored plan is same as before it was saved on server.
		bpStr = bp.toString();
		restoredBpStr = restoredBp.toString();
		assertEquals(bpStr, restoredBpStr);
		
		//2. Test if planExists() method works.
		boolean planExists;
		try
		{
			//Make sure planExists() returns false for the plan that does not exist.
			planExists = normalClient.planExists(1997);
			assertEquals(false, planExists);	
			//Make sure planExists() returns true for the plan that does exist.
			planExists = normalClient.planExists(year);
			assertEquals(true, planExists);
		} catch (Exception e1)
		{
			fail("A client is already logged in but an exception is thrown.");
		};
		
		
		//Test if operations are prohibited before login.
		
		BpClient userClient2 = null;
		try{
			userClient2 = new BpClient(localServerHostname, serverObjBindName, serverPortNumber);
		} catch (Exception e) {
			fail("BpServerBYB could not initialize.");
		}
		try {
			userClient2.getPlanByYear(year);
			fail("BpClient.getPlanByYear() should not allow an operation without login.");
		}catch(Exception e){assertEquals(e.getClass(), ClientNotLoggedInException.class);};
		try {
			userClient2.savePlan(bp);
			fail("BpClient.editPlan() should not allow an operation without login.");
		}catch(Exception e){assertEquals(e.getClass(), ClientNotLoggedInException.class);};
		try {
			userClient2.createNewPlan(VMOSA.class, "newPlan", 2017);
			fail("BpClient.createNewPlan() should not allow an operation without login.");
		}catch(Exception e){assertEquals(e.getClass(), ClientNotLoggedInException.class);};
		try {
			User newUser3 = new User("Name", "pw", "department", false);
			userClient2.addUser(newUser3);
			fail("BpClient.addUser() should not allow an operation without login.");
		}catch(Exception e){assertEquals(e.getClass(), ClientNotLoggedInException.class);};
		
		System.out.println("SUCCESS: All test passed in BpClientTest");
	};

}
