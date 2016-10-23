package ftp;
import java.io.*;
import java.net.*;
import java.nio.CharBuffer;

public class FTPClient {
	
	private PrintWriter writer = null;
	private static InputStream reader = null;
	private String USER = "a9986967";
	private String PASS = "password";
	private String message = null;
	private String response = null;
	
	public static void main(String args[]) throws Exception
	{
		FTPClient client= new FTPClient();
		client.run();
	}
	
	public void run() throws Exception
	{
		//Socket socket= new Socket("localhost",21);							// connect to localhost using socket on a port 1377
		Socket socket= new Socket("themarkfernandes.com",21);							// connect to localhost using socket on a port 1377
		System.out.println("Server IP: " + socket.getInetAddress()+" | Server Port: "+ socket.getPort() );
		System.out.println("Client IP: " + socket.getLocalAddress()+" | Client Port: "+ socket.getLocalPort() );
		
			
		//socket.setKeepAlive(true);
		PrintStream printstream= new PrintStream(socket.getOutputStream());		//send command messages to the server
		System.out.println("Host found.");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));		//accept user input
		String username;  

		//System.out.println("Enter Username");
		//username = br.readLine();
		//printstream.println(username);
		
		BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(socket.getInputStream()));		//read incoming messages from server
		message= bufferedReader.readLine();
		
        //System.out.println(reader);
        printstream.println("USER "+ USER  );
        printstream.println("PASS "+ PASS  );
        printstream.println("CWD /public_html" );
        printstream.println("RETR nodejs.txt" );
        printstream.println("QUIT" );
				
		if(message.equals("Authentication failed.")){
			System.out.println(message);
			socket.close();
		}
		else if(message.contains("220")){
			//System.out.println("Enter username using USER telnet command");
			System.out.println("220-Service ready for new user.");
			System.out.println(message);
			
			while ((response = bufferedReader.readLine()) != null)
				System.out.println(response);
			
			username = br.readLine();
		}
		else{
			System.out.println(message);			
		}
		
	}

}
/******************************************
Sample result:
Server IP: themarkfernandes.com/31.170.160.87 | Server Port: 21
Client IP: /192.168.8.104 | Client Port: 50728
Host found.
220-Service ready for new user.
220---------- Welcome to Pure-FTPd [privsep] ----------
220-You are user number 27 of 500 allowed.
220-Local time is now 17:28. Server port: 21.
220-This is a private system - No anonymous login
220 You will be disconnected after 3 minutes of inactivity.
331 User a9986967 OK. Password required
230-OK. Current restricted directory is /
230-1 files used (0%) - authorized: 10000 files
230 0 Kbytes used (0%) - authorized: 1536000 Kb
250 OK. Current directory is /public_html
425 No data connection
221-Goodbye. You uploaded 0 and downloaded 0 kbytes.
221 Logout.


********************************************/



/* Misc
InetAddress serverAddr = socket.getInetAddress();		//get server IP address
int serverPort = socket.getPort();					//get server port number
System.out.println(serverAddr+" "+serverPort);

InetAddress localAddr = socket.getLocalAddress();		//get local IP address
int localPort = socket.getLocalPort();					//get local port number
System.out.println(localAddr+" "+localPort); 
*/