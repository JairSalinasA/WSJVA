package cusos.apiesefina;


import srv.cuso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.Locale;
import java.io.OutputStream;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

import comun.bkComun;

public class compa extends bkComun {
	public compa(cuso inCuso) throws Exception {
		super(inCuso);
		kComa = "'";
	}

	public void lis() throws Exception {

        JSONArray jer = new JSONArray();
        jer.put(new Object[] { ""
                + "<i>Fecha: </i>"
                + icuso.inData.get("fecha")
        });

        icuso.outData.put("jerq", jer);
        jsVals.put("acum_fecha", icuso.inData.get("acum_fecha"));
        jsVals.put("fecha", icuso.inData.get("fecha"));

        String fecha=icuso.inData.get("acum_fecha").toString();

        String json_resul= connectApiEsefinaFecha(fecha);
        getJson(json_resul, fecha);
        ejePs( "commit");

		super.lis();
	}

	public void cap() throws Exception {
        registro();
		
	}

	public void graba() throws Exception {
        
		
	}

    public void registro() throws Exception {

        String cuso = icuso.inData.getString("cuso");

        String fecha = icuso.inData.getString("fecha");
        String acum_fecha = icuso.inData.getString("acum_fecha");




        if(cuso.equals("apiesefina.compa.vali")){//validar
            /*
            qry("select count(*) conta from tacumesefina where  acum_fecha='"+fecha+"' and status !=0 " );
               String vali = icuso.dbx.JSTblgetvCampo("conta").toString();

               if(!vali.equals("0")){
                    throw new Exception("La fecha: "+acum_fecha+" ya se encuentra validada ");
               }
            */

            /* 
            String fechacast[]=fecha.split("/");
            String fec=fechacast[2]+"-"+fechacast[1]+"-"+fechacast[0];
            */

             qry("select count(*) conta from tacumesefina where  acum_fecha='"+fecha+"' and (acum_monto-monto_api) !=0 " );
             String validacion = icuso.dbx.JSTblgetvCampo("conta").toString();

        
            if(!validacion.equals("0")){
                throw new Exception("No se puede validar porque hay montos con diferencias");
            }

            ejePs( "call p_update_folio('"+acum_fecha+"')");//formato yyyy-mm-dd
            ejePs( "commit");
            throw new Exception("Se validaron los registro con fecha: "+fecha);

        }
        if(cuso.equals("apiesefina.compa.desvali")){//quitar validacion
               /* 
               qry("select count(*) conta from tacumesefina where  acum_fecha='"+fecha+"' and status !=0 " );
               String vali = icuso.dbx.JSTblgetvCampo("conta").toString();

               
               if(vali.equals("0")){
                    throw new Exception("La fecha: "+fecha+" no se encuentra validada ");
               }*/

             // header
            ejePs("update tacumesefina set status=0 where acum_fecha ='"+fecha+"'");
            ejePs( "commit");
            throw new Exception("Se desvalidaron los registros con fecha: "+fecha+"");
        }


    }

