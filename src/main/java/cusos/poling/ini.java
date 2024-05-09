package cusos.poling;

import srv.cuso;

import comun.bkComun;

public class ini extends bkComun {

    public ini(cuso inCuso) throws Exception {
        super(inCuso);
        kComa = "'";
    }

    public void lis() throws Exception {
        super.lis();
    }

    public void cap() throws Exception {
        super.cap();
    }

    
    public String registro() throws Exception {

        //String fecha = icuso.inData.getString("valor_fecha");
        String fecha = icuso.inData.getString("fecha");
        //String anio[] = fecha.split("/");
        String anio= fecha.substring(0,4);
        String fnte = icuso.inData.getString("fnte");
        String clabe = icuso.inData.getString("clabe");
        String monto = icuso.inData.getString("monto");
        monto = monto.replace("$", "").replace(",", "");

        String min_1 = icuso.inData.getString("min_1");
        String min_2 = icuso.inData.getString("min_2");
        String min_3 = icuso.inData.getString("min_3");
        String min_4 = icuso.inData.getString("min_4");
        String min_5 = icuso.inData.getString("min_5");
        String min_6 = icuso.inData.getString("min_6");
        String min_7 = icuso.inData.getString("min_7");
        String min_8 = icuso.inData.getString("min_8");
        String min_9 = icuso.inData.getString("min_9");
        String min_10 = icuso.inData.getString("min_10");
        String min_11 = icuso.inData.getString("min_11");
        String min_12 = icuso.inData.getString("min_12");

        String cnta = "81100";
        String[] separate = fecha.split("/");

        String regis = icuso.inData.getString("regis");

        if (min_12.equals("")) {
            throw new Exception("No se ha calculado el monto");
        }

        if (regis.equals("")) {
            qry("select nextval('pk_dregis') conse");
            regis = icuso.dbx.JSTblgetvCampo("conse").toString();
        }

        // cri
        qry("select cogcri cri, lcogcri lcri, case when ( clas is null) then -10000 else clas  end  as cri_clas from tfntes left join tcogcri t on cri=cogcri and cog is false where fnte='"
                + fnte + "'");
        String clas = icuso.dbx.JSTblgetvCampo("cri_clas").toString();

        if (clas.equals("-10000")) {
            throw new Exception("La fnte capturado no tiene CRI");
        }

        String cri = icuso.dbx.JSTblgetvCampo("cri").toString();

        if (clas.equals("")) {
            throw new Exception("El fondo capturado no tiene CRI");
        }

        if (fecha.equals("")) {
            throw new Exception("Falta capturar fecha");
        }
        if (clabe.equals("")) {
            throw new Exception("Falta capturar clabe");
        }

        if (fnte.equals("")) {
            throw new Exception("Falta capturar Fnte");
        }

        if (cnta.equals("")) {
            throw new Exception("Falta capturar el tipo de ingreso");
        }

        /* Capturamos en la tabla tfondos */

        eje= ""
        + "select "
        +"    coalesce(cfnte,lfnte) lfnte ,cri ,c.clas ,c.cogcri  "
        +" from tfntes f "
        +"   left join tcogcri c on not cog and cogcri=f.cri "
        +" where fnte='"+ fnte+ "'"
        +"";

        qry(eje);
        String lfnte = icuso.dbx.JSTblgetvCampo("lfnte").toString();
        //clas = icuso.dbx.JSTblgetvCampo("clas").toString();
        String kcri= icuso.dbx.JSTblgetvCampo("cogcri").toString();

        // tfondos
        // el regis=fndo
        eje = "insert into Tfondos (fndo, lfndo, anio, fnte, clabe, cri, min_1,min_2, min_3, min_4, min_5, min_6, min_7, min_8, min_9, min_10, min_11, min_12) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
        nregis = ejePs(eje,
                new String[] { regis, "N", lfnte, "C", anio, "N", fnte, "N", clabe, "C", cri, "C", min_1, "N", min_2,
                        "N", min_3, "N", min_4, "N", min_5, "N", min_6, "N", min_7, "N", min_8, "N", min_9, "N", min_10,
                        "N", min_11, "N", min_12, "N" });

        // header
        eje = "insert into tregis (regis ,descrip ,fecha ,monto ,teven ) values(?,?,'"+fecha+ "',?,9) ";
        nregis = ejePs(eje, new String[] { regis, "N", lfnte, "C", monto, "N" });

        eje = ""
        + " insert into tdregis ("
        + "   regis ,dregis ,fndo ,tmov ,cnta ,cntap ,kcog , monto "
        + " ) values ( "
        + "   ? ,? ,? ,1 ,'99', '811', ?, ?)  ";
        nregis = ejePs(eje, new String[] {
                regis, "N",
                regis, "N",
                regis, "N",
                kcri ,"C",
                //clas, "N",
                monto, "N"
        });

        eje = ""
        + " insert into tdregis ("
        + "   regis ,fndo ,tmov ,cnta ,cntap ,kcog , monto "
        + " ) values ( "
        + "   ? ,? ,-1 ,'99', '812', ?, ?)  ";

        nregis = ejePs(eje, new String[] {
                regis, "N",
                regis, "N",
                kcri ,"C",
                //clas, "N",
                monto, "N"
        });
        
        
        String url = icuso.inData.getString("url");
        if (!url.equals("")) {
            eje = "insert into tarchivos (archi,url,tarchi ) values (?,?,0) ";
            nregis = ejePs(eje, new String[] { regis, "N", url, "C" });
        }
        icuso.inData.put("regis", regis);
        //icuso.inData.put("dregis", dregis);
        icuso.inData.put("cnta", cnta);
        icuso.inData.put("ejer", anio);// año
        //icuso.inData.put("per", separate[1]);// mes
        icuso.inData.put("clas", clas);
        icuso.inData.put("monto", monto);

        return regis;

    }

    public void borrar() throws Exception {
        String regis = icuso.inData.getString("regis");

        eje = "delete from tdregis where regis=?";
        nregis = ejePs(eje, new String[] { regis, "N" });

        eje = "delete from tregis where regis=?";
        nregis = ejePs(eje, new String[] { regis, "N" });

        eje = "delete from Tfondos where fndo=?";
        nregis = ejePs(eje, new String[] { regis, "N" });

        eje = "delete from tarchivos where archi=?";
        nregis = ejePs(eje, new String[] { regis, "N" });

    }

    public void graba() throws Exception {

        String dml = icuso.inData.getString("dml");

        String regis = icuso.inData.getString("regis");

        if (dml.equals("1")) {// inserta
            regis = registro();

        }

        if (dml.equals("2")) {// editar
            borrar();
            regis = registro();
        }

        if (dml.equals("3")) {// eliminar
            borrar();
        }

        if (!dml.equals("3")) {// Si no es borrar regresa nuevo registro
            eje = "select * from (" + icuso.ponInData(prop.getProperty("lis")) + ") lisQ where regis =" + regis;
            jsResult = ejeQry(eje, new String[] {});
        }
        icuso.outData.put("dml", dml);

    }

    public void fin() throws Exception {
        super.fin();
    }

}