
package ftp;
import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.net.URL;

public class FTPClientConn {
	private String host= null;
	private int port= 0;
	private String USER = null;
	private String PASS = null;
	private String message = null;
	private String response = null;
	private String control= "1";
	List<String> lines = new LinkedList<String>(); // create a new list
	private String myIp;
	private String myIpPort;

	public FTPClientConn(String host, int port, String user,String password){
		this.host= host;
		this.port= port;
		this.USER= user;
		this.PASS= password;
	}
		
	public void run() throws Exception
	{
		//Socket socket= new Socket("localhost",21);							// connect to localhost using socket on a port 1377
		Socket socket= new Socket(host,port);							// connect to localhost using socket on a port 1377
		System.out.println("Server IP: " + socket.getInetAddress()+" | Server Port: "+ socket.getPort() );
		System.out.println("Client IP: " + socket.getLocalAddress()+" | Client Port: "+ socket.getLocalPort() );
				
		myIp=getMyIP();
		System.out.println("Clients Remote IP:"+myIp );
		myIpPort= (myIp.toString().replace(".",",").replace("/",""));
		System.out.println("myIpPort "+myIpPort);
		
		/*InetAddress bindNew= socket.getLocalAddress();
		int bindPort= socket.getLocalPort()+1;
		//System.out.println(bindNew.toString());
		String newBind= (bindNew.toString().replace(".",",").replace("/",""));
		//System.out.println(newBind);*/
		
        System.out.println("PORT "+myIpPort+","+200+","+132 );
					
		//socket.setKeepAlive(true);
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(socket.getInputStream()));		//read incoming messages from server
		PrintStream printstream= new PrintStream(socket.getOutputStream());		//send command messages to the server
		System.out.println("Host found.");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));		//accept user input
		
		// Enables User Inputs.. can use similar to provide a menu, ex. 1. RETR 2. STOR, Press 1 or 2
		System.out.println("Press 1 to login.");
		//control = br.readLine(); uncomment
		
		if(control.equals("1")){
			
			//1message= bufferedReader.readLine();
	        printstream.println("USER "+ USER  );
	        //message= bufferedReader.readLine();
	       // System.out.println(message);
	        TimeUnit.SECONDS.sleep(2);
	        printstream.println("PASS "+ PASS  );
	        printstream.println("CWD /public_html" );
	        printstream.println("TYPE A" );
	        //printstream.println("CWD /public_html" );
	        
	        //read(bufferedReader);
	        printstream.println("PORT "+myIpPort+","+250+","+60 );      	        	
	        //printstream.println("PASV" );
	        printstream.println("NLST" );
	       // printstream.println("LIST" );
		        
		    printstream.println("RETR nodejs.txt" );
		     //printstream.println("QUIT" );
		        read(bufferedReader);
				
		        /*if(message.contains("220")){
					System.out.println(message);
				}
				else{
					System.out.println(message);			
				}	*/
			
		}
		else
		{
			socket.close();
			System.out.println("Connection Terminated.");
		}
			
	}
	public void read(BufferedReader br)throws Exception{
		while ((response = br.readLine()) != null){
			System.out.println(response);
			lines.add(response);
	        }
		System.out.println(Arrays.toString(lines.toArray()));
		
	}
	
	public static String getMyIP() throws Exception{
		
		URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		
	}

}
