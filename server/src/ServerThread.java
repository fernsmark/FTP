import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Stack;
import java.util.*;
import java.net.*;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Scanner;

/* R>W.1: Many readers or one writer with readers having a higher priority */
class ServerThread extends Thread  
    {  

		static Scanner input1;
		static Scanner input2;
		static java.util.List<String> list1=new ArrayList<>();
		static java.util.List<String> list2=new ArrayList<>();
  
        Socket sk = null;  
        BufferedReader in = null;  
        PrintWriter out = null;  
        String username=null,password=null;
        boolean auth;
        String pathfix;
        Stack<String> path;
		private String myIp=null;
		private String myIpPort=null;
		
		//static int common=0;
		static HashMap hm = new HashMap();
		
		static int activeReaders = 0; 						// number of active readers
		static int activeWriters = 0;						// number of active writers
		static int waitingWriters = 0; 					// number of waiting writers
		static int waitingReaders = 0; 					// number of waiting readers
		static countingSemaphore mutex; 					// mutual exclusion
		static countingSemaphore readers_que;			// queue for waiting readers
		static countingSemaphore writers_que;			// queue for waiting writers
		static int x=0;       								// x is shared data	
        
        private ServerSocket serverSocket=null;
        InetAddress host;
        final static int backlog=5;
        
        public ServerThread(Socket sk)  
        {  
            this.sk = sk;  
        }  
        public void run()  
        {  
            try  
            {  
            	pathfix="./data";
            	path=new Stack<String>();
                out = new PrintWriter(new OutputStreamWriter(sk.getOutputStream()),
        				true);  
                in = new BufferedReader(new InputStreamReader(sk  
                        .getInputStream())); 
                out.println("220 -------------------- Welcome to FTP-Server  --------------------");
				out.println("Developed by Mark Ashley Fernandes, Mayur Mukund Rao & Tasnim Makada");
				out.println("--------------------------------------------------------------------");
                
                String line;
                System.out.println("Client connected. Control channel established.");
                while((line = in.readLine())!=null){
                	System.out.println(line);
                	 String [] arg=line.split(" ");
                	 switch (arg[0]){
                	 	case "USER":
                	 		user(arg);
                	 						break;
                	 	case "PASS":
                	 		pass(arg);
                	 						break;
                	 	case "PASV":
                	 		pasv();
                	 						break;
                	 	case "TYPE":
                	 		type(arg);
                	 						break;
                	 	case "STOR"://upload
                	 		upload(arg);
                	 						break;
                	 	case "LIST":
                	 		list(arg);
                	 						break;
                	 	case "RETR"://download;
                	 		retr(arg);
                	 						break;
                	 	case "DELE":
                	 		dele(arg);
                	 						break;
                	 	case "CWD":
                	 		cd(arg);
                	 						break;
                	 	case "MKD":
                	 		mkdir(arg);
                	 						break;
						case "PWD":
							pwd();
											break;
											
						case "QUIT":
							quit(arg);
											break;
											
						case "NOOP":
							noop();		break;
						
                	 	default:
                	 		out.println("202 Command not implemented, superfluous at this site.");
                	 		break;
                	 
                	 }
                     out.flush();
                     System.out.println("Command executed. Waiting for next command.");
                }
            }  
            catch (IOException e)  
            {  
		out.println("Command executed. Client Connection closed.");
                //e.printStackTrace();  
            }  
              
        } 
        public void type(String [] args){
        	if(args.length>1){
        		String t=args[1];
        		out.println("200 type changed");
        	}else{
        		out.println("500 Syntax error, command unrecognized.");
        	}
        }
       
        public void user(String [] args){
        	if(args.length>1){
        		username=args[1];
				try{
				File file1 = new File("/home/ubuntu/ftp/uname.txt");
				input1 = new Scanner(file1);
				while (input1.hasNextLine()) 
				{
					list1.add(input1.nextLine());
				}
				input1.close();
				if(list1.contains(username))
				{
					out.println("331 User name okay, need password");
				}
				else
				{
					out.println("Username Incorrect. Try again.");
					sk.close();
				}
				}
				catch(Exception e) {
				System.err.println("Error in connection attempt.");	
			}
        	}
        }
        public void pass(String [] args){
        	if(args.length>1){
        		password=args[1];
				File file2 = new File("/home/ubuntu/ftp/password.txt");
				try{
				input2 = new Scanner(file2);
				while (input2.hasNextLine()) 
				{
					list2.add(input2.nextLine());
				}
				input2.close();
				if(list2.contains(password))
				{
					auth=true;
					out.println("230 User logged in, proceed.");
				}
        	else{
        		out.println("Password Incorrect. Try again.");
				sk.close();
        	}
			}catch(Exception e) {
				System.err.println("Error in connection attempt.");	
			}
			}
        }
        public String pwd() 
    	{
    		// List the current working directory.
    		String p = "/";
    		for (String e:path)
    		{
    			p += e + "/";
    		}
    		return p;
    	}

    	private String getpath()
    	{
    		return pathfix + pwd();
    	}
    	private boolean valid(String s)
    	{
    		// File names should not contain "/".
    		return (s.indexOf('/') < 0);
    	}
        public void cd(String [] args){
        	if(args.length>1){
        		String dir=args[1];
        		if (!valid(dir))
        		{
        			out.println("451 move only one level at one time.'/' is not allowed");
        		}
        		else
        		{
        			if ("..".equals(dir))
        			{
        				if (path.size() > 0)
        					path.pop();
        				else{
        					out.println("451 Requested action aborted: already in root dir.");
        					return;
        				}
        			}
        			else if (".".equals(dir))
        			{
        				;
        			}
        			else
        			{
        				File f = new File(getpath());
        				if (!f.exists()){
        					out.println("451 Requested action aborted: Directory does not exist: " + dir);
        					return;
        				}
        				else if (!f.isDirectory()){
        					out.println("451 Requested action aborted:Not a directory: " + dir);
        					return;
        				}
        				else{
        					path.push(dir);
        				}
        			}
        			out.println("250 dir switched.");
        		}

        	}else{
        		out.println("500 Syntax error, command unrecognized.");
        	}
        }
        public void mkdir(String [] args){
        	if(args.length>1){
        		String destDirName=getpath()+args[1];
        		File dir = new File(destDirName);
    		    if(dir.exists()) {
    		    	System.out.println("a" + destDirName + "b");
    		    	out.println("451 Requested dir exists.");
    		    	return;
    		    }
    		    if(!destDirName.endsWith(File.separator))
    		    	destDirName = destDirName + File.separator;

    		    if(dir.mkdirs()) {
	    		     System.out.println("a" + destDirName + "b");
	    		     out.println("250 Requested file action okay, completed.");
    		    }else{
    		    	out.println("500 something wrong.");
    		    }
        	}else{
        		out.println("500 Syntax error, command unrecognized.");
        	}
        }
        public void dele(String [] args){
        	if(args.length>1){
        		String destDirName=getpath()+args[1];
        		File dir = new File(destDirName);
    		    if(dir.exists()) {
    		    	if(!dir.isDirectory()){
	    		    	//file
    		    		dir.delete();
	    		    	out.println("250 file delete");
	    		    	return;
    		    	}else{
    		    		delFolder(destDirName);
    		    		out.println("250 folder delete");
	    		    	return;
    		    	}
    		    }
    		    else{
    		    	out.println("451 no such file or folder");
    		    }
        	}else{
        		out.println("500 Syntax error, command unrecognized.");
        	}
        }

	public void quit(String [] args){
			System.out.println("User Logged Out. Cloed Data and Control connection.");
        }
		
	public void noop() throws IOException {
    	if(sk.isConnected()){
    		out.println("200 Zzz...");
    	}
    	else {
    		out.println("Not Connected!");
    	}
    }
        
        public void pasv() throws IOException{
		StringBuilder sb = new StringBuilder();
        	serverSocket = new ServerSocket(0, backlog, host);
        	InetSocketAddress h= (InetSocketAddress) (serverSocket.getLocalSocketAddress()); //(InetSocketAddress) (serverSocket.getLocalSocketAddress());//InetAddress.getLocalHost(); 
        	int p=h.getPort();
        	int p1=p%256;
        	int p0=p/256;
			
			InetAddress IP=InetAddress.getLocalHost();
			System.out.println("IP of my system is := "+IP.getHostAddress());
			//IP ADDRESS CORRECT PASRING:
			try
			{
			Runtime r = Runtime.getRuntime();
			Process p123 = r.exec("curl http://169.254.169.254/latest/meta-data/public-ipv4");
			p123.waitFor();
			BufferedReader b123 = new BufferedReader(new InputStreamReader(p123.getInputStream()));
			String line = "";

			while ((line = b123.readLine()) != null) {
  				System.out.println("****curl***"+line);
				byte b[]= line.getBytes();
				String[] parts = line.split("\\.");
				int part1 = Integer.parseInt(parts[0]); 
				int part2 = Integer.parseInt(parts[1]);
				int part3 = Integer.parseInt(parts[2]);
				int part4 = Integer.parseInt(parts[3]);
				System.out.println("****curl***"+part1);
				System.out.println("****curl***"+part2);
				System.out.println("****curl***"+part3);
				System.out.println("****curl***"+part4);
				
			System.out.printf("227 (%d,%d,%d,%d,%d,%d)\n",part1,part2,part3,part4,p0,p1);
			out.printf("227 (%d,%d,%d,%d,%d,%d)\n",part1,part2,part3,part4,p0,p1);
			}
			b123.close();
			}catch (Exception e) {
			e.printStackTrace();
			}
        }
		
        
        public void list(String [] args) {
        	String [] lst= new File(getpath()).list();
        	out.println("200 processing");
        	String send="";
        	for(String t:lst){
				System.out.println(t);
        		send=send.concat(t).concat("\n");
        	}
        	send=send.concat(".\n");
        	send=send.concat("..\n");
        	ByteArrayInputStream is = new ByteArrayInputStream(send.getBytes());
        	new Thread(new GetThread(serverSocket, is,out)).start();
        }
        public void retr(String [] args) {
        	if(args.length>1){
        		String destDirName=getpath()+args[1];
        		FileInputStream f;
				try {
					f = new FileInputStream(destDirName);
					new Thread(new GetThread(serverSocket, f,out)).start();
					out.println("200 processing");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					out.println("451 no such file or folder");
				}
    			
        	}else{
        		out.println("500 Syntax error, command unrecognized.");
        	}
        }
        public void upload(String [] args){
        	if(args.length>1){
        		String destDirName=getpath()+args[1];
				FileOutputStream f;
				try {
					f = new FileOutputStream(destDirName);
					new Thread(new PutThread(serverSocket, f,out)).start();
					out.println("200 processing");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					out.println("451 no such file or folder");
				}
				
        	}else{
        		out.println("500 Syntax error, command unrecognized.");
        	}
        }
        private static class GetThread implements Runnable//download
    	{
    		private ServerSocket dataChan = null;
    		private InputStream file = null;
    		PrintWriter pt;

    		public GetThread(ServerSocket s, InputStream f,PrintWriter out)
    		{
    			dataChan = s;
    			file =  f;
    			pt=out;
				mutex = new countingSemaphore(1);
				readers_que = new countingSemaphore(0);
				writers_que = new countingSemaphore(0);
    		}
    		public void run()
    		{
				System.out.println("1: before entry section "+activeWriters); 
				mutex.P();
				if (activeWriters > 0) {
					waitingReaders++;
					System.out.println("Reader Thread Blocked!");
					mutex.V();
					readers_que.P();
					System.out.println("Reader Thread Resumed!");
				}
				activeReaders++;
				if (waitingReaders > 0) {
					waitingReaders--;
					readers_que.V();			// pass the baton
				}							
				else {
					mutex.V();
				}
				
				/******************************* Critical Section Starts ****************************************/
				try
    			{
    				//System.out.println(" wait for the client's initial socket");
    				Socket xfer = dataChan.accept();
    				//System.out.println(" Prepare the output to the socket");
    				BufferedOutputStream out = new BufferedOutputStream(
    						xfer.getOutputStream());
    				System.out.println(file.toString());
    				// read the file from disk and write it to the socket
    				byte[] sendBytes = new byte[4096];
    				int iLen = 0;
    				while ((iLen = file.read(sendBytes)) != -1)
    				{
    					out.write(sendBytes, 0, iLen);
    				}
    				out.flush();
    				out.close();
    				xfer.close();
    				file.close();
    				System.out.println("Data sent to client.");
    			}
    			catch (Exception e)
    			{
    				e.printStackTrace();
    			}
    			pt.println("200 trasfer complete");
				
				System.out.println("2: before CS "+activeWriters);
				
				/******************************* Critical Section Ends ***************************************/		
				
				mutex.P();
				activeReaders--;
				if (activeReaders == 0 && waitingWriters > 0) {
					  waitingWriters--;
					  writers_que.V();		// pass the baton
				}
				else
					mutex.V();
				System.out.println("3: after CS "+activeWriters);
				
    		}
    	}
        private static class PutThread implements Runnable			//Upload
    	{
    		private ServerSocket dataChan = null;
    		private FileOutputStream file = null;
    		PrintWriter pt;
			
			// int activeReaders = 0; 						// number of active readers
			// int activeWriters = 0;						// number of active writers
			// int waitingWriters = 0; 					// number of waiting writers
			// int waitingReaders = 0; 					// number of waiting readers
			// countingSemaphore mutex; 					// mutual exclusion
			// countingSemaphore readers_que;			// queue for waiting readers
			// countingSemaphore writers_que;			// queue for waiting writers
			// int x=0;       								// x is shared data	
    		
    		public PutThread(ServerSocket s, FileOutputStream f,PrintWriter out)
    		{
    			dataChan = s;
    			file = f;
    			pt=out;
				mutex = new countingSemaphore(1);
				readers_que = new countingSemaphore(0);
				writers_que = new countingSemaphore(0);
    		}

    		public void run()
    		{
    			/*
    			 * TODO: Process a client request to transfer a file.
    			 */
				 	//common++;
					//System.out.println("value of common is "+common);
				System.out.println("1: before entry section "+activeWriters); 
				mutex.P();
				if (activeReaders > 0 || activeWriters > 0 ) {
					waitingWriters++;
					System.out.println("Writer Thread Blocked!");
					mutex.V();
					writers_que.P();
					System.out.println("Writer Thread Resumed!");
				}
				activeWriters++;
				mutex.V();
				System.out.println("2: before CS "+activeWriters);

				/******************************* Critical Section Starts ****************************************/
				try
    			{
    				// wait for the client's initial socket
    				Socket xfer = dataChan.accept();

    				// Prepare the input from the socket
    				BufferedInputStream in = new BufferedInputStream(
    						xfer.getInputStream());


    				// read the data from the socket and write to the disk file
    				byte[] inputByte = new byte[4096];
    				int iLen = -1;
    				while ((iLen = in.read(inputByte)) > -1)
    				{
    					file.write(inputByte, 0, iLen);
    					
    				}
    				file.flush();
    				in.close();
    				xfer.close();
    				file.close();
					System.out.println("Data received from client.");
    			}
    			catch (Exception e)
    			{
    				e.printStackTrace();
    			}
    			
    			pt.println("200 trasfer complete");
				
				/******************************* Critical Section Ends ***************************************/				

				mutex.P();
				activeWriters--;
				if (waitingReaders > 0) {
					waitingReaders--;
					readers_que.V();
				}
				else if (waitingWriters > 0) {
					waitingWriters--;
				   writers_que.V();
				}
				else
					mutex.V();
				System.out.println("3: after CS "+activeWriters);
    		}
    	}

        public void delAllFile(String path) { 
            File file = new File(path); 
            if (!file.exists()) { 
                return; 
            } 
            if (!file.isDirectory()) { 
                return; 
            } 
            String[] tempList = file.list(); 
            File temp = null; 
            for (int i = 0; i < tempList.length; i++) { 
                if (path.endsWith(File.separator)) { 
                    temp = new File(path + tempList[i]); 
                } 
                else { 
                    temp = new File(path + File.separator + tempList[i]); 
                } 
                if (temp.isFile()) { 
                    temp.delete(); 
                } 
                if (temp.isDirectory()) { 
                    delAllFile(path+"/"+ tempList[i]);
                    delFolder(path+"/"+ tempList[i]);
                } 
            } 
        }
        public void delFolder(String folderPath) { 
            try { 
                delAllFile(folderPath);
                String filePath = folderPath; 
                filePath = filePath.toString(); 
                java.io.File myFilePath = new java.io.File(filePath); 
                myFilePath.delete(); 
            } 
            catch (Exception e) { 
                System.out.println("x"); 
                e.printStackTrace(); 
            } 
        } 
        
    }  