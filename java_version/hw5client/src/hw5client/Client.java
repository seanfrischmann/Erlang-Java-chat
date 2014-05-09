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
import java.io.BufferedReader;
import java.io.InputStreamReader;
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
        private String localHost;
	public Client(Socket s,String p,String n,String q)
	{
            localHost=q;
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
			BufferedReader chat = new BufferedReader(new InputStreamReader(System.in));//GET THE INPUT FROM THE CMD
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//GET THE CLIENTS INPUT STREAM (USED TO READ DATA SENT FROM THE SERVER)
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
                                            try{
                                                        server.setSoTimeout(300); 
                                                        clientSocket=server.accept();
                                                        BufferedReader socketin=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                                                        PrintWriter socketout=new PrintWriter(clientSocket.getOutputStream());

                                                        System.out.println(socketin.readLine());
                                                        socketout.println(chat.readLine());  //sends back response
                                                        socketout.flush();
                                                        clientSocket=server.accept();
                                                        BufferedReader chatin=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                                                        PrintWriter chatout=new PrintWriter(clientSocket.getOutputStream());
                                                        Boolean chatting=true;
                                                        System.out.println("Begin Chatting...");
                                                        String username=chatin.readLine();
                                                        String message=new String();
                                                        while(chatting){
                                                                if(chatin.ready()){
                                                                    message=chatin.readLine();
                                                                    if(message.compareTo("quit()")==0){
                                                                        System.out.println("chat has been ended");
                                                                        System.out.flush();
                                                                        clientSocket.close();
                                                                        break;
                                                                    }
                                                                    else{
                                                                        System.out.println(message);
                                                                    }
                                                                    
                                                                }
                                                                if(chat.ready()){
                                                                    message=chat.readLine();
                                                                    if(message.compareTo("quit()")==0){
                                                                        chatout.println("quit()");
                                                                        chatout.flush();
                                                                        System.out.println("chat has been ended");
                                                                        System.out.flush();
                                                                        clientSocket.close();
                                                                        break;
                                                                    }
                                                                    else{
                                                                        chatout.println(username+" said: "+message);
                                                                        chatout.flush();
                                                                    }
                                                                }
                                                                 
                                                        }
                                                        
                                                 } catch (SocketTimeoutException ste){
                                                        //System.out.println("you have no chat requests");

                                                 }
                                        if(chat.ready()){    
                                            String input = chat.readLine();


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

                                                System.out.println(in.readLine());
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
                                                    String result=in.readLine();
                                                    //retrieves response
                                                    if(result.compareTo("userisclear")==0){
                                                        System.out.println(in.readLine());
                                                        String response=in.readLine();
                                                        if(response.compareTo("yes")==0){
                                                            System.out.println("Begin chatting...");
                                                            String host=in.readLine();
                                                            String port=in.readLine();
                                                            Socket temp=new Socket(host,Integer.valueOf(port));
                                                            BufferedReader chatin=new BufferedReader(new InputStreamReader(temp.getInputStream()));
                                                            PrintWriter chatout=new PrintWriter(temp.getOutputStream());
                                                            boolean chatting=true;
                                                            chatout.println(targetusername);
                                                            chatout.flush();
                                                            String message=new String();
                                                            while(chatting){
                                                                if(chatin.ready()){
                                                                    message=chatin.readLine();
                                                                    if(message.compareTo("quit()")==0){
                                                                        System.out.println(targetusername+" quit, chat ended");
                                                                        System.out.flush();
                                                                        out.println("quit()");
                                                                        out.flush();
                                                                        temp.close();
                                                                        break;
                                                                    }
                                                                    else{
                                                                        System.out.println(message);
                                                                    }
                                                                    
                                                                }
                                                                if(chat.ready()){
                                                                    message=chat.readLine();
                                                                    if(message.compareTo("quit()")==0){
                                                                        chatout.println("quit()");
                                                                        chatout.flush();
                                                                        System.out.println("chat with " + targetusername+" ended");
                                                                        System.out.flush();
                                                                        out.println("quit()");
                                                                        out.flush();
                                                                        temp.close();
                                                                        break;
                                                                    }
                                                                    else{
                                                                        chatout.println(username+" said: "+message);
                                                                        chatout.flush();
                                                                    }
                                                                }

                                                            }

                                                        }
                                                        else{
                                                            
                                                        }
                                                    }
                                                    else if(result.compareTo("userisbusy")==0){
                                                        System.out.println(targetusername+" is already in chat");
                                                    }
                                                    else{
                                                        System.out.println(targetusername+" is offline");
                                                    }

                                                    //username=username.substring(0, username.length());
                                                }
                                            }
                                            
                                            
                                            else if(input.length()>10&&input.substring(0,11).compareTo("whosOnline(")==0){
                                                out.println("whosOnline(");
                                                out.flush();
                                                System.out.println(in.readLine());
                                            }


                                            else{
                                                //System.out.println("test2");
                                                System.out.println("error");
                                                out.flush();
                                            }
                                        }    
                                     
                        }
                }
		
		catch (Exception e)
		{
			e.printStackTrace();//MOST LIKELY WONT BE AN ERROR, GOOD PRACTICE TO CATCH THOUGH
                } 
	}

}


