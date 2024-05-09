package cusos.detpoling;



//import org.json.JSONArray;
import org.json.JSONArray;

import comun.bkComun;
import srv.cuso;

public class ini extends bkComun {

	public ini(cuso inCuso) throws Exception {
		super(inCuso);
		kComa="'";
	}

	public void lis() throws Exception {
		super.lis();

        jsVals.put("regis", icuso.inData.get("regis"));
        

        JSONArray jer = new JSONArray();
        jer.put(new Object[] { "regis", icuso.inData.get("regis") });
        icuso.outData.put("jerq", jer);
		
	}

	public void cap() throws Exception {

        jsVals.put("regis", icuso.inData.get("regis"));
       

        if (icuso.inData.get("cuso").equals("detpoling.ini.cap_2")) {//editar

                qry("select clas from tdregis where  dregis="+icuso.inData.get("dregis"));
                String cog_class =  icuso.dbx.JSTblgetvCampo("clas").toString();
                icuso.inData.put("cog_class",cog_class);
                jsVals.put("cog_class", icuso.inData.get("cog_class"));
                jsVals.put("dregis", icuso.inData.get("dregis"));
        }

 

        super.cap();

	}

	public void graba() throws Exception {

		String dml = icuso.inData.getString("dml");

             if (dml.equals("1")) {//inserta
                insertar();

            }

            if (dml.equals("2")) {//editar
                String regis = icuso.inData.getString("regis");
                String dregis = icuso.inData.getString("dregis");

                String cog_class = icuso.inData.getString("cri_clas");

                if(cog_class.equals("")){
                        qry("select clas from tdregis where  dregis="+dregis);
                        cog_class =  icuso.dbx.JSTblgetvCampo("clas").toString();
                        icuso.inData.put("cri_clas",cog_class);
                }

               

                eje = "delete from tdregis where regis=? and item=?  ";
                nregis = ejePs(eje, new String[] { regis, "N",  dregis, "N" });

              

                insertar();

            }
          
          
            
		super.graba();
                
	}

        public void insertar() throws Exception{

                String regis = icuso.inData.getString("regis");
                String monto = icuso.inData.getString("monto");
                monto= monto.replace("$", "").replace(",", "");
                String cog_class = icuso.inData.getString("cri_clas");

                qry("select nextval('pk_dregis') conse");
                String dregis =  icuso.dbx.JSTblgetvCampo("conse").toString();

                qry("select nextval('pk_dregis') conse");
                String item =  icuso.dbx.JSTblgetvCampo("conse").toString();
               
                
         

                qry("select fndo, cnta, ejer, per from tdregis where regis=dregis and regis="+regis);
                String fndo =  icuso.dbx.JSTblgetvCampo("fndo").toString();
                String cnta =  icuso.dbx.JSTblgetvCampo("cnta").toString();
                String ejer =  icuso.dbx.JSTblgetvCampo("ejer").toString();
                String per =  icuso.dbx.JSTblgetvCampo("per").toString();
               

                 // header
                 eje = "insert into tdregis (regis, dregis, cnta, fndo, tmov, monto, teven, clas, item ) values (?, ?, ?, ?, ?, ?, ?, ?, ? )  ";
                 nregis = ejePs(eje, new String[] { 
                        regis, "N", 
                        dregis, "N", 
                        "81200",  "N", 
                        fndo, "N", 
                        "-1", "N", 
                        monto, "N", 
                        "8","N", 
                        cog_class, "N", 
                        item, "N"
                          
                });

                

                eje = "insert into tdregis (regis, dregis, cnta, fndo, tmov, monto, teven, clas, item ) values (?, ?, ?, ?, ?, ?, ?, ?, ? )  ";
                nregis = ejePs(eje, new String[] { 
                       regis, "N", 
                       item, "N", 
                       cnta,  "N", 
                       fndo, "N", 
                       "1", "N", 
                       monto, "N", 
                       "8","N", 
                       cog_class, "N", 
                       item, "N"
                       
               });

               dregis=item;

                 icuso.inData.put("regis", regis);
                 icuso.inData.put("dregis", dregis);
                 icuso.inData.put("cnta", cnta);
                 icuso.inData.put("fndo",fndo);
                 icuso.inData.put("cog_class",cog_class);
                 icuso.inData.put("ejer",ejer);
                 icuso.inData.put("per",per);
                 icuso.inData.put("item",item);

        }

	public void fin() throws Exception {
		super.fin();
	}

}