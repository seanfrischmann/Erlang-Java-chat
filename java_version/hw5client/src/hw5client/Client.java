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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Client implements Runnable {

	private Socket socket;//MAKE SOCKET INSTANCE VARIABLE
	private String PORT;
        private String hostname;
	public Client(Socket s,String p,String n)
	{
            hostname=n;
            PORT=p;
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
			PrintWriter out = new PrintWriter(socket.getOutputStream());//GET THE CLIENTS OUTPUT STREAM (USED TO SEND DATA TO THE SERVE
                        Socket clientSocket;
                      
                        //System.out.print(PORT);
                        ServerSocket server= new ServerSocket(Integer.parseInt(PORT));
                        //server.setSoTimeout(1000);
                        //Socket s = server.accept();//ACCEPT SOCKETS(CLIENTS) TRYING TO CONNECT
			//Scanner serverin = new Scanner(s.getInputStream());//GET THE CLIENTS INPUT STREAM (USED TO READ DATA SENT FROM THE SERVER)
                        //PrintWriter serverout = new PrintWriter(s.getOutputStream());
                        /*error=0
                          login=1
                          username=2
                        */
			while(true)//WHILE THE PROGRAM IS RUNNING
			{	
                      
                                            String input = chat.nextLine();


                                                //SET NEW VARIABLE input TO THE VALUE OF WHAT THE CLIENT TYPED IN

                                            if(input.length()>9&&input.substring(0, 10).compareTo("goOffline(")==0){
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
                                                StringTokenizer tokens = new StringTokenizer(input.substring(16, input.length()-1),",");
                                                if(tokens.countTokens()>2){
                                                    String HOST=tokens.nextToken();
                                                    String username=tokens.nextToken();
                                                    String targetusername=tokens.nextToken();
                                                    System.out.println("You sent a chat request to "+targetusername+" on "+HOST);
                                                    System.out.flush();
                                                    out.println(input.concat("2"));
                                                    out.flush();
                                                    System.out.println(in.nextLine()); //retrieves response
                                                    if(in.nextLine().compareTo("yes")==0){
                                                        String host=in.nextLine();
                                                        String port=in.nextLine();
                                                        Socket temp=new Socket(host,Integer.valueOf(port));
                                                        Scanner chatin=new Scanner(temp.getInputStream());
                                                        PrintWriter chatout=new PrintWriter(temp.getOutputStream());
                                                        boolean chatting=true;
                                                        while(chatting){
                                                            //if(chatin.hasNext()){
                                                                System.out.println(chatin.nextLine());
                                                            //}
                                                            //if(chat.hasNext()){
                                                                chatout.println(chat.nextLine());
                                                                chatout.flush();
                                                            //}
                                                                 
                                                        }
                                                        
                                                    }

                                                    //username=username.substring(0, username.length());
                                                }
                                            }
                                            else if(input.compareTo("check()")==0){
                                                try{
                                                    server.setSoTimeout(100); 
                                                    clientSocket=server.accept();
                                                    Scanner socketin=new Scanner(clientSocket.getInputStream());
                                                    PrintWriter socketout=new PrintWriter(clientSocket.getOutputStream());
                                                    if(socketin.hasNext()){
                                                        System.out.println(socketin.nextLine());
                                                        socketout.println(chat.nextLine());  //sends back response
                                                        socketout.flush();
                                                        clientSocket=server.accept();
                                                        Scanner chatin=new Scanner(clientSocket.getInputStream());
                                                        PrintWriter chatout=new PrintWriter(clientSocket.getOutputStream());
                                                        Boolean chatting=true;
                                                        while(chatting){
                                                            //if(chatin.hasNext()){
                                                            //    System.out.println(chatin.nextLine());
                                                            //}
                                                            //if(chat.hasNext()){
                                                                chatout.println(chat.nextLine());
                                                                chatout.flush();
                                                            //}
                                                                 
                                                        }
                                                        
                                                        
                                                    }
                                                    else{
                                                        System.out.println("you have no chat requests");
                                                    }
                                                 } catch (SocketTimeoutException ste){
                                                        System.out.println("you have no chat requests");

                                                 }
                                            }
                                            
                                            else if(input.length()>10&&input.substring(0,11).compareTo("whosOnline(")==0){
                                                out.println("whosOnline(");
                                                out.flush();
                                                System.out.println(in.nextLine());
                                            }


                                            else{
                                                //System.out.println("test2");
                                                System.out.println("error");
                                                out.flush();
                                            }

                                     
                        }
                }
		
		catch (Exception e)
		{
			e.printStackTrace();//MOST LIKELY WONT BE AN ERROR, GOOD PRACTICE TO CATCH THOUGH
                } 
	}

}


