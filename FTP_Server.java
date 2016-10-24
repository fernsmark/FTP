import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class FTP_Server {
	
	 private static ServerSocket serverSocket;
	 private static Socket clientSocket = null;
	
	public static void main(String[] args)
	{
		try {
		    serverSocket = new ServerSocket(1000);
		    System.out.println("FTP Server started.");
		} catch (Exception e) {
		    System.err.println("Cannot start FTP server on this port");
		    System.exit(1);
		}

		while (true) {
		    try {
		        clientSocket = serverSocket.accept();
		        System.out.println("Accepted connection from : " + clientSocket);

		        Thread t = new Thread(new ClientService(clientSocket));

		        t.start();

		    } catch (Exception e) {
		        System.err.println("Error in connection attempt.");
		     }
		}
	}
}
