package businessplan.remote;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import businessplan.main.Action;
import businessplan.main.BusinessPlan;
import businessplan.main.Goal;
import businessplan.main.Mission;
import businessplan.main.Objective;
import businessplan.main.Part;
import businessplan.main.Strategy;
import businessplan.main.Vision;

class BpClientTest
{

	@Test
	void test()
	{
		System.out.println("START: BpClientTest unit test");
		String serverObjBindName = "test-server";
		String testServerUrl = "rmi://127.0.0.1/" + serverObjBindName;
		String validAdminName = "DEFAULT_ADMIN_NAME";
		String validAdminPw = "DEFAULT_ADMIN_PW";
		BpServer bps = new BpServerBYB(validAdminName, validAdminPw);
		bps.startServer(serverObjBindName);
		BpClient adminClient = new BpClient(testServerUrl);
		String invalidAdminName = "This user name should not exist";
		String invalidAdminPw = "This pw should not work";

		
		//Test login
		boolean adminLoginSuccess;
		adminLoginSuccess = adminClient.login(invalidAdminName, invalidAdminPw);
		assertEquals(false, adminLoginSuccess);
		adminLoginSuccess = adminClient.login(validAdminName, validAdminPw);
		assertEquals(true, adminLoginSuccess);
		
		//Create a new user, using the admin client.
		String userName = "This is a new user's name";
		String pw = "This is a new user's pw";
		boolean isAdmin = false;
		String department = "CSC";
		User newUser1 = new User(userName, pw, department, isAdmin);
		boolean addUserSuccess = adminClient.addUser(newUser1);
		assertEquals(true, addUserSuccess);
		
		//Test if the user is actually added and if it is accessible
		BpClient normalClient = new BpClient();
		boolean userLoginSuccess;
		userLoginSuccess = normalClient.login(userName, pw);
		assertEquals(true, userLoginSuccess);
		
		//Check a normal account does not have admin method access.
		try {
			User newUser2 = new User("userName2", "pw2", "department2", false);
			normalClient.addUser(newUser2);
			fail("BpClient.addUser() should not allow an operation");
		}catch(Exception e) {};
		try {
			normalClient.turnOffEditingFor(2020);
			fail("BpClient.addUser() should not allow an operation");
		}catch(Exception e) {};
		
		//Test all the Client methods
		//1. Check if the script can load the business plan on the client, edit, and restore it back.
		int year = 2020;
		String bpStr;
		String restoredBpStr;
		BusinessPlan bp = normalClient.getPlanByYear(year);
		BusinessPlan restoredBp = normalClient.getPlanByYear(year);
		bpStr = bp.toString();
		restoredBpStr = restoredBp.toString();
		assertEquals(bpStr, restoredBpStr);
		//2. Create a new business plan and save it.
		BusinessPlan newBp = normalClient.createNewPlan("testPlan", 2019);
		boolean planExists = normalClient.planExists(2019);
		assertEquals(false, planExists);
		normalClient.savePlan(newBp);
		planExists = normalClient.planExists(2019);
		assertEquals(true, planExists);
		BusinessPlan restoredNewPlan = normalClient.getPlanByYear(2019);		
		
		
		//Test if operations are prohibited before login.
		BusinessPlan plainBp = normalClient.createNewPlan("plainBusinessPlan", 2018);
		BpClient userClient2 = new BpClient(); //New client
		try {
			userClient2.getPlanByYear(year);
			fail("BpClient.getPlanByYear() should not allow an operation without login.");
		}catch(Exception e) {};
		try {
			userClient2.savePlan(plainBp);
			fail("BpClient.editPlan() should not allow an operation without login.");
		}catch(Exception e) {};
		try {
			userClient2.createNewPlan("newPlan", 2017);
			fail("BpClient.createNewPlan() should not allow an operation without login.");
		}catch(Exception e) {};
		try {
			User newUser3 = new User("Name", "pw", "department", false);
			userClient2.addUser(newUser3);
			fail("BpClient.addUser() should not allow an operation without login.");
		}catch(Exception e) {};
		
		System.out.println("SUCCESS: All test passed in BpClientTest");
	};

}
