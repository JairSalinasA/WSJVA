package cusos.salban;

import comun.bkComun;
import srv.cuso;

import org.json.JSONArray;

public class saldet extends bkComun {

    public saldet(cuso inCuso) throws Exception {
        super(inCuso);
        kComa = "'";
    }

    public void lis() throws Exception {

        JSONArray jer = new JSONArray();
        String fecha = (String) icuso.inData.get("fecha");

        jer.put(new Object[] { ""
                + icuso.inData.get("bnco") + " - " + icuso.inData.get("lbnco")
                + "&nbsp;&nbsp;&nbsp; Registro:"
                + icuso.inData.getString("archi")+ "&nbsp;&nbsp;"
                + fecha.substring(0, 19)
        });

        icuso.outData.put("jerq", jer);
        // jsVals.put("dett", icuso.inData.get("dett"));

        lisLimit = 100;

        super.lis();
    }

    public void fin() throws Exception {
        super.fin();
    }

}
