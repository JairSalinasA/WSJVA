package cusos.roles;


/**
 *
 * @author christianmendoza
 */

import srv.cuso;
import org.json.JSONObject;
import comun.bkComun;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;


public class alter extends bkComun {
    public alter(cuso inCuso) throws Exception {
		super(inCuso);
		kComa="'";
	}
    
       

	public void lis() throws Exception {

        jsVals.put("usua", icuso.inData.get("usua"));
        jsVals.put("nombre", icuso.inData.get("nombre"));

          JSONArray jer = new JSONArray();
          jer.put(new Object[] { icuso.inData.get("usua"), icuso.inData.get("nombre") });
          icuso.outData.put("jerq", jer);

		super.lis();
		
	}
        
     public void cap() throws Exception {
        jsVals.put("usua", icuso.inData.get("usua"));
		super.cap(); 
	}

	public void graba() throws Exception {
       
		super.graba();
	}

	public void fin() throws Exception {
		super.fin();
	}
        
    

}

