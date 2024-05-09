package cusos.comun;

import srv.cuso;
import srv.shkException;
import java.lang.Math;
import org.json.JSONObject;
import org.joda.time.*;

public class ini {

    public String eje;
    public cuso dcuso;

    public ini(cuso inCuso) throws Exception {
        dcuso = inCuso;
    }

    public void login() throws Exception {
        JSONObject jsQry;

        if (dcuso.usua == null)
            throw new Exception("Falta usuario");
        // dcuso.usua = dcuso.inData.getString("usua");

        if (!dcuso.inData.has("paswd"))
            throw new Exception("Faltan credenciales");

        String eje = ""
                + "select c.usua  ,c.nombre lusua ,c2.usua pasa "
                + "from cusuarios0 c  "
                + "  left join cusuarios0 c2 on c2.usua=c.usua and c2.ccont is not null and c2.ccont = crypt(?,c2.ccont) "
                + "where c.usua= ?"
                + "";

        jsQry = dcuso.dbx.ejeQry(eje, new String[] {
                dcuso.inData.getString("paswd"), "C",
                dcuso.usua, "N"
        });

        if (dcuso.dbx.gNumRows(jsQry) == 0) {
            throw new srv.shkException("No existe usuario ");
        }

        if (dcuso.dbx.JSTblgetvCampo(jsQry, "pasa") == null) {
            throw new srv.shkException("No corresponde contraseña ");
        }

        dcuso.outData.put("nomUsu", (String) dcuso.dbx.JSTblgetvCampo(jsQry, "lusua"));

        // Identificamos Rol
        eje = ""
                + " select "
                + "  usur.rol ,lrol "
                + " from cusurol0 usur "
                + "   left join croles0 on croles0.rol=usur.rol "
                + " where usua=" + dcuso.usua
                + "";
        if (dcuso.inData.has("rol"))
            eje += " and usur.rol=" + dcuso.inData.getString("rol");

        dcuso.dbx.qry(eje);
        dcuso.outData.put("roles", dcuso.dbx.jsLocTbl.toString());

        if (dcuso.dbx.gNumRows() == 0)
            throw new shkException("No existen roles para el usuario");

        dcuso.outData.put("usua", dcuso.usua);
    }

    public void cusosRol() throws Exception {
        dcuso.rol = (String) dcuso.inData.get("rol");
        String dvlp = "";
        if (!dcuso.dvlp)
            dvlp += " and not c.dvlp ";

        dcuso.dbx.qry("select rol,lrol from croles0 r where rol=" + dcuso.rol);
        eje = (String) dcuso.dbx.JSTblgetvCampo("lrol");
        dcuso.outData.put("nomRol", eje);

        eje = ""
                + " select "
                + "   c.pack||'.'||c.clas||'.'||c.metd rcuso "
                + "  ,c.lcuso nombre  "
                + "  ,c.cuso_altf cusof  "
                + "  ,r.rol ,r.lrol ,grupo "
                + " from ccusos0 c "
                + "   left join croles0 r on r.rol=" + dcuso.rol
                + "   inner join ccasrol0 cr "
                + "        on (cr.rol="+ dcuso.rol
                + "        or cr.rol in (select p.prol from croles0 p where p.rol="+ dcuso.rol+ "  )) "
                + "        and cr.pack=c.pack and cr.clas=c.clas and cr.metd=c.metd "
                + "        and cr.pack=c.pack and cr.clas=c.clas and cr.metd=c.metd"
                + " where c.cuso_pl  is null  "
                + "   and activo "
                + "   and c.grupo is null " + dvlp
                + ""
                + " union "
                + ""
                + " select "
                + "   c.pack||'.'||c.clas||'.'||c.metd rcuso"
                + "  ,grupo nombre "
                + "  ,c.cuso_altf cusof  "
                + "  ,r.rol ,r.lrol ,grupo "
                + " from ccusos0 c "
                + "   inner join ccasrol0 cr on cr.rol=" + dcuso.rol
                + " and cr.pack=c.pack and cr.clas=c.clas and cr.metd=c.metd "
                + "   left join croles0 r on r.rol=" + dcuso.rol
                + " where c.cuso_pl is null "
                + "   and activo "
                + "   and c.grupo  is not null   " + dvlp
                + "   and orden < 1"
                + " order by 2 "
                + "";

        dcuso.dbx.qry(eje);
        dcuso.dbx.ejePs("insert into slogcusos (sesion,pack,clas,metd,usua,rol,id,ip) values (?,?,?,?,?,?,?,?)",
                new String[] {
                        dcuso.sscuso, "N",
                        dcuso.pack, "C",
                        dcuso.clas, "C",
                        dcuso.metd, "C",
                        dcuso.usua, "C",
                        dcuso.rol, "N",
                        dcuso.sscuso, "N",
                        dcuso.ipdom, "C"
                });

        dcuso.outData.put("stx", dcuso.sscuso);
        dcuso.outData.put("icusos", dcuso.dbx.jsLocTbl.toString());
    }

