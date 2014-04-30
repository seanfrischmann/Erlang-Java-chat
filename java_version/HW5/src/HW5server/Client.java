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
import java.util.StringTokenizer;


public class Client implements Runnable{

	private Socket socket;//SOCKET INSTANCE VARIABLE
	private ArrayList<ArrayList<String> > usernames;
	public Client(Socket s,ArrayList<ArrayList<String> > usernamesList)
	{
		socket = s;//INSTANTIATE THE SOCKET
                usernames=usernamesList;
	}
        public static String whosonline(ArrayList<ArrayList<String> > input){
            String result="";
            for(int i =0; i<input.size(); i++){
                result=result.concat(input.get(i).get(0).concat(" "));
            }
            return result;
        }
	public static int removeHelper(ArrayList<ArrayList<String> > input, String username){
            int i=0;
            while(input.get(i).get(0).compareTo(username)!=0){
                i++;
            }
            return i;
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
                                            usernames.remove(removeHelper(usernames,input.substring(0,input.length()-1)));
                                            System.out.println("Client logout: " + input.substring(0,input.length()-1));
                                        }
                                        else if(input.endsWith("2")){
                                            StringTokenizer tokens = new StringTokenizer(input.substring(15, input.length()-1),",");
                                            if(tokens.countTokens()>2){
                                                String HOST=tokens.nextToken();
                                                String username=tokens.nextToken();
                                                String targetusername=tokens.nextToken();
                                                
                                            }
                                            System.out.println("Client logged in as: " + input.substring(0,input.length()-1));
                                        }
                                        else if(input.compareTo("whosOnline(")==0){
                                            out.println(whosonline(usernames));
                                        }
                                        else{
                                             System.out.println("Client Said: " + input.substring(0,input.length()));
                                             out.println("You Said: " + input.substring(0,input.length()));//RESEND IT TO THE CLIENT
                                        }//PRINT IT OUT TO THE SCREEN
					
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



