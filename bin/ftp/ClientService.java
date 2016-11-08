import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientService implements Runnable {

	private Socket clientSocket;
	  private BufferedReader in = null;

	  public ClientService(Socket client) {
	    this.clientSocket = client;
	}

	@Override
	public void run() {
	    try {
	        in = new BufferedReader(new InputStreamReader(
	                clientSocket.getInputStream()));
	        String clientSelection;
	        while ((clientSelection = in.readLine()) != null) {
	            switch (clientSelection) {
	                case "1":
	                    receiveFile();
	                    break;
	                /*case "2":
	                    String outGoingFileName;
	                    while ((outGoingFileName = in.readLine()) != null) {
	                        sendFile(outGoingFileName);
	                    }

	                    break;*/
	                default:
	                    System.out.println("Incorrect command received.");
	                    break;
	            }
	            in.close();
	            break;
	        }

	    } catch (IOException ex) {
	        Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}

	public void receiveFile() {
	    try {
	        int bytesRead;

	        DataInputStream clientData = new DataInputStream(clientSocket.getInputStream());

	        String fileName = clientData.readUTF();
	        OutputStream output = new FileOutputStream(("ftp_recieved" + fileName));
	        long size = clientData.readLong();
	        byte[] buffer = new byte[1024];
	        while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
	            output.write(buffer, 0, bytesRead);
	            size -= bytesRead;
	        }

	        output.close();
	        clientData.close();

	        System.out.println("File "+fileName+" received from client" + " from " + clientSocket);
	    } catch (IOException ex) {
	        System.err.println("Client error. Connection closed.");
	    }
	}
}