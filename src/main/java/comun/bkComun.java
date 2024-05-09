package comun;

import srv.cuso;
import org.json.JSONArray;
import org.json.JSONObject;
//import org.postgresql.PGNotification;
//import org.postgresql.shaded.com.ongres.scram.common.gssapi.Gs2AttributeValue;

//import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

//import java.util.ArrayList;)

public class bkComun {

    public String dtQry;

    public String kComa = "";
    public String borQry;
    public String eje;
    public int nregis;
    public JSONObject jsRel = new JSONObject();

    public boolean regRegre = true;
    public boolean regEdi = true;
    public boolean regBor = true;

    public String[] lisLbl; //// Etiquetas del Lis

    public String[] lisInp = new String[] {}; /// Tipos de Input inLine
    public String[] lisInpS = new String[] {}; /// Tamaño de los Input inLine

    public int lisLimit = 35;
    public int lisOffset = 0;

    public JSONObject jsResult; // Resultado de la lista
    public JSONObject jsParms;
    public JSONObject jsFlds = new JSONObject();
    public JSONObject jsValFlds = new JSONObject();
    public JSONObject lisFil; // Filtros en Lista

    public JSONObject jsVals = new JSONObject();
    public JSONObject jsFmt = new JSONObject(); // Formatos de la lista
    public cuso icuso;

    public Properties prop;

    public bkComun(cuso inCuso) throws Exception {
        icuso = inCuso;

        if (icuso.prop == null)
            throw new Exception("Falta Props");
        prop = icuso.prop;
    }

    public void lis() throws Exception {
        lis(false);
    }

    public void lis(boolean p) throws Exception {

        if (!prop.containsKey("lis"))
            throw new Exception("No lis params");

        ///// Para cuando es Offset
        if (icuso.inData.has("lisOffset") && !icuso.inData.getString("lisOffset").equals(""))
            lisOffset = icuso.inData.getInt("lisOffset");

        // Viene de menú Roles o de Fuente
        if (icuso.inData.has("deIni") || prop.containsKey("lisRegre")) {
            lisOffset = 0;
            if (prop.containsKey("lisRegre")) {
                lisLimit = -1;
                icuso.outData.remove("lisOffset");
            }

            ///// Filtro Inicial
            if (lisFil != null) {
                Object[] filtros = (Object[]) lisFil.get("Filtros");
                String k, v;
                for (int i = 0; i < filtros.length; ++i) {
                    k = (String) filtros[i];
                    if (lisFil.has(k)) {
                        v = (String) ((Object[]) lisFil.getJSONArray(k).get(0))[1];
                        icuso.inData.put("flt" + k, v);
                    }
                }
            }
        } else {
            if (icuso.inData.has("_filtro") && icuso.inData.get("_filtro").equals("1")) { // viene de filtro
                icuso.outData.put("repinta", "x");
                lisOffset = 0;
            } else
                icuso.outData.put("lisOffset", lisOffset);
        }

        eje = icuso.ponInData(prop.getProperty("lis"));
        if (icuso.filtroLis.size() > 0)
            icuso.outData.put("filtroLis", icuso.filtroLis.toString());
        if (p) {
            ejePs("update slogcusos set persdata='" + eje + "' where id= (select sscuso from tempses)");
        }
        eje = eje + (lisLimit == -1 ? "" : "\n limit " + String.valueOf(lisLimit)) + " offset "
                + String.valueOf(lisOffset);
        jsResult = ejeQry(eje);
        icuso.outData.put("lisLimit",lisLimit);
    }

    public void refresh() {

    }

    public void cap() throws Exception {
        // String qR = "";
        boolean capIns = icuso.cuso.endsWith("1");
        // String dml = icuso.cuso.substring(icuso.cuso.length()-1);

        if (capIns)
            if (prop.containsKey("capIns"))
                eje = prop.getProperty("capIns");
            else
                return;

        String kF = prop.getProperty("kFld");
        if (!icuso.inData.has(kF))
            throw new Exception("no hay campo llave");
        String kV = icuso.inData.getString(kF);

        String lkF = prop.getProperty("lkFld");
        // String lkV = "";
        // if (icuso.inData.has(lkF))
        // lkV = icuso.inData.getString(lkF);

        String[] alkF = {};
        String lkV = "";
        if (lkF !=null && !lkF.equals(""))
            alkF = lkF.split(",");

        if (!capIns && kV.equals(""))
            throw new Exception("no hay registro seleccionado");

        // prop.getProperty("lis");
        if (!capIns) {
            if (prop.containsKey("det"))
                eje = icuso.ponInData(prop.getProperty("det"));
            else {
                eje = prop.getProperty("lis");
                eje = icuso.ponInData("select * from (" + eje + ") lisQ where " + kF + "=" + kComa + kV + kComa);
            }

            for (int i = 0; i < alkF.length; i++) {
                if (icuso.inData.has(alkF[i]))
                    lkV += " - " + icuso.inData.getString(alkF[i]);
            }
        }

        jsResult = ejeQry(eje, new String[] {});
        icuso.outData.put("kF", kF);
        icuso.outData.put("lkF", lkF);
        icuso.outData.put("kV", (capIns?"":kV));
        icuso.outData.put("lkV", (capIns?"":lkV));

    }

