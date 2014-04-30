package JavaRMI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;

public class Client {

	public static void main(String[] args) {
		
	// Get a remote reference to the RMIExampleImpl class 
		String strName = "rmi://localhost/EchoService"; 
		Echo RemEcho = null; 
		
		try { 
			RemEcho = (Echo)Naming.lookup(strName); 
		} catch (Exception e) { 
			System.out.println("Client: Exception thrown looking up " + strName); 
			System.exit(1); 
		} 
		
		 // Send a messge to the remote object 
		 try {
			 BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			 String userInput;
	         while ((userInput = stdIn.readLine()) != null) {
	        	 String serverResponse = RemEcho.echoMessage(userInput); 
	        	 System.out.println("From Server: "+ serverResponse);
	         }
		 } catch (Exception e) { 
			 	System.out.println("Client: Exception thrown calling EchoMessage()."); 
			 	System.exit(1); 
		 } 
	}
		
		

}
