# Sprint 2 Implementation Description

![Sprint%202%20Implementation%20Description/UMI.png](Sprint%202%20Implementation%20Description/UMI.png)

- **BpServer class**
    - An interface for the BYServerBYB. It implements the RMI remote class to be able to be controlled remotely through BpClient.
    - It defines the methods needed for a server classs.
- **BpServerBYB class**
    - This class implements the BpServer interface
    - It has all the user authentications and business plan management functionality, which is specified as a sever functionality in the assignment detail doc.
    - Each method takes the user's name and password in addition to the parameters needed for the operation so that it can check if the user has valid identity information before the user runs a method.
    - Each method raises a corresponding error if there is an exceptional case in order to tell the application user what is wrong with the input parameters. For example, isValidAuth() method raises a NotValidUserException if the user does not exists on the server.
    - The list below is administrator methods that are only allowed to execute if the given user has the isAdmin=true.
    
![Sprint%202%20Implementation%20Description/UMI.png](Sprint%202%20Implementation%20Description/admin-methods.png)

- **BpClient class**
    - This class is merely a capsule for BpServer. It has coressponding functions to the ones in BpServerBYB. The purpose of this class is to be used by a user of this application to retrieve, post, and edit business plan data on the server.
    - It provides a method to allow an application user to login by login() method. After than, the application user only have to run methods on the client to retrieve, post, and edit business plan data on the server.
    - Note that createNewPlan() method takes a child class of BusinessPlan abstract class to create a new plan. This is to give some flexibility for the application user to choose different types of businessplan, such as VMOSA or other custom business plan template class.
    - N**ote that the testing for this class is fairly simple because all the main logics are built in BpServerBYB class. BpClient test is aimed at testing if all the methods on BpServerBYB is executable from the BpClient class. The logical test of the program is written in BpServerTest.**
    - Note this class represents **a client application, not a business plan user,** meaning that BpClient represents an aplication like a browser or other application that uses this class to manipulate business plan data.

- User class
    - The purpose of this is to represent the information of a business plan application user.
    - This class is used both in BpServer and BpClient to create a user and keep track of the user information such as his/her department.
    - Note this class does not represent a client in terms of server-client application.