    public void grabaq() throws Exception {
        String dml;

        if (!icuso.inData.has("dml"))
            throw new Exception("NoDML");
        dml = icuso.inData.getString("dml");

        eje = "dml" + dml; // 1=Ins 2=Edi 3=Bor
        if (!prop.containsKey(eje))
            throw new Exception("NoDMLQ");
        eje = prop.getProperty(eje);

        if (!dml.equals("1") && !eje.contains(" where "))
            throw new Exception("noDML" + dml + "Where");
        eje = icuso.ponInData(eje);

        jsResult = ejeQry(eje, new String[] {});
        icuso.outData.put("dml", dml);
    }

    // Graba desde lista
    public void graba() throws Exception {
        // lisIns = true;
        String dml, kFld, kVal;
        if (!icuso.inData.has("dml"))
            throw new Exception("NoDML");
        dml = icuso.inData.getString("dml");

        eje = "dml" + dml; // 1=Ins 2=Edi 3=Bor
        if (!prop.containsKey(eje))
            throw new Exception("NoDMLQ");
        eje = prop.getProperty(eje);

        if (!prop.containsKey("kFld"))
            throw new Exception("NoKFld");
        kFld = prop.getProperty("kFld");

        if (!dml.equals("1") && !eje.contains(" where "))
            throw new Exception("noDML" + dml + "Where");
        eje = icuso.ponInData(eje);

        kVal = icuso.inData.getString(kFld);

        if (prop.containsKey("kSq") && dml.equals("1")) { // Toma llave de secuencia cuando inserta
            ejeQry("SELECT cast( nextval('" + prop.getProperty("kSq") + "') as text) \"kVal\" ");
            kVal = valStrFld(0);
        }

        if (prop.containsKey("kSqCalc") && dml.equals("1")) { // Calcula siguiente consecutivo cuando inserta
            // eje = icuso.ponInData(prop.getProperty("lis"));
            String meje = "select cast(coalesce(max(" + kFld + "),-1)+1 as text) kv from ("
                    + icuso.ponInData(prop.getProperty("lis")) + ") lis ";
            qry(meje);
            kVal = gStrFld("kv");
        }

        // kFldi no necesita K para dml
        if (kVal == null || kVal.equals(""))
            throw new Exception("K sin dato");

        eje = eje.replace("#Key", kVal);
        nregis = ejePs(eje, new String[] {});

        if (!dml.equals("3")) {// Si no es borrar regresa nuevo registro
            eje = "select * from (" + icuso.ponInData(prop.getProperty("lis")) + ") lisQ where " + kFld + "=" + kComa
                    + kVal + kComa;
            jsResult = ejeQry(eje, new String[] {});
        }
        icuso.outData.put("dml", dml);
    }

    public void fin() throws Exception {
        ///// Cusos siguientes //////////
        eje = ""
                + " select "
                + "   pack||'.'||clas||'.'||metd cuso "
                + "  ,lcuso ,ico ,coalesce(cuso_altf,'') cusfr"
                + "  ,pack "
                + "  ,descrip ,coalesce(lay,'0') lay "
                + " from ccusos0 "
                + " where activo "
                + "   and cuso_pl= '" + icuso.cuso + "'"
                + " order by orden "
                + "";

        JSONObject tbm = ejeQry(eje);
        while (barre(tbm))
            jsRel.append("datos", new Object[] {
                    icuso.dbx.valStrFld(tbm, "cuso"),
                    icuso.dbx.valStrFld(tbm, "lcuso"),
                    icuso.dbx.valStrFld(tbm, "ico"),
                    icuso.dbx.valStrFld(tbm, "lay"),
                    icuso.dbx.valStrFld(tbm, "cusfr")
            });

        /// Identificamos Grupo
        qry(""
                + " select grupo from ccusos0 "
                + " where pack||'.'||clas||'.'||metd='" + icuso.cuso + "' "
                + "");

        String grpo = valStrFld("grupo");
        if (grpo != null) {
            eje = ""
                    + " select "
                    + "   pack||'.'||clas||'.'||metd cuso "
                    + "  ,lcuso ,ico ,cuso_alt"
                    + "  ,coalesce(script,'ldCuso('''||pack||'.'||clas||'.'||metd||''','||lay||')') eje "
                    + "  ,pack "
                    + "  ,descrip ,lay "
                    + " from ccusos0 "
                    + " where grupo= '" + grpo + "' and orden >=0 "
                    + "";
            tbm = ejeQry(eje);
            JSONObject jsGRel = new JSONObject();
            jsGRel.append("datos", new Object[] { grpo });
            while (barre(tbm))
                jsGRel.append("datos", new Object[] {
                        icuso.dbx.valStrFld(tbm, "lcuso"),
                        icuso.dbx.valStrFld(tbm, "descrip"),
                        icuso.dbx.valStrFld(tbm, "ico"),
                        icuso.dbx.valStrFld(tbm, "eje"),
                        icuso.dbx.valStrFld(tbm, "lay"),
                        icuso.dbx.valStrFld(tbm, "cuso_alt")
                });
            icuso.outData.put("jsGRel", jsGRel.toString());

        }

        ////// Llenamos los PROPS
        Set<String> dbProps = prop.stringPropertyNames();
        String nparm;
        for (String dbp : dbProps) {
            if (!(dbp.contains("front.") || dbp.contains("fvals.")))
                continue;
            nparm = dbp.substring(6);
            if (dbp.startsWith("front."))
                jsFlds.put(nparm, prop.getProperty(dbp));
            if (dbp.startsWith("fvals."))
                jsVals.put(nparm, (String) icuso.inData.get(nparm));
        }

        // if (jsFlds.length() > 0)
        icuso.outData.put("jsFlds", jsFlds.toString());
        if (jsRel != null)
            icuso.outData.put("jsRel", jsRel.toString());
        if (jsResult != null)
            icuso.outData.put("jsResult", jsResult.toString());
        if (lisFil != null)
            icuso.outData.put("lisFil", lisFil.toString());
        if (lisLbl != null && jsResult != null)
            jsResult.put("etiq", lisLbl);
        if (lisInp != null)
            icuso.outData.put("lisInp", lisInp);
        if (lisInpS != null)
            icuso.outData.put("lisInpS", lisInpS);
        if (jsVals != null)
            icuso.outData.put("JSVals", jsVals);
        if (jsValFlds != null)
            icuso.outData.put("JSValFlds", jsValFlds);

    }

