import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class FTP_client {
	
	public static void main(String[] args)
	{
		Socket s;
		try {
			s = new Socket("localhost",1000);
		  
		DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
		dout.writeUTF("Asshole");  
		dout.flush();  
		dout.close();  
		s.close();  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
