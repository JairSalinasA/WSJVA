package comun;

import srv.*;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

public class gDML extends cuso {

    String eje ;
    JSONObject JSParms;
    JSONObject inData;
    JSONObject outData;
 

    ///// Para una sola consulta
    public gDML(String datos, String qry) throws Exception {
        super(datos);
        qry = ponInData(qry);
    }

    ///// Para fetch, consultas continuas, sigue es el último registro
    public gDML(String datos, String qry, int sigue) throws Exception {
        super(datos);
        qry = ponInData(qry);
    }


    ///// Insert, Update o Delete con consulta de regreso
    public gDML(String datos, String qry, String dml) throws Exception {
        super(datos);
        qry = ponInData(qry);
    }


    public String ponInData(String sent) throws Exception {
        /// Parametros desde variables
        Iterator<String> ll = inData.keys();
        String llave, pllave;
        while (ll.hasNext()) {
            llave = ll.next();
            pllave = "@" + llave;
            if (sent.contains(pllave)) {
                sent = sent.replace(pllave, "'" + inData.getString(llave) + "'");
            }
            pllave = "#" + llave;
            if (sent.contains(pllave)) {
                sent = sent.replace(pllave, inData.getString(llave));
            }
        }
        return sent;
    }

    // public void DML() throws Exception {
    // DML((String) inData.get("cusop"), (String) inData.get("cuso"),
    // "0");
    // }

    // public void DML(String ini) throws Exception {
    // DML((String) inData.get("cusop"), (String) inData.get("cuso"),
    // ini);
    // }

    // public void DML(String cusop, String cuso, String ini) throws Exception {
    public void DML(String qry, String ini) throws Exception {

        // ini = valor del ? en el qry, default=0
        // ld= Qry o Select a regresar
        // fmt= DML (insert,update o delete)
        // qry
        String fmt = "Sentencia DML";
        qry = "Consulta a ejecutar";

        ////// Procesamos fmt
        if (!fmt.equals("")) {
            JSParms = new JSONObject(fmt);
            outData.put("fmt", JSParms.toString());
            if (JSParms.has("DML")) {
                eje = ponInData((String) JSParms.get("DML"));
                //-- int reg= dbx.ejePs(eje, new String[] {});
                //-- outData.put("dmlRegis", reg);
            }
        }

        ////// Procesamos qry
        // qry = ponInData((qry);

        JSONArray flds = new JSONArray(), fldTipos = new JSONArray();
        eje = qry;

        // Si hay filtros sustituimos
        if (eje.indexOf(":") != -1) {
            if (JSParms.has("Filtro")) {
                JSParms = JSParms.getJSONObject("Filtro");
                flds = (JSONArray) JSParms.get("fltFld");
                fldTipos = (JSONArray) JSParms.get("fltTpo");
            }

            // Identificamos Filtros
            String parm, vparm, condi, coma;
            int hasta = 0;
            for (int desde = eje.indexOf(":"); desde != -1; desde = eje.indexOf(":")) {
                hasta = eje.indexOf(".", desde);
                if (hasta == -1) {
                    throw new Exception("Parametro sin terminación");
                }
                parm = eje.substring(desde + 1, hasta);
                vparm = "";

                ///// Desde Filtros
                if (inData.has(parm) && !inData.get(parm).equals("")) {
                    coma = "";
                    condi = (String) inData.get(parm);

                    for (int fldPos = 0; fldPos < flds.length(); fldPos++) {
                        if (flds.get(fldPos).equals(parm.substring(3))) {
                            if (fldTipos.get(fldPos).equals("C")) {
                                coma = "'";
                            }
                            break;
                        }
                    }

                    String[] aDato = condi.split("\\|");
                    if (aDato.length > 2) {
                        throw new Exception("Solo acepta dos rangos");
                    }

                    if (aDato.length > 0 && condi.contains("|") && coma.equals("")) { /////////// Rango Numeros o Fechas
                        if (!aDato[0].equals("")) { // Mayor o igual que
                            vparm = parm.substring(3) + " >= " + aDato[0];
                        }
                        if (aDato.length > 1) {
                            vparm = vparm + (vparm.equals("") ? "" : " and ") + parm.substring(3) + " <= " + aDato[1];
                        }
                    } else if (aDato.length > 0 && condi.contains("|") && coma.equals("'")) { ///////// Likes, cadena
                                                                                              ///////// empieza con o
                                                                                              ///////// termina con
                        vparm = parm.substring(3) + " like '" + condi.replace("|", "%") + "'";
                    } else if (condi.contains(",")) {///////////////////////////////////////// Lista
                        vparm = parm.substring(3) + " in (" + condi + ")";
                    } else { //////////////////////////////////////////////////////////// Igual
                        vparm = parm.substring(3) + " = " + coma + condi + coma;
                    }

                    vparm = "and " + vparm;
                }
                eje = eje.replace(":" + parm + ".", vparm);
            }
        }

        //////////////////////////////////////////////////////////
//        String[] parms = new String[] {};

        // if (eje.contains("?"))
        // parms = new String[] { ini, "N" };
        // jsQry = dcuso.dbconx.ejeQry(100, eje, parms);
        // outData.put("result", jsQry.toString());
    }

    public void fetch() throws Exception {
        // String cusop = (String) inData.get("cusop_org");
        // String cuso = (String) inData.get("cuso_org");
        // String _last = (String) inData.get("_last");
        // DML(cusop, cuso, _last);
    }

    public void nada() throws Exception {
        // bd.outData=inData;
    }

    public void filtro() throws Exception {
        // DML("ini.vaLogin", (String) inData.get("cuso_org"), "0");
    }

    public void borra() throws Exception {

    }

    public void edi() throws Exception {
        // DML();
    }

}