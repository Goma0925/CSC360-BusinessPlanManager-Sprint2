package businessplan.remote;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.Naming;
import org.junit.jupiter.api.Test;
import businessplan.main.BusinessPlan;

class BPServerTest
{

	@Test
	void test()
	{
		System.out.println("START: BPServer unit test");
		String defaultAdminName = "DefaultAdminName";
		String defaultAdminPw = "defaultAdminPw";
		BpServer server = new BpServerBYB(defaultAdminName, defaultAdminPw);
		
		String invalidAdminName = "This user name should not exist";
		String invalidAdminPw = "This pw should not work";
		String validAdminName = defaultAdminName;
		String validAdminPw = defaultAdminPw;
		
		//Test login
		boolean adminLoginSuccess;
		adminLoginSuccess = server.login(invalidAdminName, invalidAdminPw);
		assertEquals(false, adminLoginSuccess);
		adminLoginSuccess = server.login(validAdminName, validAdminPw);
		assertEquals(true, adminLoginSuccess);
		
		//Create a new user, using the admin client.
		String userName = "This is a new user's name";
		String pw = "This is a new user's pw";
		boolean isAdmin = false;
		String department = "CSC";
		User newUser1 = new User(userName, pw, department, isAdmin);
		boolean addUserSuccess = server.addUser(defaultAdminName, defaultAdminPw, newUser1);
		assertEquals(true, addUserSuccess);
		
		//Test if the user is actually added and if it is accessible
		boolean userLoginSuccess;
		userLoginSuccess = server.login(userName, pw);
		assertEquals(true, userLoginSuccess);
		
		//Check a normal account does not have admin method access.
		try {
			User newUser2 = new User("userName2", "pw2", "department2", false);
			server.addUser(defaultAdminName, defaultAdminPw, newUser2);
			fail("BpClient.addUser() should not allow an operation");
		}catch(Exception e) {};
		try {
			server.turnOffEditingFor(defaultAdminName, defaultAdminPw, 2010);
			fail("BpClient.addUser() should not allow an operation");
		}catch(Exception e) {};
		
		//Test all the Client methods
		//1. Check if the script can load the business plan on the client, edit, and restore it back.
    	int yearNow = 2020;
    	String bpStr;
    	String restoredBpStr;
    	BusinessPlan bp = server.getPlanByYear(defaultAdminName, defaultAdminPw, yearNow);
    	BusinessPlan restoredBp = server.getPlanByYear(defaultAdminName, defaultAdminPw, yearNow);
    	bpStr = bp.toString();
    	restoredBpStr = restoredBp.toString();
    	assertEquals(bpStr, restoredBpStr);
    	//2. Create a new business plan and save it.
    	int yearPrev = 2019;
    	BusinessPlan newBp = server.createNewPlan(defaultAdminName, defaultAdminPw, "testPlan", yearPrev);
    	boolean planExists = server.planExists(defaultAdminName, defaultAdminPw, yearPrev);
    	assertEquals(false, planExists);
    	server.savePlan(defaultAdminName, defaultAdminPw,newBp);
    	planExists = server.planExists(defaultAdminName, defaultAdminPw, yearPrev);
    	assertEquals(true, planExists);
    	BusinessPlan restoredNewPlan = server.getPlanByYear(defaultAdminName, defaultAdminPw, yearPrev);
		
		
		System.out.println("SUCCESS: All test passed in BPServerTest");
	};
	


}
