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
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;


public class Hw5client {

	private final static int PORT = 50054;//SET A CONSTANT VARIABLE PORT
	//SET A CONSTANT VARIABLE HOST
	
	public static void main(String[] args) throws IOException
	{
                Boolean loggedin=false;
                while(loggedin==false){
                    Scanner chat = new Scanner(System.in);
                    System.out.println("cmd: ");
                    String input = chat.nextLine();
                    String HOST="";
                    String username="";
                    if(input.length()>9&&input.substring(0,9).compareTo("goOnline(")==0){
                        StringTokenizer tokens = new StringTokenizer(input.substring(9, input.length()),",");
                        if(tokens.countTokens()>1){
                            HOST=tokens.nextToken();
                            username=tokens.nextToken();
                            username=username.substring(0, username.length()-1);
                        }
                    //System.out.println(HOST+username);
                    try 
                    {

                            Socket s = new Socket(HOST, PORT);//CONNECT TO THE SERVER
                            Scanner in = new Scanner(s.getInputStream());//GET THE CLIENTS INPUT STREAM (USED TO READ DATA SENT FROM THE SERVER)
                            PrintWriter out = new PrintWriter(s.getOutputStream());//GET THE CLIENTS OUTPUT STREAM (USED TO SEND DATA TO THE SERVER)
                            out.println(username);
                            out.flush();
                            
                            String result=in.nextLine();
                            //System.out.println(result);
                            if(result.compareTo("notexist")==0){
                                System.out.println("You connected to " + HOST + " with username " +username);
                                //IF CONNECTED THEN PRINT IT OUT
                                loggedin=true;
                                Client client = new Client(s);//START NEW CLIENT OBJECT
                                
                                Thread t = new Thread(client);//INITIATE NEW THREAD
                                
                                t.start();//START THREAD
                            }
                            else{
                                System.out.println("user already exists, please use different name");
                            }
                    } 
                    catch (Exception noServer)//IF DIDNT CONNECT PRINT THAT THEY DIDNT
                    {
                            System.out.println("The server might not be up at this time.");
                            System.out.println("Please try again later.");
                    }
                    }
                    else{
                        System.out.println("use goOnline(Server,Username) to login");
                    }
                }
	}
}



