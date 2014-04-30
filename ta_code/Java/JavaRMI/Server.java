package JavaRMI;
import java.rmi.Naming;

public class Server {

	public static void main(String[] args) {
		try { 
			 System.out.println("Server: Registering Echo Service"); 
			 EchoImpl remote = new EchoImpl();
			 Naming.rebind("EchoService", remote); 
			 System.out.println("Server: Ready..."); 
			 } 
			 catch (Exception e) { 
			 System.out.println("Server: Failed to register Echo Service: " + e); 
			 } 
		}
}
