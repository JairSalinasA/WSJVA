package cusos.ingfnte;

import srv.cuso;

import org.json.JSONArray;

import comun.bkComun;
public class bolsa extends bkComun {
  
    public bolsa(cuso inCuso) throws Exception {
        super(inCuso);
        kComa = "'";
    }

    public void lis() throws Exception {

        JSONArray jer = new JSONArray();
        jer.put(new Object[] { ""
                + "<i>Año: </i>"
                + icuso.inData.get("anio")
                + "&nbsp;&nbsp;&nbsp;&nbsp;<i>Mes: </i>"
                + icuso.inData.get("mes")
                + "&nbsp;&nbsp;&nbsp;&nbsp;<i>Tfnte: </i>"
                + icuso.inData.get("tfnte")
                + "&nbsp;&nbsp;&nbsp;&nbsp;<i>Fuente: </i>"
                + icuso.inData.get("fuente")
                + "&nbsp;&nbsp;&nbsp;&nbsp;<i>Ramo: </i>"
                + icuso.inData.get("ramo")
                + "&nbsp;&nbsp;&nbsp;&nbsp;<i>Etiquetado: </i>"
                + icuso.inData.get("etiquetado")
               
            
                

        });

        icuso.outData.put("jerq", jer);

        jsVals.put("anio", icuso.inData.get("anio"));
        jsVals.put("mes", icuso.inData.get("mes"));
        jsVals.put("tfnte", icuso.inData.get("tfnte"));
        jsVals.put("fuente", icuso.inData.get("fuente"));
        jsVals.put("ramo", icuso.inData.get("ramo"));
        jsVals.put("etiquetado", icuso.inData.get("etiquetado"));
       

        super.lis();
    }

    public void cap() throws Exception {
        super.cap();
    }


    public void fin() throws Exception {
        super.fin();
    }

}