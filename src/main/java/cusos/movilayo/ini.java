package cusos.movilayo;


import srv.cuso;

import comun.bkComun;

public class ini extends bkComun {

	public ini(cuso inCuso) throws Exception {
		super(inCuso);
		kComa="'";
	}

	public void lis() throws Exception {
		super.lis();		
	}

	public void cap() throws Exception {
		super.cap();
	}

	public void graba() throws Exception {
        String dml = icuso.inData.getString("dml");

          String regis = icuso.inData.getString("regis");

        if (!dml.equals("o")) {//genera poliza contable
           
            //ejePs( "call p_layout_det_reg_conta("+regis+")");//genera la poliza contable

        }

        if (!dml.equals("oo")) {//elimina poliza contable
            //ejePs( "call p_layout_det_elimi_conta("+regis+")");//elimina la poliza contable


        }
		
        dml="2";
        icuso.outData.put("dml", dml);

	}

	public void quitar() throws Exception {
		String hola="";
	}



	public void fin() throws Exception {
		super.fin();
	}

}