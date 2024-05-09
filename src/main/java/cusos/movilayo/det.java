package cusos.movilayo;


import srv.cuso;

import org.json.JSONArray;

import comun.bkComun;

public class det extends bkComun {

	public det(cuso inCuso) throws Exception {
		super(inCuso);
		kComa="'";
	}

	public void lis() throws Exception {

         JSONArray jer = new JSONArray();
        jer.put(new Object[] { ""
                + "<i>Regis: </i>"
                + icuso.inData.get("regis")
                + "&nbsp;&nbsp;<i>Fecha: </i>"
                + icuso.inData.get("fecha")
                + "&nbsp;&nbsp;<i>Clabe: </i>"
                + icuso.inData.get("clabe")
                + "&nbsp;&nbsp;<i>Banco: </i>"
                + icuso.inData.get("banco")
              
                

        });

        icuso.outData.put("jerq", jer);
        jsVals.put("regis", icuso.inData.get("regis"));
        jsVals.put("fecha", icuso.inData.get("fecha"));
        jsVals.put("clabe", icuso.inData.get("clabe"));
        jsVals.put("banco", icuso.inData.get("banco"));
       
       
      
		super.lis();		
	}

	public void cap() throws Exception {
		super.cap();
	}

	public void graba() throws Exception {
       
       
	}


	public void fin() throws Exception {
		super.fin();
	}

}