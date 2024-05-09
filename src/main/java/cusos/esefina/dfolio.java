package cusos.esefina;

import srv.cuso;

import java.text.DecimalFormat;

import org.json.JSONArray;

import comun.bkComun;


public class dfolio extends bkComun {
    public dfolio(cuso inCuso) throws Exception {
		super(inCuso);
		kComa="'";
	}

	public void lis() throws Exception {


         DecimalFormat formato = new DecimalFormat("$ ###,###,###,###,###.00");

                JSONArray jer = new JSONArray();
        jer.put(new Object[] { ""
                + "<i> Referencia: </i>"
                + icuso.inData.get("reciboreferenciapago")
                + "<i>&nbsp;&nbsp;&nbsp;&nbsp; Monto: </i>"
                + formato.format(Double.parseDouble((String) icuso.inData.get("monto")))

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
