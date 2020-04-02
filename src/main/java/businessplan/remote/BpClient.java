package businessplan.remote;

import java.rmi.Naming;

import businessplan.main.BusinessPlan;

public class BpClient
{
	private BpServer server;
	private String userName;
	private String password;

	public BpClient(String serverUrl) {
        try {
        	//requestURL = "rmi://127.0.0.1/RemoteHello";
            BpServer stub = (BpServer)Naming.lookup(serverUrl);
            this.server = stub;
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
	}

	public BusinessPlan getPlanByYear(int year)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public boolean login(String userName, String pw)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	public BusinessPlan createNewPlan(String planName, int year)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void savePlan(BusinessPlan businessPlan)
	{
		// TODO Auto-generated method stub
		
	}
	
	public boolean planExists(int year) {
		return false;
	}

	public boolean addUser(User newUser)
	{
		// TODO Auto-generated method stub
		boolean success = false;
		return success;
	}

	public void turnOffEditingFor(int year)
	{
		// TODO Auto-generated method stub
		
	}
	
	public void turnOnEditingFor(int year)
	{
		// TODO Auto-generated method stub
		
	}

}
