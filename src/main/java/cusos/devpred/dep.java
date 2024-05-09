package cusos.devpred;

import comun.bkComun;
import srv.cuso;
import java.text.NumberFormat;

import org.json.JSONArray;

public class dep extends bkComun {

    public dep(cuso inCuso) throws Exception {
        super(inCuso);
    }

    public void lis() throws Exception {
        eje = (String) icuso.inData.get("fecha");
        String fmtFec = eje.substring(8, 10) + "/" + eje.substring(5, 7) + "/" + eje.substring(0, 4);
        JSONArray jer = new JSONArray();
        jer.put(new Object[] { ""
                + "<i>Trámite: </i> "
                + icuso.inData.get("even")
                + "&nbsp;&nbsp;&nbsp;"
                + fmtFec
        });
        jer.put(new Object[] { ""
                + "<i>Solic Pago: </i> "
                + icuso.inData.get("dregis")
                + "&nbsp;&nbsp;&nbsp;"
                + icuso.inData.get("lmunpo")
                + "&nbsp;&nbsp;&nbsp;"
                + NumberFormat.getCurrencyInstance().format(Double.parseDouble((String) icuso.inData.get("monto")))
        });
        icuso.outData.put("jerq", jer);
        jsVals.put("dregis", icuso.inData.get("dregis"));
        super.lis();
    }

    public void cap() throws Exception {
        jsVals.put("dregis", icuso.inData.get("dregis"));
        super.cap();
    }

    public void graba() throws Exception {
        if (icuso.inData.get("dml").equals("1")) { // obten k
            eje = ""
                    + " select coalesce(max(item),-1)+1 item "
                    + " from tarchivos where archi="
                    + icuso.inData.get("dregis");
            ejeQry(eje);
            icuso.inData.put("item",gFld("item").toString());
        }
        super.graba();
    }

    public void fin() throws Exception {
        super.fin();
    }

}
