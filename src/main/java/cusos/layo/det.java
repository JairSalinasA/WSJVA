package cusos.layo;
import srv.cuso;

import java.text.DecimalFormat;

import org.json.JSONArray;

import comun.bkComun;
public class det extends bkComun {
  
    public det(cuso inCuso) throws Exception {
        super(inCuso);
        kComa = "'";
    }

    public void lis() throws Exception {

         
         DecimalFormat formato = new DecimalFormat("$ ###,###,###,###,###.00");
         
        JSONArray jer = new JSONArray();
        jer.put(new Object[] { ""
                + "<i>Registro: </i>"
                + icuso.inData.get("archi")
                + "&nbsp;&nbsp;<i>Banco: </i>"
                + icuso.inData.get("lbnco")
                + "&nbsp;&nbsp;<i>Fecha Registro: </i>"
                + icuso.inData.get("fecha")
                + "&nbsp;&nbsp;<i>Clabe: </i>"
                + icuso.inData.get("clabe")
                + "&nbsp;&nbsp;<i>Cargos: </i>"
                + formato.format(Double.parseDouble((String) icuso.inData.get("cargos")))
                + "&nbsp;&nbsp;<i>Abonos: </i>"
                + formato.format(Double.parseDouble((String) icuso.inData.get("abonos")))
                + "&nbsp;&nbsp;<i>Saldo: </i>"
                + formato.format(Double.parseDouble((String) icuso.inData.get("saldo")))
            
                

        });

        icuso.outData.put("jerq", jer);
        jsVals.put("archi", icuso.inData.get("archi"));
        jsVals.put("lbnco", icuso.inData.get("lbnco"));
        jsVals.put("fecha", icuso.inData.get("fecha"));
        jsVals.put("clabe", icuso.inData.get("clabe"));
        jsVals.put("cargos", icuso.inData.get("cargos"));
        jsVals.put("abonos", icuso.inData.get("abonos"));
        jsVals.put("saldo", icuso.inData.get("saldo"));
       
       
        super.lis();
    }

    public void cap() throws Exception {
        super.cap();
    }


    public void fin() throws Exception {
        super.fin();
    }

}