    public void valValid() throws Exception {
        // dcuso.dbx = new srv.dbCon(dcuso);

        ///// Sacamos el eje de la lista, este es para prueba

        // List<String> myList = new ArrayList<String>(Arrays.asList(s.split(","))); -->
        // Para Etiquetas
        JSONObject jsResult;
        String vv = dcuso.inData.getString("vv");
        String lay = dcuso.inData.getString("lay");
        String vvid = dcuso.inData.getString("vvid");
        String vvqry = "";
        if (dcuso.inData.has("vvqry"))
            vvqry = dcuso.inData.getString("vvqry");

        if (vvqry.equals("")) {
            String eje = " select query,labels from cvalvalid where nomqry='" + vv + "'";

            jsResult = dcuso.dbx.ejeQry(eje, new String[] {});
            eje = (String) dcuso.dbx.JSTblgetvCampo(jsResult, "query");
            // String lbls = (String) dcuso.dbx.JSTblgetvCampo(jsResult, "labels");
            eje = eje.replace("{", "[").replace("}", "]");
            eje = dcuso.ponInData(eje);
            vvqry = eje.replace("[", "{").replace("]", "}");
        }
        // eje = vvqry;

        eje = " select * from (" + dcuso.ponInData(vvqry) + ") q ";
        /////////////

        jsResult = dcuso.dbx.ejeQry(eje + " limit ? ", new String[] { "500", "N" });

        if (lay.equals("-1"))
            dcuso.outData.put("repinta", "1");
        dcuso.outData.put("jsResult", jsResult.toString());
        // dcuso.outData.put("lbsls", lbls);
        dcuso.outData.put("vv", vv);
        dcuso.outData.put("vvid", vvid);
        dcuso.outData.put("vvqry", vvqry);
    }

    public void genToken() throws Exception {
        String usua = dcuso.inData.getString("usua");
        // dcuso.dbx = new srv.dbCon(dcuso);
        JSONObject jsQry = dcuso.dbx.ejeQry(
                "select email from cusuarios0 where usua=?", new String[] { usua, "N" });

        int token = (int) (10000 + Math.random() * 900000);
        String email = (String) dcuso.dbx.JSTblgetvCampo(jsQry, "email");
        DateTime orita = DateTime.now();

        //////////////////////////////////////////
        ////// Envia el token al email /////////
        comun.Correo mail = new comun.Correo();
        String resp = mail.sendEmail(email, token);

        if (!resp.equals("1")) {
            throw new Exception("Error en envio de correo");
        }
        //////////////////////////////////////////

        dcuso.outData.put("tk", token);
        dcuso.outData.put("usua", usua);
        dcuso.outData.put("orita", orita);

        dcuso.outData.put("ldFrm", "x");
        dcuso.outData.put("ldJS", "passw"); // Por si quiero que se ejecute un script
        dcuso.outData.put("nWIN", "-1");

    }

    ////// Solo poner el usua y pswd como parámentros desde el bak y listo
    public void grabPswd() throws Exception {
        String usua = dcuso.inData.getString("usua").trim();
        String pswd = dcuso.inData.getString("pswd").trim();
        // dcuso.dbx = new srv.dbCon(dcuso);
        String eje = ""
                + " update cusuarios0 set "
                + "  ccont = crypt(?,gen_salt('md5')) "
                + " where usua=?"
                + "";

        int nregis = dcuso.dbx.ejePs(eje, new String[] { pswd, "C", usua, "N" });
        if (nregis == 0) {
            throw new Exception("no existe usuario");
        }
        dcuso.outData.put("ldFrm", "x");
        dcuso.outData.put("nWIN", "-1");
        dcuso.outData.put("ldJS", "window.location = 'index.html';");
    }
}
