/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hw5server;
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

public class Hw5server {
        public static Boolean searchlist(ArrayList<ArrayList<String> > input, String search, int j ){
            Boolean result=true;
            for(int i=0; i<input.size(); i++){
                if(input.get(i).get(j).compareTo(search)==0){
                    result=false;
                }
            }
            return result;
        }
	public static void main(String[] args) throws IOException {
		ArrayList<ArrayList<String> > usernames=new ArrayList<ArrayList<String> >();
                ArrayList<String> isUserChatting= new ArrayList<String>();
                try 
		{
			int PORT = 50054;//SET NEW CONSTANT VARIABLE: PORT
			ServerSocket server = new ServerSocket(PORT); //SET PORT NUMBER
			System.out.println("Waiting for clients...");//AT THE START PRINT THIS
                        
			while (true)//WHILE THE PROGRAM IS RUNNING
			{												
				Socket s = server.accept();//ACCEPT SOCKETS(CLIENTS) TRYING TO CONNECT
				Scanner in = new Scanner(s.getInputStream());//GET THE CLIENTS INPUT STREAM (USED TO READ DATA SENT FROM THE SERVER)
                                PrintWriter out = new PrintWriter(s.getOutputStream());
                                String input="";
                                String input2="";
                                if(in.hasNext()){
                                    input=in.nextLine();
                                }
                                if(in.hasNext()){
                                    input2=in.nextLine();
                                }
                                if(searchlist(usernames,input,0)){
                                    System.out.println("Client connected from " + s.getPort()+" on server "+s.getLocalAddress().getHostName()+ " using name "+input);	//	TELL THEM THAT THE CLIENT CONNECTED
                                    ArrayList<String> temp = new ArrayList<String>();
                                    PORT=PORT+128;
                                    temp.add(input);
                                    temp.add(Integer.toString(PORT));
                                    temp.add(s.getLocalAddress().getHostName());
                                    temp.add(input2);
                                    usernames.add(temp);
                                    out.println("notexist");
                                    out.flush();
                                    //PORT=PORT+128;
                                    if(in.nextLine().compareTo("send")==0){
                                        out.println(Integer.toString(PORT));
                                        out.flush();
                                    }
                                    if(in.nextLine().compareTo("sending")==0){
                                        out.println(s.getLocalAddress().getHostName());
                                        out.flush();
                                    }
                                    
                                    Client chat = new Client(s,usernames,isUserChatting);//CREATE A NEW CLIENT OBJECT
                                    Thread t = new Thread(chat);//MAKE A NEW THREAD
                                    t.start();//START THE THREAD
                                }
                                else{
                                    out.println("exist");
                                    out.flush();
                                    System.out.println("user already exists");
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


