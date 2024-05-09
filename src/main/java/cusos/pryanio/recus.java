package cusos.pryanio;

import srv.cuso;
//import org.json.JSONObject;
import org.json.JSONArray;

import comun.bkComun;

public class recus extends bkComun {

	public recus(cuso inCuso) throws Exception {
		super(inCuso);
        JSONArray jer = new JSONArray();
        jer.put(new Object[] { ""
                + "<i>Fndo: </i>"
                + icuso.inData.get("fndo")
                + "&nbsp;  Año: "
				+ icuso.inData.get("anio")
        });
        jer.put(new Object[] { ""
                + "<i>Proyecto: </i>"
                + icuso.inData.get("proy")
                + "&nbsp;"
                + icuso.inData.get("lproy")
        });

		jsVals.put("fndo", icuso.inData.get("fndo"));
		jsVals.put("anio", icuso.inData.get("anio"));
		jsVals.put("proy", icuso.inData.get("proy"));
		jsVals.put("lproy", icuso.inData.get("lproy"));

		icuso.outData.put("jerq", jer);
	}

	public void lis() throws Exception {
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