package ftp;
import java.io.*;
import java.net.*;

public class FTPClient {
	
	public static void main(String args[]) throws Exception
	{
		FTPClient client= new FTPClient();
		client.run();
	}
	
	public void run() throws Exception
	{
		//Socket socket= new Socket("localhost",21);							// connect to localhost using socket on a port 1377
		Socket socket= new Socket("themarkfernandes.com",21);							// connect to localhost using socket on a port 1377
		PrintStream printstream= new PrintStream(socket.getOutputStream() );
		System.out.println("Host found.");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String username;  

		//System.out.println("Enter Username");
		//username = br.readLine();
		//printstream.println(username);
		
		BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		String message= bufferedReader.readLine();
		/*
		String response;
				while ((response = bufferedReader.readLine()) != null) {
	        System.out.println(response);
	    }*/
				
		if(message.equals("Authentication failed.")){
			System.out.println(message);
			socket.close();
		}
		else if(message.contains("220")){
			System.out.println("Enter username using USER telnet command");
			System.out.println("220-Service ready for new user.");
			System.out.println(message);
		}
		else{
			System.out.println(message);			
		}
		
	}

}