	public void fin() throws Exception {
		super.fin();
	}

        
    public static String  connectApiEsefinaFecha (String fecha)  throws Exception {
        fecha=fecha.replace("-", "/");
       String message="";
        URL url = new URL("https://esefina.ingresos-guerrero.gob.mx/recaudacion/siiagub/servicio.php");
        String token="Bearer<eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9leGFtcGxlLm9yZyIsImF1ZCI6Imh0dHA6XC9cL2V4YW1wbGUuY29tIiwiaWF0IjoxMzU2OTk5NTI0LCJuYmYiOjEzNTcwMDAwMDAsImV4cCI6IjE3MTIwNzA4NDUiLCJkYXRhIjp7Im5hbWUiOiJFTlJJUVVFIFJBTU9TIExVR08iLCJlbWFpbCI6ImVyYWx1Z283NzdAZ21haWwuY29tIn19.TeHEyXoGkDQIiEtqq3OMtJVSpzXvQQvHzPEwtslwCaI>";
        // json
        String jsonString = "{\n" + //
                "    \"peticion\": \"ImporteSefina\",\n" + //
                "    \"fechaInicial\":\""+fecha+"\",\n" + //
                "    \"fechaFinal\":\""+fecha+"\"\n" + //
                "    \n" + //
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
        
        System.out.println("");
        System.out.println("");
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
                   
                }

        }else{
                 message="Status Code: "+conection.getResponseCode();
                 throw new Exception(""+message);
        }

         return message;


    }

    public  void getJson(String json, String fecha ) throws Exception  {
        JSONObject data = new JSONObject(json);
        JSONArray array = data.getJSONArray("data");

        System.out.println("");
        System.out.println("");

        System.out.println("Recibiendo json:");

        System.out.println("");
        System.out.println("");
        boolean bandera=true;
        String mensaje="";
        NumberFormat formatoImporte = NumberFormat.getInstance(Locale.ENGLISH);
            for(int i=0;i<array.length();i++){
                    JSONObject jsonApi = array.getJSONObject(i);
                    String idBanco=  String.valueOf(jsonApi.getInt("idBanco"));
                    String Banco=  jsonApi.getString("Banco").toString();
                    String Importe_Bancos=  jsonApi.getString("Importe_Bancos").toString();
                    String Importe_Ingresos=  jsonApi.getString("Importe_Ingresos").toString();
                    String Diferencia=  jsonApi.getString("Diferencia").toString();

                    System.out.println("idBanco: "+idBanco+
                                    " Banco: "+Banco+
                                    " Importe_Bancos: "+Importe_Bancos+
                                    " Importe_Ingresos: "+Importe_Ingresos+
                                    " Diferencia: "+Diferencia
                                    );
                 qry("select count(*) conta from tacumesefina where acum_bancoid="+idBanco+ " and acum_fecha='"+fecha+"'");
                 String validacion = icuso.dbx.JSTblgetvCampo("conta").toString();
                Double dif=0.00;
                 if(!validacion.equals("0")){
                        qry("select acum_bancoid, acum_fecha, acum_clabe, acum_monto, status from tacumesefina where acum_bancoid="+idBanco+ " and acum_fecha ='"+fecha+"'");
                        String status = icuso.dbx.JSTblgetvCampo("status").toString();
                        String monto  = icuso.dbx.JSTblgetvCampo("acum_monto").toString();
                        String bancoid  = icuso.dbx.JSTblgetvCampo("acum_bancoid").toString();

                         dif= Double.parseDouble(monto) - Double.parseDouble(Importe_Bancos);
                                                    
                                        eje = ""
                                        + "  update tacumesefina set monto_api=? where  acum_fecha='"+fecha+"' and acum_bancoid=? ";//cuando es cuenta 11225 poner en el auxi la clabe
                                        nregis = ejePs(eje, new String[] {
                                                Importe_Bancos, "N",
                                                bancoid, "N"
                                                
                                        });

                     
                 }else{
                     String clabe="";
                     qry("    select count(*)  conta from tbancoid t where id= "+idBanco);
                    String conta = icuso.dbx.JSTblgetvCampo("conta").toString();

                    if(!conta.equals("0")){
                         qry("    select clabe from tbancoid t where id= "+idBanco);
                     clabe = icuso.dbx.JSTblgetvCampo("clabe").toString();
                    }

                   
                    
                              eje = ""
                                        + "  insert into tacumesefina (acum_bancoid, acum_fecha, acum_clabe, acum_monto, monto_api) values (?, '"+fecha+"',?, 0,?) ";//cuando es cuenta 11225 poner en el auxi la clabe
                                        nregis = ejePs(eje, new String[] {
                                                idBanco, "N",
                                                clabe, "C",
                                                Importe_Bancos, "N"
                                        });
                 }

            }
           


            System.out.println("");
            System.out.println("");
            System.out.println("Termino");
           
    }

}
