import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class FTP_Server {
	
	 private static ServerSocket serverSocket_command;
	 private static ServerSocket serverSocket_data;
	 private static Socket clientSocket = null;
	 private static Socket clientSocket_data = null;
	 static Scanner input1;
	 static java.util.List<String> list1=new ArrayList<>();
	
	
	public static void main(String[] args)
	{
		try {
		    serverSocket_command = new ServerSocket(21);
		    serverSocket_data = new ServerSocket(20);
		    System.out.println("FTP Server started.");
		} catch (Exception e) {
		    System.err.println("Cannot start FTP server on this port");
		    System.exit(1);
		}

		while (true) {
		    try {
		        clientSocket = serverSocket_command.accept();
		        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        	DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
		        
		        if(clientSocket.isConnected())
		        {
		        	System.out.println("Accepted connection from : " + clientSocket + " Now checking authentication");
		        	
		        	String returnMessage = null;
			        //Authentication
		        	String user = inFromClient.readLine();
		        	inFromClient.close();
	 
	                //Multiplying the number by 2 and forming the return message
	                File file1 = new File("E:/Mayur/UTA/3rd Sem/Advanced SE/FTP/AdvancedSE/uname.txt");
	                input1 = new Scanner(file1);
	                while (input1.hasNextLine()) {
	                    list1.add(input1.nextLine());
	                }
	                input1.close();
	                if(list1.contains(user))
	                {
	                	 System.out.println("Authenticated");
	                }
	                	
	                else
	                {
	                	System.out.println("User is a hacker ass");
	                	clientSocket.shutdownInput();
	                	clientSocket.close();
	                }
	                
	                if(!clientSocket.isInputShutdown())
	                {
	                	 clientSocket_data = serverSocket_data.accept();
			        	 System.out.println("Data connection started on : " + clientSocket_data);
			        	 Thread t = new Thread(new ClientService(clientSocket_data));
			        	 t.start();
	                }
	                
		        }
		      

		    } catch (Exception e) {
		        System.err.println("Error in connection attempt.");
		     }
		}
	}
}
