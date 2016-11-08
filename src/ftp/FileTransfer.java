import java.util.*;
import java.net.*;
import java.io.*;


public class FileTransfer
{
    ClientPI clientPI;

    public FileTransfer(ClientPI pi)
    {
        this.clientPI = pi;
    }

    public void downloadFileInActive(String fileName) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(0);
        String socketAddress = getSocketAddress(serverSocket.getLocalPort());

        // Set type to ascii
		System.out.println("TYPE A");
        System.out.println(clientPI.sendCommand("TYPE A").getResponseText());

        // Send the port command with the local socket address
		System.out.println("PORT "+socketAddress);
        System.out.println(clientPI.sendCommand("PORT "+socketAddress).getResponseText());
		System.out.println(serverSocket.getLocalPort());

        // send the RETR command with the file name
		System.out.println("RETR "+fileName);
        System.out.println(clientPI.sendCommand("RETR "+fileName).getResponseText());

        // Start listening for socket connections
        Socket dataTransferSocket = serverSocket.accept();

        // Client DTP process
        FileOutputStream outputStream = new FileOutputStream(fileName);
        InputStream inputStream = dataTransferSocket.getInputStream();
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while((bytesRead = inputStream.read(buffer)) > -1)
        {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();

        dataTransferSocket.close();

		if(clientPI.getReply().toString().contains("4e25154f"))
			System.out.println("File successfully transferred to client.");

        // Get the completion reply from the server.
        //System.out.println(clientPI.getReply());

    }

    public void uploadFileInActive(String fileName) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(0);
        String socketAddress = getSocketAddress(serverSocket.getLocalPort());

        // Set type to ascii
		System.out.println("TYPE A");
        System.out.println(clientPI.sendCommand("TYPE A").getResponseText());

		System.out.println("PORT "+socketAddress);
        System.out.println(clientPI.sendCommand("PORT "+socketAddress).getResponseText());
		//System.out.println(serverSocket.getLocalPort());

        // send the STOR command with the file name
		System.out.println("STOR "+fileName);
        System.out.println(clientPI.sendCommand("STOR "+fileName).getResponseText());
        Socket dataTransferSocket = serverSocket.accept();
        FileInputStream inputStream = new FileInputStream(fileName);
        OutputStream outputStream = dataTransferSocket.getOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while((bytesRead = inputStream.read(buffer)) > -1)
        {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        dataTransferSocket.close();

		if(clientPI.getReply().toString().contains("70dea4e"))
			System.out.println("File successfully transferred to server.");

        // Get the completion reply from the server.
        //System.out.println(clientPI.getReply());
    }


    private String getSocketAddress(int port) throws IOException
    {
        String hostAddress = null;
        try
        {
            hostAddress = 
                InetAddress.getLocalHost().getHostAddress();
            hostAddress = hostAddress.replace('.',',');
        }
        catch(Exception e)
        {
            throw new IOException("Cannot send port command "
                    +e.getMessage());
        }
        return hostAddress+","+(port>>8)+","+(port & 255);
    }

            


    public static void main(String[] args) throws Exception
    {
        String user = "a9986967";
        String password = "enter_password_here";
        String host = "themarkfernandes.com";
        int port = 21;

        ClientPI clientPI = new ClientPI(host,port);
        clientPI.open();
        clientPI.getReply();
        System.out.println(clientPI.sendCommand("USER "+user).getResponseText());
        System.out.println(clientPI.sendCommand("PASS "+password).getResponseText());

        System.out.println(clientPI.sendCommand("CWD /public_html").getResponseText());

        FileTransfer ft = new FileTransfer(clientPI);
		ft.downloadFileInActive("default.php");
		ft.uploadFileInActive("FTPResponse.java");
    }
}
