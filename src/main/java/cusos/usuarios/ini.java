/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cusos.usuarios;

/**
 *
 * @author christianmendoza
 */

import srv.cuso;
import org.json.JSONObject;
import comun.bkComun;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;


public class ini extends bkComun {
    public ini(cuso inCuso) throws Exception {
		super(inCuso);
		kComa="'";
	}
    
       

	public void lis() throws Exception {
            
            jsVals.put("rol", icuso.inData.get("rol"));

            JSONArray jer = new JSONArray();
            jer.put(new Object[] { icuso.inData.get("rol"), icuso.inData.get("nombre_rol") });
            icuso.outData.put("jerq", jer);
            jsVals.put("rol", icuso.inData.get("rol"));

		/* lisFil = new JSONObject();
		 lisFil.put("Filtros", new Object[] {"nombre","email"}); // Listado y en Orden
		 lisFil.put("nFiltros", new Object[] { "Nombre","Email"}); // Nombres de Filtros
		 lisFil.put("sFiltros", new Object[] { "7","10"}); // Tamaño de Filtros
            
                 */
		super.lis();
		
	}
        
        public void cap() throws Exception {
            
             jsVals.put("rol", icuso.inData.get("rol"));
             
           
            
            /*String rol = icuso.inData.getString("rol");
            
            icuso.prop.setProperty("lis", ""
		+ " select  rfc  ,nombre, e.rol, e.lrol lrol, e.prol tipo , ee.descrip ltipo \n" +
                "from tbenefi r \n" +
                "left join croles0 e on r.rol = e.rol \n" +
                "left join croles0 ee on e.prol =ee.rol \n" +
                "where 1=1 "
		+ "");
            */
                
		super.cap(); 
	}

	public void graba() throws Exception {
        
                String dml = icuso.inData.getString("dml");
              
          if (dml.equals("1")) {//inserta
              String secuencia="susuarios";
              
              ejeQry("SELECT cast( nextval('" + secuencia + "') as text) \"kVal\" ");
              String  usua = valStrFld(0);
              //jsVals.put("usua", usua); 
              icuso.inData.put("usua", usua);
              
              String nombre = icuso.inData.getString("nombre");
              String email = icuso.inData.getString("email");

               //guardamos el archivo
              eje = "insert into cusuarios0 (usua, nombre,email) values (?,?,?) ";
              nregis = ejePs(eje, new String[] {usua, "N", nombre,"C",email,"C"});
              
              
          }
          
           if (dml.equals("2")) {//editar
               
               String usua = icuso.inData.getString("usua");
               String nombre = icuso.inData.getString("nombre");
               String email = icuso.inData.getString("email");
                 //guardamos el archivo
              
               
           }
           if (dml.equals("3")) {//eliminar
              String usua = icuso.inData.getString("usua");
                 //guardamos el archivo
              eje = "delete from cusuarios0 where usua=? ";
              nregis = ejePs(eje, new String[] {usua, "N"});
           } 
          
            
		super.graba();
                
                 if (dml.equals("1")) {//inserta
                     
                      String usua = icuso.inData.getString("usua");
                      String email = icuso.inData.getString("email");
                      
                        //mandamos llamar API mail
                   String mensaje= connectApiMail(email,usua);
                   
                   if(!mensaje.equals("true")){
                        throw new Exception(mensaje);
                   }
                   
                 }
              
          
	}

	public void fin() throws Exception {
		super.fin();
	}
        
        public  String  connectApiMail(String email, String id) throws UnsupportedEncodingException, IOException{
                String mensaje="";
                String charset = StandardCharsets.UTF_8.name();   
                String url="http://10.1.32.56:8080/sendEmail/Email"; 
                String query = String.format("email="+email+"&id="+id, 
                        URLEncoder.encode(email, charset),
                        URLEncoder.encode(id, charset));

                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Accept-Charset", charset);
                connection.setRequestProperty("User-Agent", "sendEmail");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
                try (OutputStream output = connection.getOutputStream()) {
                    output.write(query.getBytes(charset));
                }

                  InputStream response = connection.getInputStream();
                if (connection.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(response));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    JSONObject json;



                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                        JSONArray jsonArray1 = new JSONArray("["+line+"]");
                        json = jsonArray1.getJSONObject(0);
                        String msg= json.getString("msg");
                        boolean status= json.getBoolean("status");
                        System.out.println(line);

                        if(!status){
                            mensaje=msg;
                        }else{
                            mensaje="true";
                        }
                    }
                }
              

                 return mensaje;
    }

}

