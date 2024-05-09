package cusos.autoriza;

import srv.cuso;
import comun.bkComun;
import org.json.JSONArray;
import java.text.NumberFormat;

public class docsid extends bkComun {

	public docsid(cuso inCuso) throws Exception {
		super(inCuso);
	}

	public void lis() throws Exception {
        eje = (String) icuso.inData.get("fecha");
        String fmtFec = eje.substring(8,10)+"/"+eje.substring(5,7)+ "/"+eje.substring(0,4);

		JSONArray jer = new JSONArray();
        jer.put(new Object[] { ""
                + "<i>Trámite: </i> "
                + icuso.inData.get("tram")
				+ "&nbsp;&nbsp;&nbsp;"
                + fmtFec
				+ "&nbsp;&nbsp;&nbsp;"
				+ NumberFormat.getCurrencyInstance().format(Double.parseDouble((String) icuso.inData.get("monto")))
			});

        icuso.outData.put("jerq", jer);
        jsVals.put("tram", icuso.inData.get("tram"));

		super.lis();
	}

	public void fin() throws Exception {
		super.fin();
	}

}