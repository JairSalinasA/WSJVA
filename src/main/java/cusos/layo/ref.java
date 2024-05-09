package cusos.layo;


import srv.cuso;

import org.json.JSONArray;

import comun.bkComun;
public class ref extends bkComun {
  
    public ref(cuso inCuso) throws Exception {
        super(inCuso);
        kComa = "'";
    }

    public void lis() throws Exception {

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
                + "&nbsp;&nbsp;<i>Referencia: </i>"
                + icuso.inData.get("crefer")
            
                

        });

        icuso.outData.put("jerq", jer);
        jsVals.put("archi", icuso.inData.get("archi"));
        jsVals.put("lbnco", icuso.inData.get("lbnco"));
        jsVals.put("fecha", icuso.inData.get("fecha"));
        jsVals.put("clabe", icuso.inData.get("clabe"));
        jsVals.put("crefer", icuso.inData.get("crefer"));
       
        super.lis();
    }

    public void cap() throws Exception {
        super.cap();
    }


    public void fin() throws Exception {
        super.fin();
    }

}