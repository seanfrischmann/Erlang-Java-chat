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
import java.util.StringTokenizer;

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
                                    if(input.length()>10){
                                        if(input.substring(0, 10).compareTo("goOffline(")==0){
                                            StringTokenizer tokens = new StringTokenizer(input.substring(10, input.length()-1),",");
                                            if(tokens.countTokens()>1){
                                                String HOST=tokens.nextToken();
                                                String username=tokens.nextToken();
                                               // username=username.substring(0, username.length()-1);
                                            //System.out.println("test");
                                            System.out.println("You logged off of " + HOST + " with username " +username);
                                            out.println(username.concat("1"));
                                            out.flush();
                                           
                                            System.out.println(in.nextLine());
                                            System.exit(0);
                                            }
                                        }
                                        else if(input.length()>15&&input.substring(0,16).compareTo("requestChatWith(")==0){
                                            StringTokenizer tokens = new StringTokenizer(input.substring(15, input.length()-1),",");
                                            if(tokens.countTokens()>1){
                                                String HOST=tokens.nextToken();
                                                String username=tokens.nextToken();
                                                String targetusername=tokens.nextToken();
                                                System.out.println("You sent a chat request to "+targetusername+" on "+HOST);
                                                out.println(input.concat("2"));
                                                //username=username.substring(0, username.length()-1);
                                            }
                                        }
                                        else if(input.substring(0,11).compareTo("whosOnline(")==0){
                                            out.println("whosOnline(");
                                            out.flush();
                                            System.out.println(in.nextLine());
                                        }
                                    }
                                    else{
                                        //System.out.println("test2");
                                        System.out.println("error");
                                    }
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


