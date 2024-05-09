package cusos.layo;
import srv.cuso;

import java.lang.reflect.Method;

import org.json.JSONArray;

import comun.bkComun;
public class comp extends bkComun {
    private String id, regis, item;

    public comp(cuso inCuso) throws Exception {
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
                + "&nbsp;&nbsp;<i>Dregis: </i>"
                + icuso.inData.get("dregis")
                + "&nbsp;&nbsp;<i>Fecha Archivo: </i>"
                + icuso.inData.get("feceven")
            
                

        });

        icuso.outData.put("jerq", jer);
        jsVals.put("regis", icuso.inData.get("regis"));
        jsVals.put("lbnco", icuso.inData.get("lbnco"));
        jsVals.put("fecha", icuso.inData.get("fecha"));
        jsVals.put("clabe", icuso.inData.get("clabe"));
        jsVals.put("dregis", icuso.inData.get("dregis"));
        jsVals.put("item", icuso.inData.get("dregis"));
       
        super.lis();
    }

    public void cap() throws Exception {

         JSONArray jer = new JSONArray();

        icuso.outData.put("jerq", jer);
        jsVals.put("regis", icuso.inData.get("regis"));
        jsVals.put("lbnco", icuso.inData.get("lbnco"));
        jsVals.put("fecha", icuso.inData.get("fecha"));
        jsVals.put("clabe", icuso.inData.get("clabe"));
        jsVals.put("dregis", icuso.inData.get("dregis"));
        jsVals.put("item", icuso.inData.get("dregis"));
        
        super.cap();
    }


    public void fin() throws Exception {
        super.fin();
    }

    public void graba() throws Exception {
    String dml = icuso.inData.getString("dml");

        if (dml.equals("1")) {//insertar
            qry("select nextval('pk_dregis') conse");
            id = icuso.dbx.JSTblgetvCampo("conse").toString();
            Insertar();
        }

        if (dml.equals("2")) {//update
            id = icuso.inData.getString("id");
            Borrar();
            Insertar();
        }

        if (dml.equals("3")) {//borrar
            id = icuso.inData.getString("id");
            Borrar();
        }

        if (!dml.equals("3")) {// Si no es borrar regresa nuevo registro
            eje = "select * from (" + icuso.ponInData(prop.getProperty("lis")) + ") lisQ where id =" + id;
            jsResult = ejeQry(eje, new String[] {});
        }
        icuso.outData.put("dml", dml);
    }

    public void Borrar() throws Exception{
            eje = "delete from tdregis where dregis=" + id;
            nregis = ejePs(eje);
    }

    public void Insertar() throws Exception{
            regis = icuso.inData.getString("regis");
            
            item = icuso.inData.getString("item");
            String cnta = icuso.inData.getString("cnta");
            //cnta=cnta.trim();
            String cri = icuso.inData.getString("cri");
            String monto = icuso.inData.getString("monto");
            monto = monto.replace("$", "").replace(",", "");


            eje = "insert into tdregis ( regis, dregis, auxi , cnta, tmov,  monto , ldregis, feceven, fndo, crefer, item, even, kcog)\n" +
             "select b.regis, ? dregis, b.auxi, ? cnta, (b.tmov *-1) tmov, ? monto, b.ldregis, b.feceven, b.fndo, b.crefer, b.item, b.even, ? cri from tdregis b where b.dregis=? ";
            nregis = ejePs(eje, new String[] {
                    id, "N",
                    cnta, "C",
                    monto,"N",
                    cri,"C",
                    item, "N"
            });
    }

}