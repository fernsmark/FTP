package ftp;
import java.io.*;
import java.net.*;

public class Client {

	public static void main(String args[]) throws Exception
	{
		Client c= new Client();
		c.run();
	}
	
	public void run() throws Exception
	{
		Socket socket= new Socket("localhost",1377);
		PrintStream pS= new PrintStream(socket.getOutputStream() );
		pS.println("Hello to server from client");
		
		InputStreamReader iR= new InputStreamReader(socket.getInputStream());
		BufferedReader bR= new BufferedReader(iR);
		
		String message= bR.readLine();
		System.out.println(message);
	}

}
