package cusos.pagos;

import srv.cuso;

import comun.bkComun;

public class ini extends bkComun {

	public ini(cuso inCuso) throws Exception {
		super(inCuso);
	}

	public void lis() throws Exception {
		super.lis();
	}

	public void cap() throws Exception {
		if (icuso.inData.get("cuso").equals("pagos.ini.cap_xpago") && icuso.inData.get("pago").equals(""))
			throw new Exception("CxPagar no tiene pago");

		jsVals.put("cxp", icuso.inData.get("cxp"));
		super.cap();
	}

	public void graba() throws Exception {
		String mdml = (String) icuso.inData.get("dml");
		if (mdml.equals("o")) {
			prop.setProperty("dml2", "delete from vpagos where pago=" + icuso.inData.get("pago"));
			icuso.inData.put("dml", "2");
		}
		super.graba();
		// icuso.outData.put("dml", 2);
	}

	public void fin() throws Exception {
		super.fin();
	}

}