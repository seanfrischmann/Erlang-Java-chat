package JavaRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class EchoImpl extends UnicastRemoteObject implements Echo {
	public EchoImpl() throws RemoteException { 
		super(); 
	};

	 @Override
	public String echoMessage(String message) throws RemoteException {
		String returnMessage = message;
		System.out.println("Server echoMessage method invoked!");
		return returnMessage;
	}

}
