/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package HW5server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
/**
 *
 * @author Jason
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Hw5 {

	public static void main(String[] args) throws IOException {
		ArrayList<String> usernames=new ArrayList<String>();
                try 
		{
			final int PORT = 50054;//SET NEW CONSTANT VARIABLE: PORT
			ServerSocket server = new ServerSocket(PORT); //SET PORT NUMBER
			System.out.println("Waiting for clients...");//AT THE START PRINT THIS
                        
			while (true)//WHILE THE PROGRAM IS RUNNING
			{												
				Socket s = server.accept();//ACCEPT SOCKETS(CLIENTS) TRYING TO CONNECT
				Scanner in = new Scanner(s.getInputStream());//GET THE CLIENTS INPUT STREAM (USED TO READ DATA SENT FROM THE SERVER)
                                PrintWriter out = new PrintWriter(s.getOutputStream());
                                String input="";
                                if(in.hasNext()){
                                    input=in.nextLine();
                                }
                                if(!usernames.contains(input)){
                                    System.out.println("Client connected from " + s.getLocalAddress().getHostName()+" using name "+input);	//	TELL THEM THAT THE CLIENT CONNECTED
                                    usernames.add(input);
                                    out.println("notexist");
                                    out.flush();
                                    Client chat = new Client(s,usernames);//CREATE A NEW CLIENT OBJECT
                                    Thread t = new Thread(chat);//MAKE A NEW THREAD
                                    t.start();//START THE THREAD
                                }
                                else{
                                    out.println("exist");
                                    out.flush();
                                    System.out.println("didnt exist");
                                }
			}
		} 
		catch (Exception e) 
		{
			System.out.println("An error occured.");//IF AN ERROR OCCURED THEN PRINT IT
                        e.printStackTrace();
		}
	}

}


