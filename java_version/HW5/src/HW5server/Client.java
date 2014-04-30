/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package HW5server;

/**
 *
 * @author Jason
 */
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Client implements Runnable{

	private Socket socket;//SOCKET INSTANCE VARIABLE
	private ArrayList<String> usernames;
	public Client(Socket s,ArrayList<String> usernamesList)
	{
		socket = s;//INSTANTIATE THE SOCKET
                usernames=usernamesList;
	}
	
	@Override
	public void run() //(IMPLEMENTED FROM THE RUNNABLE INTERFACE)
	{
                
		try //HAVE TO HAVE THIS FOR THE in AND out VARIABLES
		{
			Scanner in = new Scanner(socket.getInputStream());//GET THE SOCKETS INPUT STREAM (THE STREAM THAT YOU WILL GET WHAT THEY TYPE FROM)
			PrintWriter out = new PrintWriter(socket.getOutputStream());//GET THE SOCKETS OUTPUT STREAM (THE STREAM YOU WILL SEND INFORMATION TO THEM FROM)
			
			while (true)//WHILE THE PROGRAM IS RUNNING
			{		
				if (in.hasNext())
				{
					String input = in.nextLine();//IF THERE IS INPUT THEN MAKE A NEW VARIABLE input AND READ WHAT THEY TYPED
                                        if(input.endsWith("1")){
                                            usernames.remove(input.substring(0, input.length()-1));
                                            System.out.println("Client logout: " + input.substring(0,input.length()-1));
                                        }
                                        else if(input.endsWith("2")){
                                            
                                            System.out.println("Client logged in as: " + input.substring(0,input.length()-1));
                                        }
                                        else{
                                             System.out.println("Client Said: " + input.substring(0,input.length()));
                                        }//PRINT IT OUT TO THE SCREEN
					out.println("You Said: " + input.substring(0,input.length()));//RESEND IT TO THE CLIENT
					out.flush();//FLUSH THE STREAM
				}
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();//MOST LIKELY THERE WONT BE AN ERROR BUT ITS GOOD TO CATCH
		}	
	}

}



