package cusos.esefina;


import srv.cuso;

import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONObject;
import comun.bkComun;


public class folio extends bkComun {
    public folio(cuso inCuso) throws Exception {
		super(inCuso);
		kComa="'";
	}

	public void lis() throws Exception {

        

                JSONArray jer = new JSONArray();
        jer.put(new Object[] { ""
                + "<i> Registro: </i>"
                + icuso.inData.get("archi")
               
                 + "<i>&nbsp;&nbsp;&nbsp;&nbsp; idbanco: </i>"
                + icuso.inData.get("idbanco")

        });

        icuso.outData.put("jerq", jer);
        jsVals.put("archi", icuso.inData.get("archi"));
        jsVals.put("fecha", icuso.inData.get("fecha"));
        jsVals.put("idbanco", icuso.inData.get("idbanco"));
      
		super.lis();
		
	}
        
         public void cap() throws Exception {
		super.cap(); 
	}

	public void graba() throws Exception {
		super.graba();
	}

	public void fin() throws Exception {
		super.fin();
	}

}
