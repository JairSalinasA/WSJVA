package cusos.expe;

import srv.cuso;

import org.json.JSONArray;

import comun.bkComun;

public class ini extends bkComun {

	public ini(cuso inCuso) throws Exception {
		super(inCuso);
		kComa="'";
	}

	public void lis() throws Exception {
		super.lis();		


		jsVals.put("usua", icuso.inData.get("usua"));
		jsVals.put("nombre", icuso.inData.get("nombre"));

		JSONArray jer = new JSONArray();
		jer.put(new Object[] { icuso.inData.get("usua"), icuso.inData.get("nombre") });
		icuso.outData.put("jerq", jer);
	}

	public void cap() throws Exception {

		jsVals.put("usua", icuso.inData.get("usua"));
		super.cap();
	}

	public void graba() throws Exception {

		icuso.inData.put("item", "0");
		super.graba();
	}

	public void fin() throws Exception {
		super.fin();
	}
}