package businessplan.remote;

import java.io.Serializable;

public class User implements Serializable
{
    private static final long serialVersionUID = 227L;
	private String userName;
	private String password;
	private String department;
	private boolean isAdmin;
	public User(String userName, String password, String department, boolean isAdmin)
	{
		this.setUserName(userName);
		this.setPassword(password);
		this.setDepartment(department);
		this.setAdmin(isAdmin);
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getDepartment()
	{
		return department;
	}
	public void setDepartment(String department)
	{
		this.department = department;
	}
	public boolean isAdmin()
	{
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin)
	{
		this.isAdmin = isAdmin;
	}

}
