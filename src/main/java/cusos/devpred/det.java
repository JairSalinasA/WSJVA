package cusos.devpred;

import comun.bkComun;
import srv.cuso;
import org.json.JSONArray;
import java.io.InputStream;

public class det extends bkComun {

    public det(cuso inCuso) throws Exception {
        super(inCuso);
    }

    public void lis() throws Exception {
        eje = (String) icuso.inData.get("fecha");
        String fmtFec = eje.substring(8, 10) + "/" + eje.substring(5, 7) + "/" + eje.substring(0, 4);

        JSONArray jer = new JSONArray();
        jer.put(new Object[] { ""
                + "<i>Trámite: </i>"
                + icuso.inData.get("comp")
                + "&nbsp;&nbsp;&nbsp;"
                + fmtFec
        });

        icuso.outData.put("jerq", jer);
        jsVals.put("comp", icuso.inData.get("comp"));
        // jsVals.put("fecha", icuso.inData.get("fecha"));
        // jsVals.put("anio", eje.substring(0, 4));

        super.lis();
    }

    //public void refresh() throws Exception {
    //    super.refresh();
   // }


    public void cap() throws Exception {

        jsVals.put("comp", icuso.inData.get("comp"));
        // jsVals.put("ejer", icuso.inData.get("ejer"));

        if (icuso.cuso.endsWith("1")) {
            eje = ""
                    + " select "
                    + "  fecha - interval '1 day' fecha "
                    + " ,anio "
                    + " from vdevpred v "
                    + " where comp=" + icuso.inData.get("comp");

            jsResult = ejeQry(eje, new String[] {});
        } else if (!icuso.inData.getString("cmprb").equals(""))
            throw new Exception("No se puede borrar/modificar ya tiene comprobantes");
        else
            super.cap();
    }

    public void graba() throws Exception {

        super.graba();

        String hK = icuso.inData.getString("comp");
        // String propHd= ;
        InputStream is = getClass().getClassLoader().getResourceAsStream("cusos.devpred.ini.properties");
        if (is != null)
            prop.load(is);
        else
            throw new Exception("No existe archivo properties");
        String hlis = prop.getProperty("lis");
        if (hlis!=null) {
            eje=hlis;
        }


    }

    public void fin() throws Exception {
        super.fin();
    }
}
