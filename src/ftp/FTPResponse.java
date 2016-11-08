    public class FTPResponse
    {
        private String responseText;
        private String responseCode;

        public FTPResponse(String responseCode,
                    String responseText)
        {
            this.responseText = responseText;
            this.responseCode = responseCode;
        }

        public String getResponseCode()
        {
            return this.responseCode;
        }

        public String getResponseText()
        {
            return this.responseText;
        }
    }