    public JSONObject ejeQry(String eje) throws Exception {
        return ejeQry(eje, new String[] {});
    }

    public void qry(String eje) throws Exception {
        icuso.dbx.qry(eje);
    }

    public boolean barre() throws Exception {
        return icuso.dbx.barre();
    }

    public boolean barre(JSONObject tabla) throws Exception {
        return icuso.dbx.barre(tabla);
    }

    public boolean barre(String meje) throws Exception {
        return icuso.dbx.barre(meje);
    }

    public Object gReg(int reg) throws Exception {
        return icuso.dbx.JSTblgetReg(reg);
    }

    public JSONObject ejeQry(String eje, String[] flds) throws Exception {
        icuso.dbx.qry(eje, flds);
        return icuso.dbx.jsLocTbl;
    }

    public Object gFld(String fld) throws Exception {
        return icuso.dbx.JSTblgetvCampo(fld);
    }

    public Object gField(int regNum, int nCampo) throws Exception {
        return icuso.dbx.JSTblgetvCampo(regNum, nCampo);
    }

    public Object gFld(int reg, String fld) throws Exception {
        // return null;
        return icuso.dbx.JSTblgetvCampo(reg, fld);
    }

    public String gStrFld(String fld) throws Exception {
        return (String) icuso.dbx.JSTblgetvCampo(fld);
    }

    public int gIntFld(String fld) throws Exception {
        return (int) icuso.dbx.JSTblgetvCampo(fld);
    }

    public Object vFld(int reg, int fld) throws Exception {
        return ((Object[]) ((JSONArray) icuso.dbx.jsLocTbl.get("datos")).get(reg))[fld];
    }

    ///////////////// getFldVal
    public Object valFld(JSONObject tabla, String nCampo) throws Exception {
        return icuso.dbx.valFld(tabla, nCampo);
    }

    public Object valFld(String nCampo) throws Exception {
        return icuso.dbx.valFld(nCampo);
    }

    public Object valFld(int numCampo) throws Exception {
        return icuso.dbx.valFld(numCampo);
    }

    public String valStrFld(int numCampo) throws Exception {
        return (String) icuso.dbx.valFld(numCampo);
    }

    public String valStrFld(JSONObject tabla, String nCampo) throws Exception {
        return icuso.dbx.valStrFld(tabla, nCampo);
    }

    public String valStrFld(String nCampo) throws Exception {
        return icuso.dbx.valStrFld(nCampo);
    }

    public int gNumCols() throws Exception {
        return gNumCols(icuso.dbx.jsLocTbl);
    }

    public int gNumCols(JSONObject tabla) throws Exception {
        return ((Object[]) ((JSONArray) tabla.get("datos")).get(0)).length;
    }

    public int gNumRows() throws Exception {
        return gNumRows(icuso.dbx.jsLocTbl);
    }

    public int gNumRows(JSONObject tabla) throws Exception {
        return ((JSONArray) tabla.get("datos")).length();
    }

    public int ejePs(String eje) throws Exception {
        return ejePs(eje, new String[] {});
    }

    public int ejePs(String eje, String[] flds) throws Exception {
        return icuso.dbx.ejePs(eje, flds);
    }

    public boolean chkMen(String men) throws Exception {
        boolean regre = false;
        if (prop.containsKey(men)) {
            eje = prop.getProperty(men);
            String v = "";

            if (!eje.equals("") && icuso.inData.has(eje.replace("!", "")))
                v = icuso.inData.getString(eje.replace("!", ""));

            if (eje.equals("") ||
                    (v.equals("") && eje.contains("!")) ||
                    (!v.equals("") && !eje.contains("!")))
                regre = true;
        }

        return regre;
    }
}