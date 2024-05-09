/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cusos.banco;

/**
 *
 * @author christianmendoza
 */

import srv.cuso;
import org.json.JSONObject;
import comun.bkComun;


public class ini extends bkComun {
    public ini(cuso inCuso) throws Exception {
		super(inCuso);
		kComa="'";
	}
    
       

	public void lis() throws Exception {

		 lisFil = new JSONObject();
		 lisFil.put("Filtros", new Object[] {"banco","nombre"}); // Listado y en Orden
		 lisFil.put("nFiltros", new Object[] { "Banco","Nombre"}); // Nombres de Filtros
		 lisFil.put("sFiltros", new Object[] { "7","10"}); // Tamaño de Filtros
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

