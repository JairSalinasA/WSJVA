package cusos.pryanio;

import srv.cuso;
import org.json.JSONObject;

import comun.bkComun;

public class ini extends bkComun {

	public ini(cuso inCuso) throws Exception {
		super(inCuso);
	}

	public void lis() throws Exception {
		if (!icuso.inData.getString("lay").equals("-1")) {
			JSONObject dvalFld = new JSONObject();
			dvalFld.put("2024", 2024);
			dvalFld.put("2023", 2023);
			jsValFlds.put("anioList", dvalFld);
			icuso.inData.put("fltanio","2023"); // Iniciamos con año=2023

		}		
		super.lis();
	}

	public void cap() throws Exception {
		super.cap();
		jsVals.put("anio", icuso.inData.get("anio"));
	}

	public void graba() throws Exception {
		super.graba();
	}

	public void fin() throws Exception {
		super.fin();
	}

}