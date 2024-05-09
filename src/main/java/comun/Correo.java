package comun;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class Correo {

    public String sendEmail(String Email, int Token) {
        
        String msj = "";
        
        try{
            URL url = new URL("http://209.124.64.29/SendEmail/Email");
            LinkedHashMap<String, String> params = new LinkedHashMap<>();

            params.put("email", Email);
            params.put("token", Token + "");

            StringBuilder postData = new StringBuilder();

            for (Map.Entry<String, String> param : params.entrySet()) {

                if (postData.length() != 0)
                    postData.append('&');

                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()),"UTF-8"));
            }

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String inputLine;

            while ((inputLine = in.readLine()) != null)
                msj = inputLine;

            in.close();
            
            Thread.sleep(3000);
            
        }catch(Exception ex){
            msj = ex.getMessage();
        }
        
        return msj;
        
    }
    
    public String sendEmailAttachment(String Email, int Usua) {
        
        String msj = "";
        
        try{
            URL url = new URL("http://209.124.64.29/SendEmail/EmailAttachment");
            
            LinkedHashMap<String, String> params = new LinkedHashMap<>();

            params.put("email", Email);
            params.put("usua", Usua + "");

            StringBuilder postData = new StringBuilder();

            for (Map.Entry<String, String> param : params.entrySet()) {

                if (postData.length() != 0)
                    postData.append('&');

                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()),"UTF-8"));
            }

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String inputLine;
    
            while ((inputLine = in.readLine()) != null)
                msj = inputLine;

            in.close();
            
            Thread.sleep(3000);
            
        }catch(Exception ex){
            msj = ex.getMessage();
        }
        
        return msj;
        
    }
    

}
