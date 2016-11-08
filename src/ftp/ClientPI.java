    import java.io.*;
    import java.net.*;

    public class ClientPI 
    {
        private String hostName;
        private int port;
        private BufferedWriter writer;
        private BufferedReader reader;

        public ClientPI(String hostName, int port)
        {
            this.hostName = hostName;
            this.port = port;
        }

        public void open() throws IOException
        {
            Socket socket = new Socket(hostName, port);
            writer = new BufferedWriter(
                      new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(
                      new InputStreamReader(socket.getInputStream()));
        }


        public FTPResponse sendCommand(String command) throws IOException
        {
            writer.write(command+"\r\n");
            writer.flush();
            return getReply();
        }


        public FTPResponse getReply() throws IOException
        {
            StringBuffer sb = new StringBuffer();
            String line = null;
            String resp = null;
            FTPResponse response = null;

            do
            {
                line = reader.readLine();
                sb.append(line).append("\r\n");
                if(line == null ||
                    line.length() < 3 )
                    throw new IOException("Illegal FTP response! "+line);
                if(resp == null)
                    resp = line.substring(0,3);
            } 
            while(!(line.startsWith(resp) &&
                line.charAt(3) == ' '));

            if(resp.startsWith("4") || resp.startsWith("5"))
                                throw new FTPException("Ftp error! "+
                                                    resp+":"+sb);
													
            return new FTPResponse(resp, sb.toString());
        }

    }



    
