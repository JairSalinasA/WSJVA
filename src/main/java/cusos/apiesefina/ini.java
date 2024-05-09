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

	public void graba() throws Exception {
        /* 
        String dml = icuso.inData.getString("dml");
        String fecha = icuso.inData.getString("fecha");
        if(fecha.equals("")){
            throw new Exception("Es necesario capturar la fecha");
        }

        if(!dml.equals("3")){
                    String json_resul= connectApiEsefinaFecha(fecha);
                    getJson(json_resul, fecha);
        }else{
             // header
            qry("update tacumesefina set status=0 where acum_fecha ='"+fecha+"'");
        }

        if (!dml.equals("3")) {// Si no es borrar regresa nuevo registro
            eje = "select * from (" + icuso.ponInData(prop.getProperty("lis")) + ") lisQ where to_char(acum_fecha,'dd/mm/yyyy') =" + fecha;
            jsResult = ejeQry(eje, new String[] {});
        }
        icuso.outData.put("dml", dml);
        */
		
	}

	public void fin() throws Exception {
		super.fin();
	}

        
    public static String  connectApiEsefinaFecha (String fecha)  throws IOException {
        fecha=fecha.replace("-", "/");
       String message="";
        URL url = new URL("https://esefina.ingresos-guerrero.gob.mx/recaudacion/siiagub/servicio.php");
        String token="Bearer<eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9leGFtcGxlLm9yZyIsImF1ZCI6Imh0dHA6XC9cL2V4YW1wbGUuY29tIiwiaWF0IjoxMzU2OTk5NTI0LCJuYmYiOjEzNTcwMDAwMDAsImV4cCI6IjE3MDQzMDYwODEiLCJkYXRhIjp7Im5hbWUiOiJFTlJJUVVFIFJBTU9TIExVR08iLCJlbWFpbCI6ImVyYWx1Z283NzdAZ21haWwuY29tIn19.7IwxkimhUDPaUN6EOOhl4er3tsOjCqNz6yWR_hcw0oI>";
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

                        if(status.equals("1")){
                            throw new Exception("El registro ya se encuentra validado");
                        }
                    

                        if(!monto.equals(Importe_Bancos)){
                        bandera=false;
                        mensaje=mensaje+"✘ El bancoid: "+bancoid+" no coinciden los montos, monto_esefina: $"+formatoImporte.format(Double.parseDouble(monto)).replace("$", "")+ " montoAPI: $"+formatoImporte.format(Double.parseDouble(Importe_Bancos)).replace("$", "")+ " Dif: $"+formatoImporte.format(dif).replace("$", "")+"        \r\n";
                        }else{
                             mensaje=mensaje+"✔ El bancoid: "+bancoid+" si coinciden los montos, monto_esefina: $"+formatoImporte.format(Double.parseDouble(monto)).replace("$", "")+ " montoAPI: $"+formatoImporte.format(Double.parseDouble(Importe_Bancos)).replace("$", "")+" Dif: $"+formatoImporte.format(dif).replace("$", "")+"      \r\n";
                        }

                     
                 }else{
                    mensaje=mensaje+"✘ El bancoid: "+idBanco+"  no coinciden los montos, monto_esefina: $0.00 montoAPI: $"+formatoImporte.format(Double.parseDouble(Importe_Bancos)).replace("$", "")+" Dif: $"+formatoImporte.format(Double.parseDouble(Importe_Bancos)).replace("$", "")+"      \r\n";
                    bandera=false;
                 }

            }
           

            if(bandera){
                 String fechacast[]=fecha.split("/");
                 String fec=fechacast[2]+"-"+fechacast[1]+"-"+fechacast[0];
                 ejePs( "call p_update_folio("+fec+")");//formato yyyy-mm-dd
            }else{
                mensaje.replace("¤", "");
                throw new Exception(""+mensaje);
            }

            System.out.println("");
            System.out.println("");
            System.out.println("Termino");
    }

}
