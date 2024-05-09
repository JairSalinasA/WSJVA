package cusos.bancos;

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
        kComa = "'";
    }

    public void lis() throws Exception {

        // yoyo: los filtros se declaran en la consulta
        // lisFil = new JSONObject();
        // lisFil.put("Filtros", new Object[] {"banco","nombre"}); // Listado y en Orden
        // lisFil.put("nFiltros", new Object[] { "Banco","Nombre"}); // Nombres de
        // Filtros
        // lisFil.put("sFiltros", new Object[] { "7","10"}); // Tamaño de Filtros
        
        super.lisLimit = 20;
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
