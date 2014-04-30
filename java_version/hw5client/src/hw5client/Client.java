/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hw5client;

/**
 *
 * @author Jason
 */
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {

	private Socket socket;//MAKE SOCKET INSTANCE VARIABLE
	
	public Client(Socket s)
	{
		socket = s;//INSTANTIATE THE INSTANCE VARIABLE
	}
	
	@Override
	public void run()//INHERIT THE RUN METHOD FROM THE Runnable INTERFACE
	{
		Boolean loggedin=false;
                try
		{
			Scanner chat = new Scanner(System.in);//GET THE INPUT FROM THE CMD
			Scanner in = new Scanner(socket.getInputStream());//GET THE CLIENTS INPUT STREAM (USED TO READ DATA SENT FROM THE SERVER)
			PrintWriter out = new PrintWriter(socket.getOutputStream());//GET THE CLIENTS OUTPUT STREAM (USED TO SEND DATA TO THE SERVER)
			/*error=0
                          login=1
                          username=2
                        */
			while (true)//WHILE THE PROGRAM IS RUNNING
			{	
                                
                                
                                    String input = chat.nextLine();

                                            //SET NEW VARIABLE input TO THE VALUE OF WHAT THE CLIENT TYPED IN
                                    if(input.length()>7){
                                        if(input.substring(0, 6).compareTo("login(")==0){
                                            //System.out.println("test");
                                            out.println(input.substring(6, input.length()-1).concat("1"));
                                        }
                                    }
                                    else{
                                        //System.out.println("test2");
                                        out.println(input);
                                    }//SEND IT TO THE SERVER
                                    out.flush();//FLUSH THE STREAM

                                    if(in.hasNext())//IF THE SERVER SENT US SOMETHING
                                            System.out.println(in.nextLine());//PRINT IT OUT
                                }
			}
		
		catch (Exception e)
		{
			e.printStackTrace();//MOST LIKELY WONT BE AN ERROR, GOOD PRACTICE TO CATCH THOUGH
                } 
	}

}

