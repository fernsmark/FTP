package ftp;
import java.io.*;
import java.net.*;
import java.util.Arrays.*;

public class FTPServer {
	
	public static void main(String args[]) throws Exception
	{
		FTPServer server= new FTPServer();
		server.run();
	}
	
	public void run() throws Exception
	{
		ServerSocket serverSocket= new ServerSocket(21);					// create socket on a port 1377
		System.out.println("Server Running, waiting for connections..");
		Socket socket= serverSocket.accept();								// accept incoming client request
		//System.out.println("Client Connected.");
		System.out.println("Client attempting to connect. Awaiting username..");
		
		BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(socket.getInputStream()));		// read from socket, read from client to server
				
		String message= bufferedReader.readLine();
		//System.out.println(message);
		
		PrintStream printStream= new PrintStream(socket.getOutputStream());
		if(message.equals("root")){
			
			printStream.println("User Authenticated");
			System.out.println("User Authenticated");
			sendInfo(socket);
			//serverSocket.close();
		}
		else
		{
			printStream.println("Authentication failed.");
			System.out.println("Authentication failed.");
			serverSocket.close();
		}
	}	
	
	public void sendInfo(Socket socket) throws Exception
	{
		System.out.println("Sending info to connected client..");
		
		PrintWriter printWriter= new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));			// write to socket, from server to client
		
	}

}
