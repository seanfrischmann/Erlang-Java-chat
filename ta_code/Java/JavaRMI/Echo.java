package JavaRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

//interface to access the echoMessage method of server objects


public interface Echo extends Remote {
	String echoMessage(String message) throws RemoteException;
}
