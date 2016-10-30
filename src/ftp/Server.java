package ftp;
import java.io.*;
import java.net.*;

public class Server {

	public static void main(String args[]) throws Exception
	{
		Server s= new Server();
		s.run();
	}
	
	public void run() throws Exception
	{
		ServerSocket sSocket= new ServerSocket(1377);
		Socket socket= sSocket.accept();
		//InputStreamReader iR= new InputStreamReader(socket.getInputStream());
		//BufferedReader bR= new BufferedReader(iR);
		BufferedReader bR= new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		String message= bR.readLine();
		System.out.println(message);
		
		if(message!=null){
			PrintStream pS= new PrintStream(socket.getOutputStream());
			pS.println("Message Received");
		}
	}
	
}
