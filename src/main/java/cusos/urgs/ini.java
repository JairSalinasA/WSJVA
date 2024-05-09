package cusos.urgs;

import srv.cuso;
//import org.json.JSONArray;
//import org.json.JSONObject;

import comun.bkComun;

public class ini extends bkComun {

	public ini(cuso inCuso) throws Exception {
		super(inCuso);
		kComa="'";
	}

	public void lis() throws Exception {
//		lisFil = new JSONObject();

//		lisFil.put("Filtros", new Object[] { "urg","lurg"}); // Listado y en Orden
//		lisFil.put("nFiltros", new Object[] { "Entidad","Nombre"}); // Nombres de Filtros
//		lisFil.put("sFiltros", new Object[] { "3","10"}); // Tamaño de Filtros
		super.lis();
		
	}

	public void lism() throws Exception {
		super.lis();
	}

	public void lisf() throws Exception {
		super.lis();
	}


	public void graba() throws Exception {
		super.graba();
	}

	public void fin() throws Exception {
		super.fin();
	}

}