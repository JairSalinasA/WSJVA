package cusos.datsubing;

import srv.cuso;
//import org.json.JSONArray;
//import org.json.JSONObject;

import comun.bkComun;

public class ini extends bkComun {

	public ini(cuso inCuso) throws Exception {
		super(inCuso);		
	}

	public void lis() throws Exception {
		super.lis();		
	}

	public void cap() throws Exception {
		super.cap();
	}

	public void graba() throws Exception {
		if (icuso.inData.getString("item").equals("") ) {
			icuso.inData.put("item", "0");
		}
		super.graba();
	}

	public void fin() throws Exception {
		super.fin();
	}

}