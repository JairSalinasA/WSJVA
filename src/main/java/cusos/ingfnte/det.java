package cusos.ingfnte;

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
                + "<i>Fnte: </i>"
                + icuso.inData.get("tfnte")
                + "&nbsp;&nbsp;<i>Ramo: </i>"
                + icuso.inData.get("ramo")
               
            
                

        });

        icuso.outData.put("jerq", jer);
        jsVals.put("tfnte", icuso.inData.get("tfnte"));
        jsVals.put("ramo", icuso.inData.get("ramo"));
       
        super.lis();
    }

    public void cap() throws Exception {
        super.cap();
    }


    public void fin() throws Exception {
        super.fin();
    }

}