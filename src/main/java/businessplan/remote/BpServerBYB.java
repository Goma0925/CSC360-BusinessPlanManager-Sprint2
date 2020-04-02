package businessplan.remote;

import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;

import businessplan.main.BusinessPlan;

public class BpServerBYB  extends UnicastRemoteObject implements BpServer{
	private boolean isRunning = false;
	private static final long serialVersionUID = -5331251289904714278L;
	private Hashtable<?, ?> userInfo = new Hashtable<?, ?>();

	public BpServerBYB(String defaultAdminName, String defaultAdminPw){
		//Add admin user to userInfo
	};
	
	public void startServer(String serverBindName) throws RemoteException, AlreadyBoundException
	{
		// Code to start a registry (in slides)
		// 
		System.out.println("Server started.");
		try {
			BpServerBYB server = this;
			//ConcreteRemoteObject stub = (ConcreteRemoteObject) UnicastRemoteObject.exportObject(remoteObj, 0);
			Naming.rebind(serverBindName, server);  
			this.setRunning(true);
            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
	};


	public BusinessPlan getPlanByYear(String userName, String pw, int year)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public boolean login(String userName, String pw)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public BusinessPlan createNewPlan(String userName, String pw, String planName, int year)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void savePlan(String userName, String pw, BusinessPlan businessPlan)
	{
		// TODO Auto-generated method stub
		
	}

	public boolean planExists(String userName, String pw, int year)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean addUser(String userName, String pw, User newUser)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public void turnOffEditingFor(String userName, String pw, int year)
	{
		// TODO Auto-generated method stub
		
	}

	public void turnOEditingFor(String userName, String pw, int year)
	{
		// TODO Auto-generated method stub
		
	}

	public boolean isRunning()
	{
		return this.isRunning;
	}

	public void setRunning(boolean isRunning)
	{
		this.isRunning = isRunning;
	}
}