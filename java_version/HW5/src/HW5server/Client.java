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
        private ArrayList<String> usersInChat;       
	public Client(Socket s,ArrayList<ArrayList<String> > usernamesList, ArrayList<String> chattingList)
	{
		socket = s;//INSTANTIATE THE SOCKET
                usernames=usernamesList;
                usersInChat=chattingList;
	}
        public static String whosonline(ArrayList<ArrayList<String> > input){
            String result="";
            for(int i =0; i<input.size(); i++){
                result=result.concat(input.get(i).get(0).concat(" "));
            }
            return result;
        }
	public static int indexOfName(ArrayList<ArrayList<String> > input, String username){
            int i=0;
            while(i<input.size()&&input.get(i).get(0).compareTo(username)!=0){
                i++;
            }
            
            return i;
        }
        public boolean isUserOnline(ArrayList<ArrayList<String > > input,String username){
            for (int i=0; i<input.size();i++) {
                //System.out.println(input.get(i).get(0)+" "+username);
                if (input.get(i).get(0).compareTo(username) == 0) {
                    return true;
                    
                }
            }
            return false;
        }
        public boolean isUserChatting(ArrayList<String> input,String username){
            for (int i=0; i<input.size(); i++) {
                if (input.get(i).compareTo(username) == 0) {
                    return true;
                }
            }
            return false;
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
                                            usernames.remove(indexOfName(usernames,input.substring(0,input.length()-1)));
                                            System.out.println("Client logout: " + input.substring(0,input.length()-1));
                                            out.println("Logout successful");
                                            out.flush();
                                        }
                                        else if(input.endsWith("2")){
                                            StringTokenizer tokens = new StringTokenizer(input.substring(15, input.length()-2),",");
                                            if(tokens.countTokens()>2){
                                                String HOST=tokens.nextToken();
                                                String username=tokens.nextToken();
                                                String targetusername=tokens.nextToken();
                                                System.out.println("1"+isUserOnline(usernames,targetusername));
                                                System.out.println(usersInChat);
                                                //System.out.println("3"+isUserChatting(usersInChat,username));
                                                System.out.println("4"+isUserChatting(usersInChat,targetusername));
                                                if(isUserOnline(usernames,targetusername)&&usersInChat.contains(targetusername)==false){
                                                    System.out.println(usernames.get(indexOfName(usernames,targetusername)).get(2) + "  " + usernames.get(indexOfName(usernames,targetusername)).get(1));
                                                    Socket temp=new Socket(usernames.get(indexOfName(usernames,targetusername)).get(2),Integer.parseInt(usernames.get(indexOfName(usernames,targetusername)).get(1)));
                                                    Scanner tempin=new Scanner(temp.getInputStream());
                                                    PrintWriter tempout=new PrintWriter(temp.getOutputStream());
                                                    out.println("userisclear");
                                                    out.flush();
                                                    tempout.println(username+" request to chat with you [y/n]");
                                                    tempout.flush();
                                                    String response=tempin.nextLine();
                                                    System.out.println(response);
                                                    if (response.compareTo("y")==0){
                                                        out.println("User has accepted your chat...begin chatting");
                                                        out.flush();
                                                        out.println("yes");
                                                        out.flush();
                                                        out.println(usernames.get(indexOfName(usernames,targetusername)).get(2));
                                                        out.flush();
                                                        out.println(Integer.parseInt(usernames.get(indexOfName(usernames,targetusername)).get(1)));
                                                        out.flush();
                                                        usersInChat.add(targetusername);
                                                        usersInChat.add(username);
                                                        in.nextLine();
                                                        usersInChat.remove(targetusername);
                                                        usersInChat.remove(username);
                                                    }
                                                    else if(response.compareTo("n")==0){
                                                        out.println("User has denied your chat");
                                                        out.flush();
                                                        out.println("no");
                                                        out.flush();
                                                    }
                                                    else{
                                                        out.println("User did not answer [y/n] try sending again");
                                                        out.flush();
                                                    }
                                                }
                                                else if(isUserOnline(usernames,targetusername)){
                                                    out.println("userisbusy");
                                                    out.flush();
                                                }
                                                else{
                                                    out.println("userisoffline");
                                                    out.flush();
                                                }
                                                     
                                                
                                            }
                                            //System.out.println("Client logged in as: " + input.substring(0,input.length()-1));
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



