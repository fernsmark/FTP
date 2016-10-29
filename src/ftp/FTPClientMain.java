package ftp;

public class FTPClientMain {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		FTPClientConn fcon= new FTPClientConn("themarkfernandes.com",21,"a9986967","Metalgear@123");
		fcon.run();

	}

}
