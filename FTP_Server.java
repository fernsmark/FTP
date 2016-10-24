import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class FTP_Server {
	
	 private static ServerSocket serverSocket_command;
	 private static ServerSocket serverSocket_data;
	 private static Socket clientSocket = null;
	 private static Socket clientSocket_data = null;
	
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
		        if(clientSocket.isConnected())
		        {
		        	System.out.println("Accepted connection from : " + clientSocket);
		        	clientSocket_data = serverSocket_data.accept();
		        }
		        
		        if(clientSocket_data.isConnected())
		        {
		        	 System.out.println("Data connection started on : " + clientSocket_data);
		        	 Thread t = new Thread(new ClientService(clientSocket_data));
		        	 t.start();
		        }

		    } catch (Exception e) {
		        System.err.println("Error in connection attempt.");
		     }
		}
	}
}
