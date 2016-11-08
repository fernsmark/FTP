import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;


public class FTP_client {

	private static Socket sock_command, sock_data;
	private static String fileName;
	private static PrintStream os;
	static Scanner in = new Scanner(System.in);
	public static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) throws IOException {
		
		try {
			
			sock_command = new Socket("localhost", 21); 
			System.out.println("Enter Username");
			String user = stdin.readLine();
			//Send the message to the server
            DataOutputStream outToServer = new DataOutputStream(sock_command.getOutputStream());
            outToServer.writeBytes(user);
            outToServer.close();
            
            
            
            	if(!sock_command.isOutputShutdown())
    			{
    				sock_data = new Socket("localhost", 20); 
    				System.out.println("Connection established on " + sock_command.getPort() + " \nData communication on " + sock_data.getPort());
    			}
    			else
    			{
    				System.err.println("User Authentication Failed. Try again.");
    				sock_command.close();
    			}
    				
            
		} catch (Exception e) {
			System.err.println("Cannot connect to the server, try again later.");
			System.exit(1);
		}	
		os = new PrintStream(sock_data.getOutputStream());

		try {
			switch (Integer.parseInt(selectAction())) {
			case 1:
				os.println("1");
				sendFile();
				break;
			/*case 2:
				os.println("2");
				System.err.print("Enter file name: ");
				fileName = stdin.readLine();
				os.println(fileName);
				receiveFile(fileName);
				break;*/
			}
		} catch (Exception e) {
			System.err.println("not valid input");
		}


		sock_data.close();
	}

	public static String selectAction() throws IOException {
		System.out.println("1. STOR ");
		System.out.println("2. Recieve file.");
		System.out.print("\nMake selection: ");
		
		return stdin.readLine();
	}

	public static void sendFile() {
		try {
			System.err.print("Enter file name: ");
			fileName = stdin.readLine();

			File myFile = new File(fileName);
			byte[] mybytearray = new byte[(int) myFile.length()];

			FileInputStream fis = new FileInputStream(myFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			//bis.read(mybytearray, 0, mybytearray.length);

			DataInputStream dis = new DataInputStream(bis);
			dis.readFully(mybytearray, 0, mybytearray.length);

			OutputStream os = sock_data.getOutputStream();

			//Sending file name and file size to the server
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeUTF(myFile.getName());
			dos.writeLong(mybytearray.length);
			dos.write(mybytearray, 0, mybytearray.length);
			dos.flush();
			System.out.println("File "+fileName+" sent to Server.");
		} catch (Exception e) {
			System.err.println("File does not exist!");
		}
	}
}