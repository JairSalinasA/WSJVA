package cusos.proys;

import srv.cuso;

import comun.bkComun;

public class ini extends bkComun {

	public ini(cuso inCuso) throws Exception {
		super(inCuso);
	}

	public void lis() throws Exception {
		//lisFil = new JSONObject();

		//lisFil.put("Filtros", new Object[] { "fnte","lfnte"}); // Listado y en Orden
		//lisFil.put("nFiltros", new Object[] { "Fuente","Nombre"}); // Nombres de Filtros
		//lisFil.put("nFiltros", new Object[] { "Fuente","Nombre"}); // Nombres de Filtros
		//lisFil.put("sFiltros", new Object[] { "3","10"}); // Tamaño de Filtros
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