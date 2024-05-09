package cusos.particip;

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
		if (icuso.inData.has("ing")) {
			eje = ""
					+ " insert into visrmunic_ing ( "
					+ "  even,tram,fecha,monto,ficha,fficha)	"
					+ " select "
					+ "  #ing ,#even ,@ifecha ,#monto ,@ficha ,@fficha where 1=1 ";
			icuso.prop.setProperty("dml2", eje);
			// eje = icuso.ponInData(eje);
			// icuso.outData.put("dml", 2);
			// nregis = ejePs(eje, new String[] {});

		}
		// else
		super.graba();
	}

	public void fin() throws Exception {
		super.fin();
	}

}