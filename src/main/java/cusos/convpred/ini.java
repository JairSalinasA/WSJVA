package cusos.convpred;

import srv.cuso;
import comun.bkComun;
// import org.json.JSONArray;
//import org.json.JSONObject;

public class ini extends bkComun {
	public ini(cuso inCuso) throws Exception {
		super(inCuso);
	}

	public void lis() throws Exception {
		if (icuso.inData.getString("lay").equals("2")) /// Iniciamos con año 2023
		icuso.inData.put("fltanio", "2023");
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
