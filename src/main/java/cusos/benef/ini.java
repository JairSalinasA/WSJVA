package cusos.benef;

import srv.cuso;
import org.json.JSONObject;
import comun.bkComun;

public class ini extends bkComun {
	public ini(cuso inCuso) throws Exception {
		super(inCuso);
		kComa = "'";
		if (! icuso.inData.getString("lay").equals("-1")) {
			JSONObject dvalFld = new JSONObject();
			qry("select rol,lrol from vrolbenef order by rol");
			while (barre())
				dvalFld.put(Integer.toString((int) valFld("rol")), valFld("lrol"));
			jsValFlds.put("rolList", dvalFld);
		}
	}

	public void lis() throws Exception {
		
		if (icuso.inData.getString("lay").equals("2")) /// Iniciamos con rol=1
			icuso.inData.put("fltrol", "1");
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
