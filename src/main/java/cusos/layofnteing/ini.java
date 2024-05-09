package cusos.layofnteing;

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

        if (!dml.equals("3")) {//borrar
            String fnteing=  icuso.inData.getString("fnteing");
            String lfnteing=  icuso.inData.getString("lfnteing");
            String cri=  icuso.inData.getString("cri");
            String cnta=  icuso.inData.getString("cnta");
            String anterior=  icuso.inData.getString("anterior");

            if(fnteing.equals("")){
                throw new Exception("Es necersario capturar la Fuente de Ingresos");
            }

            if(lfnteing.equals("")){
                throw new Exception("El nombre no puede estar vacio");
            }

            if(cri.equals("")){
                throw new Exception("Es necersario capturar el CRI");
            }

            if(cnta.equals("")){
                throw new Exception("El cri capturado no tiene una cuenta asignada");
            }

            ejePs( "call p_fnte_ingresos_layo('"+fnteing+"','"+lfnteing+"','"+cri+"')");

        }

        dml="3";
        icuso.outData.put("dml", dml);
	}

	public void fin() throws Exception {
		super.fin();
	}

}