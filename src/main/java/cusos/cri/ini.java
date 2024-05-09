package cusos.cri;

import srv.cuso;

import comun.bkComun;

public class ini extends bkComun {

	public ini(cuso inCuso) throws Exception {
		super(inCuso);
		kComa = "'";
	}

	public void lis() throws Exception {
		super.lis();
	}

	public void cap() throws Exception {
		super.cap(); 
	}

	public void graba() throws Exception {

		String dml = icuso.inData.getString("dml");

		if (dml.equals("1") || dml.equals("2")) {// inserta
			String cnta= icuso.inData.getString("cnta");

			if(!cnta.equals("")){
				if(!cnta.substring(0,1).equals("4")){
					throw new Exception("Las cuentas para el CRI deben ser cuentas 4");
				}
			}
	    }
		super.graba();
	}

	public void fin() throws Exception {
		super.fin();
	}

}