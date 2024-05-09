package cusos.concilia;
import srv.cuso;

import org.json.JSONArray;

import comun.bkComun;
public class det extends bkComun {
  
    public det(cuso inCuso) throws Exception {
        super(inCuso);
        kComa = "'";
    }

    public void lis() throws Exception {

        JSONArray jer = new JSONArray();
        jer.put(new Object[] { ""
                + "<i>Movimiento: </i>"
                + icuso.inData.get("mov")
                + "&nbsp;&nbsp;<i>Clabe: </i>"
                + icuso.inData.get("clabe")
                + "&nbsp;&nbsp;<i>Año: </i>"
                + icuso.inData.get("anio")
                + "&nbsp;&nbsp;<i>Mes: </i>"
                + icuso.inData.get("mes")
           
        });

        icuso.outData.put("jerq", jer);

        jsVals.put("mov", icuso.inData.get("mov"));
        jsVals.put("clabe", icuso.inData.get("clabe"));
        jsVals.put("anio", icuso.inData.get("anio"));
        jsVals.put("mes", icuso.inData.get("mes"));
       
        super.lis();
    }

    public void cap() throws Exception {
        super.cap();
    }


    public void fin() throws Exception {
        super.fin();
    }

}