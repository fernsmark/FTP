import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Scanner;


public class Server extends Thread  
{  
  
    ServerSocket server = null; //listen 
    Socket sk = null;  
    BufferedReader br = null;  
    PrintWriter pw = null;  
    int port;
    public Server(int p)  
    {  	
    	port=p;
        try  
        {  
            server = new ServerSocket(port);  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
    }  
  
    public void run()  
    {  
  
        while (true)  
            {  
                System.out.println("Listenning...");  
                try  
                {   
                    sk = server.accept();  
                    if(sk!=null){
	                    ServerThread th = new ServerThread(sk);  
	                    th.start();  
                    }
                    sleep(1000);  
                }  
                catch (Exception e)  
                {  
                    e.printStackTrace();  
                }  
                  
            }  
    }  
  
    public static void main(String [] args) throws Exception  
    {  
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	System.out.print("Enter port :");
    	String p=br.readLine();
        new Server(Integer.parseInt(p)).start();  
    }  

}
