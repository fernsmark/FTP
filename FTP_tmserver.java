import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
		        	String[] userInputs = user.split(" ");
		        	String userCommand = userInputs[0].trim();
                    String username = "";
                    String password = "";
                    switch (userCommand) {
						case "USER":
							validateUser(userInputs[1].trim());
							break;
						
						case "PWD":
							validatePassword(userInputs[1].trim());
							break;
							
						case "STOR"
							port("hostname", "port");
							store("filename");
							break;
							
						case "RETR":
							port("hostname", "port");
							retr("filename");
							break;
						
						case "NOOP":
							noop();
							break;
						
						case "QUIT":
							quit();
							break;
							
						case "TYPE":
							type();
							break;
							
						case "MODE":
							mode();
							break;
							
						case "STRU":
							stru();
							break;

					default:
						break;
					}
                    if(user.contains("USER")) {
                        String[] users = user.split(" ");
                        username = users[1];
                    }
                    else {
                        System.out.println("Please enter username in the format \"USER username\"");
                    }
		        	inFromClient.close();
	 
                    if(!validateUser(username)){
                        System.out.println("Username not Valid!");
                    }
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

    public boolean validateUser(String inputusername){
        Scanner fileScan = new Scanner (new File("users.txt"));
        boolean found = false;
        while (fileScan.hasNextLine()) {
            String input = fileScan.nextLine();
            String Username = input.substring(0,input.indexOf(' '));
            String Password = input.substring(input.indexOf(' '),input.length());

            if (Username.equals(inputusername){// || (Password.equals(inputpassword))) { // Use for password later
                found = true;
            }
        }
        return found;
    }
    
    public String[] getPort(String portCommand){
    	String[] parts = portCommand.split(" ");
    	String ip=parts[1];
    	String ip_parts = ip.split(",");
    	int clientPort= Integer.parseInt(ip_parts[4]*256) + Integer.parseInt(ip_parts[5]);
    	String client_ip = String.join(ip_parts[0], ".", ip_parts[1], ".", ip_parts[2], ".", ip_parts[3]);
    	String vals[] = new String()[2];
    	vals[0] = client_ip;
    	vals[1] = String.valueOf(clientPort);
    	
    	return vals; 
    }
    
    public synchronized String port(Socket dataSocket, String port) throws IOException {
    	int port = getPort(port);
    	
    	try {
	    	ServerSocket data_socket = new ServerSocket(port);
	    	data_socket.accept();
	    	return "200 PORT command successful"
    	}
    	catch(Exception e){
    		System.out.println(e.printStackTrace();
    	}
    }
    
    public synchronized boolean retr(String fileName, Socket dataSocket) throws IOException {
//    	Socket dataSocket = new Socket(0);
//    	String socketAddress = getSoc
        
    	String response = null;

        String fullPath = "F:\FTP\testFile.txt"//pwd() + "/" + fileName;
        
        sendLine("RETR " + fullPath);
        response = readLine();   

        BufferedInputStream input = new BufferedInputStream(new FileInputStream(new File(fileName)));//dataSocket.getInputStream());
        BufferedOutputStream output = new BufferedOutputStream(dataSocket.getOutputStream());//new FileOutputStream(new File(fileName)));

        byte[] buffer = new byte[4096];
        int bytesRead = 0;

        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(bytesRead);
        }
        output.close();
        input.close();
    }
    
    public synchronized boolean noop(Socket serverSocket) throws IOException {
    	if(serverSocket.isConnected()){
    		return "200 Zzz...";
    	}
    	else {
    		return "Not Connected!";
    	}
    }
    
    public synchronized boolean quit(Socket serverSocket) throws IOException {
    	serverSocket.close();
    }
}
