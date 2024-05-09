package cusos.devpred;

import comun.bkComun;
import srv.cuso;
import org.json.JSONArray;

public class cmprb extends bkComun {

    public cmprb(cuso inCuso) throws Exception {
        super(inCuso);
    }

    public void lis() throws Exception {
        eje = (String) icuso.inData.get("fecha");
        String fmtFec = eje.substring(8, 10) + "/" + eje.substring(5, 7) + "/" + eje.substring(0, 4);

        JSONArray jer = new JSONArray();
        jer.put(new Object[] { ""
                + "<i>Trámite: </i>"
                + icuso.inData.get("solp")
                + "&nbsp;&nbsp;&nbsp;"
                + fmtFec
        });

        jer.put(new Object[] { ""
                + "<i>Solicitud: </i>"
                + icuso.inData.get("solp")
                + "&nbsp;&nbsp;&nbsp;"
                + icuso.inData.get("lbenef")
        });

        icuso.outData.put("jerq", jer);

        jsVals.put("solp", icuso.inData.get("solp"));

        super.lis();
    }

    public void cap() throws Exception {

        jsVals.put("solp", icuso.inData.get("solp"));
        // jsVals.put("even", icuso.inData.get("even"));
        super.cap();
    }

    public void graba() throws Exception {
         super.graba();
    }

    public void fin() throws Exception {
        super.fin();
    }
}
