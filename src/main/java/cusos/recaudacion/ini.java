package cusos.recaudacion;

import srv.cuso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import java.io.OutputStream;
import org.json.JSONObject;

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

        if(icuso.inData.getString("cuso").equals("recaudacion.ini.cap_repo")){

            String regis = icuso.inData.getString("regis");
            String reca = icuso.inData.getString("reca");

            if (regis.equals(reca)){
                throw new Exception("Ya esta generada la recaudacion");
            }

        }
    }

    public void graba() throws Exception {  

        String dml = icuso.inData.getString("dml");
        String regis = icuso.inData.getString("regis");

        if (dml.equals("1")) {// inserta
            regis = generarRegistro();
        }

        if (dml.equals("2")) {// editar
            borrar();
            regis = generarRegistro();
        }

        if (dml.equals("3")) {// eliminar
            borrar();
        }

        if (dml.equals("o")) {//generamos la recaudacion o la borrala
			recaudaciones();
			icuso.inData.put("dml", "2");  
            dml="2";
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


    public String generarRegistro() throws Exception {//no genera devengado hasta el recaudado

       
        String fecha = icuso.inData.getString("fecha");

        if (fecha.equals("")) {
            throw new Exception("Falta capturar fecha");
        }
        String respuesta = icuso.inData.getString("respuesta");

        String descrip = icuso.inData.getString("concepto");
        descrip=descrip.replace("\"", "\\\"");
        String fndo = icuso.inData.getString("fndo");
        String clabe = icuso.inData.getString("clabe");
        String kcri = icuso.inData.getString("cri");
       
        
        if (clabe.equals("")) {
            throw new Exception("Falta capturar Clabe");
        }

        if (fndo.equals("")) {
            throw new Exception("Falta capturar Fndo");
        }

        String rfc_cap = icuso.inData.getString("rfc");
        String nombre_cap = icuso.inData.getString("nombre");
        String direccion_cap = icuso.inData.getString("direccion");

        rfc_cap= rfc_cap.toUpperCase();
        nombre_cap= nombre_cap.toUpperCase();
        direccion_cap= direccion_cap.toUpperCase();

       
        if (rfc_cap.equals("")) {
            throw new Exception("Falta capturar RFC");
        }

        if (nombre_cap.equals("")) {
            throw new Exception("Falta capturar Nombre");
        }

        if (direccion_cap.equals("")) {
            throw new Exception("Falta capturar Direccion");
        }

        if (descrip.equals("")) {
            throw new Exception("Falta capturar Concepto");
        }


        String fnteing = icuso.inData.getString("fnteing");

        if (fnteing.equals("")) {
            throw new Exception("Falta capturar Fnte de Ingresos");
        }
        
        String monto = icuso.inData.getString("monto");
        monto = monto.replace("$", "").replace(",", "");

        if (monto.equals("")) {
            throw new Exception("Falta capturar Monto");
        }

        String cnta = "81100";
        String regis = icuso.inData.getString("regis");

        eje = " update tfondos set concepto=? where fndo=? ";
        nregis = ejePs(eje, new String[] { descrip, "C", fndo, "N" });

        if (regis.equals("")) {
            qry("select nextval('pk_dregis') conse");
            regis = icuso.dbx.JSTblgetvCampo("conse").toString();
        }
       

        /* Capturamos en la tabla tfondos */

        eje= ""+
                " select "+
                " fndo, fnte, lfnte ,anio ,lfndo, cri, lcri lcri, c.cnta , clabe , case when c.ctax is null then '11225' else c.ctax end ctax"+
                " , a.rfc , case when b.nombre is null then '' else b.nombre end  ,  case when b.direccion is null then '' else  b.direccion end"+
            " from vfondos a "+
            " left join tbenefi b on b.rfc =a.rfc "+
            "  left join tcogcri c on c.cogcri  =a.cri and c.cog is false  "+
            " where 1=1 and  fndo='"+fndo+"'";
    
        

        qry(eje);
        // String kcri= icuso.dbx.JSTblgetvCampo("cri").toString();
        String cnta_cri= icuso.dbx.JSTblgetvCampo("cnta").toString();
        String ctax= icuso.dbx.JSTblgetvCampo("ctax").toString();
        String fnte= icuso.dbx.JSTblgetvCampo("fnte").toString();
        String rfc= icuso.dbx.JSTblgetvCampo("rfc").toString();
        String nombre= icuso.dbx.JSTblgetvCampo("nombre").toString();
        String direccion= icuso.dbx.JSTblgetvCampo("direccion").toString();
       

        rfc_cap= rfc_cap.toUpperCase();
        nombre_cap= nombre_cap.toUpperCase();
        direccion_cap= direccion_cap.toUpperCase();

        

        //actualizamos
        /* 
        if(!rfc_cap.equals(rfc)){
            eje = " update tfondos set rfc=UPPER(?) where fndo=? ";
            nregis = ejePs(eje, new String[] { rfc_cap, "C", fndo, "N" });

            eje= ""+
                " select count(*) con from tbenefi where rfc='"+rfc_cap+"'";
            qry(eje);

            String validar= icuso.dbx.JSTblgetvCampo("con").toString();

            if(validar.equals("0")){
                 qry("select nextval('sbenef') conse");
               String id_rfc = icuso.dbx.JSTblgetvCampo("conse").toString();
                eje = " insert into tbenefi (usua, rfc, nombre, rol,  direccion  ) values (?, UPPER(?) , UPPER(?), 1, UPPER(?) ) ";
                nregis = ejePs(eje, new String[] { id_rfc, "N", rfc_cap, "C", nombre_cap, "C", direccion_cap, "C" });
            }

           rfc= rfc_cap;
           nombre=nombre_cap;
           direccion=direccion_cap;
        }*/

       // descrip = descrip.replaceAll("\"", "&quot;");

        String datosc=connectApiRefer(rfc_cap,nombre_cap, direccion_cap, descrip, fecha,  fnteing, monto);
        
        descrip = descrip.replace("\\\"", "\"");
       
       
        if(!datosc.contains("referencia")){
            throw new Exception(datosc);
        }
        JSONObject o = new JSONObject(datosc);
        String referencia= o.get("referencia").toString();
        String folio= o.get("folio").toString();
        String Caja= o.get("caja").toString();
        String usuario=icuso.usua;

        //String referencia="referencia";

       
        if (kcri.equals("-10000")) {
			throw new Exception("La Fnteing "+fnteing+" no tiene asignado un CRI");
		}

       /*  if (cnta_cri.equals("-10000")) {
			throw new Exception("El CRI "+kcri+" no tiene asignado una cuenta");
		}*/

        
        String interes=icuso.inData.getString("inte");

        if(interes.equals("Si")){
            interes="1";
        }else{
            interes="0";
        }

        // header
        eje = "insert into tregis (regis ,descrip ,fecha ,monto ,teven, refer, benef, nrefer, xrefer, clabe, resp, num ) values(?,?,'"+fecha+ "',?,13 ,?, ?, ?, ?,? ,?, ?) ";
        nregis = ejePs(eje, new String[] { regis, "N", descrip, "C", monto, "N", referencia, "C", rfc_cap, "C", Caja, "N", folio, "C", clabe, "C", usuario, "N", interes, "N" });

        eje = ""
        + " insert into tdregis ("
        + "   regis ,dregis ,fndo ,tmov ,cnta  ,kcog , monto, auxi, crefer, nrefer "
        + " ) values ( "
        + "   ? ,? ,? ,1 ,'-1', ?, ?, ?, ?, ?)  ";//cuando es cuenta -1 poner en el auxi la clabe
        nregis = ejePs(eje, new String[] {
                regis, "N",
                regis, "N",
                fndo, "N",
                kcri ,"C",
                //clas, "N",
                monto, "N",
                clabe, "C",  //auxi
                referencia, "N", //crefer
                fnteing, "N" //nrefer
                 
        });

        /**+*********************************************************************************************************************************/
        /**+**********************************************   Generamos devengado  ***********************************************************/
        /**+*********************************************************************************************************************************/

                    eje = ""
                    + " insert into tdregis ("
                    + "   regis ,fndo ,tmov ,cnta ,cntap ,kcog , monto, auxi,  crefer  "
                    + " ) values ( "
                    + "   ? ,? ,-1 , ?, '812', ?, ?, ?, ?)  ";// cuando es cuenta 4 la del cri poner fuente de ingresos en auxi
            
                    nregis = ejePs(eje, new String[] {
                            regis, "N",
                            fndo, "N",
                            cnta_cri, "C",
                            kcri ,"C",
                            //clas, "N",
                            monto, "N",
                            fnteing, "C",
                            referencia, "N"
                    });
                
        
                    eje = ""
                    + " insert into tdregis ("
                    + "   regis ,fndo ,tmov ,cnta ,cntap ,kcog , monto , tram, auxi, crefer "
                    + " ) values ( "
                    + "   ? ,? ,1 , '"+ctax+"', '814', ?, ?, ?, ?, ?)  ";
            
                    nregis = ejePs(eje, new String[] {
                            regis, "N",
                            fndo, "N",
                            kcri ,"C",
                            monto, "N",
                            regis, "N", 
                            rfc, "C",//guardamos el rfc en el auxi
                            referencia, "N",
                    });


        if(respuesta.equals("Si")){
            icuso.inData.put("fec", fecha);
            icuso.inData.put("comentp", "Recaudacion del documento "+regis);
            icuso.inData.put("regis", regis);
            generarRecaudacion(regis);
        }
        


            icuso.inData.put("regis", regis);
        //icuso.inData.put("dregis", dregis);
        icuso.inData.put("cnta", cnta);
        
        //icuso.inData.put("per", separate[1]);// mes
        //icuso.inData.put("cnta_cri", cnta_cri);
        icuso.inData.put("monto", monto);

        return regis;

    }

    public void generarRecaudacion(String regis_new) throws Exception {
         String regis = icuso.inData.getString("regis");
         

            String fecha = icuso.inData.getString("fec");
            String coment = icuso.inData.getString("comentp");

            if (fecha.equals("")) {
                throw new Exception("Falta capturar Fecha");
            }

            if (coment.equals("")) {
                throw new Exception("Falta capturar comentario");
            }

            qry("select  a.fndo, a.monto, a.kcog , a.auxi, a.nrefer, a.crefer ,  a.auxi from tdregis a where  dregis="+regis);
            String fndo = icuso.dbx.JSTblgetvCampo("fndo").toString();
            String monto = icuso.dbx.JSTblgetvCampo("monto").toString();
            String kcri = icuso.dbx.JSTblgetvCampo("kcog").toString();
            String fnteing = icuso.dbx.JSTblgetvCampo("nrefer").toString();
            String clabe = icuso.dbx.JSTblgetvCampo("auxi").toString();

            qry("select refer, coalesce(xrefer,'') xrefer, benef , nrefer, num from tregis where regis="+regis);
            String referencia = icuso.dbx.JSTblgetvCampo("refer").toString();
            String folio = icuso.dbx.JSTblgetvCampo("xrefer").toString();
            String rfc = icuso.dbx.JSTblgetvCampo("benef").toString();

            String interes = icuso.dbx.JSTblgetvCampo("num").toString();

            String cnta_cri;
            String ctax;
            if(interes.equals("1")){//si tiene interes
                qry("SELECT t2.cogcri cri, CASE WHEN t2.cnta is null then '-1' else t2.cnta end cnta, CASE WHEN t2.ctax IS NULL THEN '-1' ELSE t2.ctax END ctax   FROM tfnteing t\n" + //
                                        "left join tcogcri t2 on t2.cogcri =t.cri and t2.cog is false \n" + //
                                        "where fnteing ='"+fnteing+"'");
                cnta_cri = icuso.dbx.JSTblgetvCampo("cnta").toString();
                ctax = icuso.dbx.JSTblgetvCampo("ctax").toString();
                String cri = icuso.dbx.JSTblgetvCampo("cri").toString();

                if(cnta_cri.equals("-1")){
                    throw new Exception("La fnteing tiene asignado el cri: "+ cri+" pero no tiene asignada una cuenta");
                }
                if(ctax.equals("-1")){
                    throw new Exception("La fnteing tiene asignado el cri: "+ cri+" pero no tiene asignada una cuenta por cobrar");
                }

            }else{
                eje= ""+
                    " select "+
                    " fndo, fnte, lfnte ,anio ,lfndo, cri, lcri lcri, c.cnta , clabe, case when c.ctax is null then '11225' else c.ctax end ctax "+
                    " , a.rfc , case when b.nombre is null then '' else b.nombre end  ,  case when b.direccion is null then '' else  b.direccion end"+
                    " from vfondos a "+
                    " left join tbenefi b on b.rfc =a.rfc "+
                    "  left join tcogcri c on c.cogcri  =a.cri and c.cog is false  "+
                    " where 1=1 and  fndo='"+fndo+"'";

                    qry(eje);
                    cnta_cri= icuso.dbx.JSTblgetvCampo("cnta").toString();
                    ctax= icuso.dbx.JSTblgetvCampo("ctax").toString();


            }
             // generamos un regis nuevo si viene vacio
            if (regis_new.equals("")){
                qry("select nextval('pk_dregis') conse");
                regis_new = icuso.dbx.JSTblgetvCampo("conse").toString();

                 // header
                 eje = "insert into tregis (regis ,descrip ,fecha ,monto ,teven, refer, benef, nrefer, xrefer, clabe ) values( ?, ?, '"+fecha+ "', ?, 14 ,? ,?, ?, ? ,?) ";
                 nregis = ejePs(eje, new String[] { regis_new, "N", coment, "C", monto, "N", referencia, "C", rfc, "C", fnteing, "N", folio, "C", clabe, "C"  });
             
            }

            //recaudado
                eje = ""
                + " insert into tdregis ("
                + "   regis  ,fndo ,tmov ,cnta ,cntap ,kcog , monto, tram, auxi, crefer "
                + " ) values ( "
                + "   ?  ,? ,-1 ,'"+ctax+"', '814', ?, ?, ?, ? ,? )  ";
                nregis = ejePs(eje, new String[] {
                        regis_new, "N",
                        fndo, "N",
                        kcri ,"C",
                        monto, "N",
                        regis, "N",
                        rfc, "C", //guardamos el rfc en el auxi
                        referencia, "N"
                });
        
                eje = ""
                + " insert into tdregis ("
                + "   regis ,fndo ,tmov ,cnta ,cntap ,kcog , monto , tram, auxi, crefer "
                + " ) values ( "
                + "   ? ,? ,1 , '11121', '815', ?, ?, ?, ?, ?)  ";
        
                nregis = ejePs(eje, new String[] {
                        regis_new, "N",
                        fndo, "N",
                        kcri ,"C",
                        monto, "N",
                        regis, "N", 
                        clabe, "C",
                        referencia, "N",
                });
            
    }

    public void borrarRecaudacion() throws Exception {

         String regis = icuso.inData.getString("regis");
         String reca = icuso.inData.getString("reca");

            eje = "delete from tdregis where regis=?";
            nregis = ejePs(eje, new String[] { reca, "N" });
    
            eje = "delete from tregis where regis=?";
            nregis = ejePs(eje, new String[] { reca, "N" });
        
    }

    public void recaudaciones() throws Exception {

        String regis = icuso.inData.getString("regis");
        String reca = icuso.inData.getString("reca");

        if (regis.equals(reca)){
            throw new Exception("Ya esta generada la recaudacion");
        }

        // si esta vacio es porque no exite y hay que insertarlo
        if(reca.equals("")){
            
            generarRecaudacion("");

        }else{//si existe y tenemos que borrarlo

            borrarRecaudacion();
        }

        eje = "select * from (" + icuso.ponInData(prop.getProperty("lis")) + ") lisQ where regis =" + regis;
        jsResult = ejeQry(eje, new String[] {});
            
        icuso.outData.put("dml", 2);
        
        
    }

    public void borrar() throws Exception {
        String regis = icuso.inData.getString("regis");
        String reca = icuso.inData.getString("reca");

        if(!regis.equals(reca)){

            if(!reca.equals("")){
                throw new Exception("No se puede editar/eliminar el docu devengado ya que existe un documento recaudado ligado");
            }

        }
      



        eje = "delete from tdregis where regis=?";
        nregis = ejePs(eje, new String[] { regis, "N" });

        eje = "delete from tregis where regis=?";
        nregis = ejePs(eje, new String[] { regis, "N" });

        eje = "delete from Tfondos where fndo=?";
        nregis = ejePs(eje, new String[] { regis, "N" });

        eje = "delete from tarchivos where archi=?";
        nregis = ejePs(eje, new String[] { regis, "N" });

    }

    public void repo() throws Exception {

        String regis = icuso.inData.getString("regis");
        
		

		//contable
        eje = ""+
                 "select "+
				"dregis, "+
				"  cnta, auxi , lax  "+
				"  ,case when saldo >0 then saldo else 0 end cargo "+
				"  ,case when saldo <0 then saldo*-1 else 0 end abono "+
				" from ( "+
				" select  "+
				" dregis, "+
				"  cnta ,auxi ,ldregis lax "+
				" ,(monto * tmov) saldo "+
				" from tdregis d "+
				" where regis= "+regis +
				"  and signo !=0 "+
				"  and cnta not like '83%' "+
				" ) g where cnta != '99' "+
				" order by sign(saldo*-1),cnta "
                + "";
        // tabla 2 hoja 3
        jsResult = ejeQry(eje);

	


		qry(" SELECT to_char(current_timestamp, 'dd/mm/yyyy HH12:MI:SS') actual");
        String actual = icuso.dbx.JSTblgetvCampo("actual").toString();

	
        qry("select a.regis, "+
        " coalesce(a.descrip,'') descrip , "+
        " to_char(a.fecha,'DD/MM/YYYY') fecha, "+
        " to_char(a.fecha,'YYYY') periodo, "+
        " coalesce(a.refer,'')  referencia,  "+
        " coalesce(case when a.num =1 then (select bn.rfc  from tcbancos bn where bn.bnco=substring(v.clabe,0,4))  else v.rfc end,'') rfc, "+
        " coalesce(case when a.num =1 then (select bn.nbnco  from tcbancos bn where bn.bnco=substring(v.clabe,0,4))  else t.nombre  end,'') nom, "+
        " coalesce(case when a.num =1 then (select bn.direccion  from tcbancos bn where bn.bnco=substring(v.clabe,0,4))  else t.direccion  end ,'') direccion, "+
        " concat(b.kcog,'-',a.nrefer) cri, "+
        " coalesce(cast(b.nrefer as varchar),'') fnteing, "+
        " c.lfnteing, "+
        " b.monto, "+
        " coalesce(a.resp,0) cajero, "+
        " coalesce(a.xrefer,'') folio, "+
        " coalesce(a.nrefer,0) caja, "+
        " coalesce(a.clabe,'') clabe, "+
        " coalesce(v.lbnco,'') banco, "+
        " coalesce(substring(a.clabe,7,11),'') cuenta "+
        " from tregis a "+
        " left join tdregis b on  a.regis=b.regis and b.cnta like '-1%' "+
        " left join tbenefi t on   a.benef=t.rfc "+
        " left join vfondos v on   v.fndo=b.fndo "+
        " left join tfnteing  c on  cast(b.nrefer as varchar)=c.fnteing   "+
        " where  a.regis="+regis);
        String descrip = icuso.dbx.JSTblgetvCampo("descrip").toString();
        String fecha = icuso.dbx.JSTblgetvCampo("fecha").toString();
        String periodo = icuso.dbx.JSTblgetvCampo("periodo").toString();
        String referencia = icuso.dbx.JSTblgetvCampo("referencia").toString();
        String rfc = icuso.dbx.JSTblgetvCampo("rfc").toString();
        String nom = icuso.dbx.JSTblgetvCampo("nom").toString();
        String direccion = icuso.dbx.JSTblgetvCampo("direccion").toString();
        String cri = icuso.dbx.JSTblgetvCampo("cri").toString();
        String fnteing = icuso.dbx.JSTblgetvCampo("fnteing").toString();
        String lfnteing = icuso.dbx.JSTblgetvCampo("lfnteing").toString();
        String monto = icuso.dbx.JSTblgetvCampo("monto").toString();
        String banco = icuso.dbx.JSTblgetvCampo("banco").toString();
        String clabe = icuso.dbx.JSTblgetvCampo("clabe").toString();
        String cuenta = icuso.dbx.JSTblgetvCampo("cuenta").toString();

        String cajero=icuso.dbx.JSTblgetvCampo("cajero").toString();
        String folio=icuso.dbx.JSTblgetvCampo("folio").toString();
        String caja=icuso.dbx.JSTblgetvCampo("caja").toString();

        jsResult.put("nombre", "Reporte");
		jsResult.put("actual", actual);
        jsResult.put("monto", monto);
		jsResult.put("regis", regis);

        jsResult.put("descrip", descrip);
        jsResult.put("fecha", fecha);
        jsResult.put("periodo", periodo);
        jsResult.put("referencia", referencia);
        jsResult.put("rfc", rfc);
        jsResult.put("nom", nom);
        jsResult.put("direccion", direccion);
        jsResult.put("cri", cri);
        jsResult.put("fnteing", fnteing);
        jsResult.put("lfnteing", lfnteing);
        jsResult.put("cantidad", "1");
        jsResult.put("cajero", cajero);
        jsResult.put("folio", folio);
        jsResult.put("caja", caja);
        jsResult.put("banco", banco);
        jsResult.put("clabe", clabe);
        jsResult.put("cuenta", cuenta);
        
        icuso.outData.put("tabla", jsResult);

    }


    public static String  connectApiRefer(String RFC, String nombre, String direccion, String concepto, String fecha, String fnteing, String monto) throws IOException {
        String message="";
        URL url = new URL("https://esefina.ingresos-guerrero.gob.mx/recaudacion/siiagub/servicio.php");
        String token="Bearer<eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9leGFtcGxlLm9yZyIsImF1ZCI6Imh0dHA6XC9cL2V4YW1wbGUuY29tIiwiaWF0IjoxMzU2OTk5NTI0LCJuYmYiOjEzNTcwMDAwMDAsImV4cCI6IjE3MTIwNzA4NDUiLCJkYXRhIjp7Im5hbWUiOiJFTlJJUVVFIFJBTU9TIExVR08iLCJlbWFpbCI6ImVyYWx1Z283NzdAZ21haWwuY29tIn19.TeHEyXoGkDQIiEtqq3OMtJVSpzXvQQvHzPEwtslwCaI>";
        // json
        String jsonString = "{\n" + //
                "    \"peticion\":\"CreaReferencia\",\n" + //
                "    \"rfc\": \""+RFC+"\",\n" + //
                "    \"nombre\": \""+nombre+"\",\n" + //
                "    \"direccion\": \""+direccion+"\",\n" + //
                "    \"concepto\": \""+concepto+"\",\n" + //
                "    \"fecha\": \""+fecha+"\",\n" + //
                "    \"detalles\": [\n" + //
                "        {\n" + //
                "            \"CveFteIng\": \""+fnteing+"\",\n" + //
                "            \"CanFteIng\": \"1\",\n" + //
                "            \"ImpIniRec\": \""+monto+"\",\n" + //
                "            \"ReciboDetImpAntesDev\": \""+monto+"\"\n" + //
                "        }\n" + //
                "    ]\n" + //
                "}";


        HttpURLConnection conection = (HttpURLConnection) url.openConnection();
        conection.setRequestMethod("POST");
        conection.setRequestProperty("Content-Type", "application/json");
        conection.setRequestProperty("Accept", "application/json");
        conection.setRequestProperty("Authorization",token );
        conection.setDoOutput(true);
        

        try (OutputStream os = conection.getOutputStream()) {
            byte[] input = jsonString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        // Print Stataus code
        System.out.println("Status Code: "+conection.getResponseCode());
        if (conection.getResponseCode() == 200) {

                try (BufferedReader br = new BufferedReader(new InputStreamReader(conection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder resp = new StringBuilder();
                    String respLine = null;
                    while ((respLine = br.readLine()) != null) {
                        resp.append(respLine.trim());
                    }
                    message=resp.toString();
                    JSONObject o = new JSONObject(message);
                    String refer= o.get("referencia").toString();
                   message=o.toString();
                    //message=message.replace("{\"referencia\":\"", "").replaceAll("\"", "").replaceAll("}", "");
                   // System.out.println(resp.toString());
                }

        }else{
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conection.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder resp = new StringBuilder();
                    String respLine = null;
                    while ((respLine = br.readLine()) != null) {
                        resp.append(respLine.trim());
                    }
                    message=resp.toString();
                    //message=message.replace("{\"referencia\":\"", "").replaceAll("\"", "").replaceAll("}", "");
                   // System.out.println(resp.toString());
                }

                
        }

        return message;

    }

}