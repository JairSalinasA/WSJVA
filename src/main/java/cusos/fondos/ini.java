package cusos.fondos;

import srv.cuso;
//import org.json.JSONArray;
//import org.json.JSONObject;

import org.json.JSONObject;

import comun.bkComun;

public class ini extends bkComun {

	public ini(cuso inCuso) throws Exception {
		super(inCuso);
	}

	public void lis() throws Exception {

		if (icuso.rol.equals("10")) {
			eje = prop.getProperty("lis10");
			prop.setProperty("lis", eje);
		}

		if (!icuso.inData.getString("lay").equals("-1")) {
			JSONObject dvalFld = new JSONObject();
			dvalFld.put("2024", 2024);
			dvalFld.put("2022", 2022);
			dvalFld.put("2023", 2023);
			
			jsValFlds.put("anioList", dvalFld);
			icuso.inData.put("fltanio","2024"); // Iniciamos con año=2024
			icuso.outData.put("fltanio","2024"); // Para seleccionar en el frente

		}
		super.lis();
	}

	public void cap() throws Exception {
		super.cap();
	}

	public void graba() throws Exception {
		String dml = icuso.inData.getString("dml");

		if (dml.equals("o") ) {

			String estimado= icuso.inData.getString("estimado");
			estimado=estimado.replace("$", "").replace(",", "");

			if(estimado.equals("")){
				throw new Exception("El estimado no puede estar vacio");
			}
			

		}else{
			String clabe= icuso.inData.getString("clabe");

			if(clabe.length()!=18){
				throw new Exception("La clabe debe de contener 18 digitos");
			}
			//agregar a tbenefi

			String rfc_cap=icuso.inData.getString("rfc");
			eje= ""+
                " select count(*) con from tbenefi where rfc='"+rfc_cap+"'";
            qry(eje);

            String validar= icuso.dbx.JSTblgetvCampo("con").toString();

            if(validar.equals("0")){
				String nombre_cap=icuso.inData.getString("nombre");
				String direccion_cap=icuso.inData.getString("direccion");

				qry("select nextval('sbenef') conse");
				String id_rfc = icuso.dbx.JSTblgetvCampo("conse").toString();
				eje = " insert into tbenefi (usua, rfc, nombre, rol,  direccion  ) values (?, UPPER(?) , UPPER(?), 1, UPPER(?) ) ";
				nregis = ejePs(eje, new String[] { id_rfc, "N", rfc_cap, "C", nombre_cap, "C", direccion_cap, "C" });
			}
			
			
		}
		super.grabaq();
		if (icuso.outData.getString("dml").equals("o"))
			icuso.outData.put("dml", "2");

	}

	public void fin() throws Exception {
		super.fin();
	}

}