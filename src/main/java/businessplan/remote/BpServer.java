package businessplan.remote;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import businessplan.main.BusinessPlan;

public interface BpServer extends Remote{
	public void startServer(String serverObjBindName) throws RemoteException, AlreadyBoundException;
//
//	public BusinessPlan getPlanByYear(String userName, String pw, int year);
//
//	public boolean login(String userName, String pw);
//	
//	public BusinessPlan createNewPlan(String userName, String pw, String planName, int year);
//
//	public void savePlan(String userName, String pw, BusinessPlan businessPlan);
//	
//	public boolean planExists(String userName, String pw, int year);
//
//	public boolean addUser(String userName, String pw, User newUser);
//
//	public void turnOffEditingFor(String userName, String pw, int year);
//	
//	public void turnOEditingFor(String userName, String pw, int year);
}
