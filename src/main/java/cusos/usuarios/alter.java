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


public class alter extends bkComun {
    public alter(cuso inCuso) throws Exception {
		super(inCuso);
		kComa="'";
	}
    
       

	public void lis() throws Exception {

        lisFil = new JSONObject();
		 lisFil.put("Filtros", new Object[] {"usua","nombre","email", "rfc"}); // Listado y en Orden
		 lisFil.put("nFiltros", new Object[] { "Usuario","Nombre", "Email", "RFC"}); // Nombres de Filtros
		 lisFil.put("sFiltros", new Object[] { "7","10","10","10"}); // Tamaño de Filtros

		super.lis();
		
	}
        
        public void cap() throws Exception {
                
		super.cap(); 
	}

	public void graba() throws Exception {
        
                String dml = icuso.inData.getString("dml");

             if (dml.equals("1")) {//inserta
                            String usua = icuso.inData.getString("usua");
                            String ine = icuso.inData.getString("ine");

                            if(!ine.equals("")){
                                String divarchi = ine.replace("http://35.212.157.120:3000/archis/", "");
                                String split[] = divarchi.split("\\.");
                                jsVals.put("archi", split[0]);
                                icuso.inData.put("archi", split[0]);
                
                                // guardamos el archivo
                                eje = "insert into tarchivos (archi,url, item, tarchi) values (?,?,0,2) ";
                                nregis = ejePs(eje, new String[] { usua, "N", ine, "C"});
                            }


                            String nombra = icuso.inData.getString("nombra");

                            if(!nombra.equals("")){
                                String divarchi = nombra.replace("http://35.212.157.120:3000/archis/", "");
                                String split[] = divarchi.split("\\.");
                                jsVals.put("archi", split[0]);
                                icuso.inData.put("archi", split[0]);
                
                                // guardamos el archivo
                                eje = "insert into tarchivos (archi,url, item, tarchi) values (?,?,1,2) ";
                                nregis = ejePs(eje, new String[] { usua, "N", nombra, "C"});
                            }

            }

            if (dml.equals("2")) {//editar
                   String usua = icuso.inData.getString("usua");
                    eje = "delete from tarchivos where archi=? and item in(0,1) and tarchi=2";
                    nregis = ejePs(eje, new String[] { usua, "N"});

                    String ine = icuso.inData.getString("ine");

                    if(!ine.equals("")){
    
                        // guardamos el archivo
                        eje = "insert into tarchivos (archi,url, item, tarchi) values (?,?,0,2) ";
                        nregis = ejePs(eje, new String[] { usua, "N", ine, "C"});
                    }


                    String nombra = icuso.inData.getString("nombra");

                    if(!nombra.equals("")){
                        // guardamos el archivo
                        eje = "insert into tarchivos (archi,url, item, tarchi) values (?,?,1,2) ";
                        nregis = ejePs(eje, new String[] { usua, "N", nombra, "C"});
                    }
            }
       
           if (dml.equals("3")) {//eliminar
              String usua = icuso.inData.getString("usua");
                 //guardamos el archivo
              eje = "delete from cusurol0 where usua=? ";
              nregis = ejePs(eje, new String[] {usua, "N"});

              eje = "delete from tarchivos where archi=? and item in(0,1) and tarchi=2";
              nregis = ejePs(eje, new String[] { usua, "N"});

           } 
          
            
		super.graba();
                
                 if (dml.equals("1")) {//inserta
                     
                      String usua = icuso.inData.getString("usua");
                      String email = icuso.inData.getString("email");
                      
                        //mandamos llamar API mail
                        String mensaje="";
                        try{
                             mensaje= connectApiMail(email,usua);
                        }catch(Exception e){
                            throw new Exception("No se pudo enviar el correo");
                        }
                        
                    
                        if(!mensaje.equals("true")){
                                throw new Exception("No se pudo enviar el correo");
                        }

                 }

                 if (dml.equals("2")) {//editar
                     
                    String usua = icuso.inData.getString("usua");
                    String email = icuso.inData.getString("email");

                    //validamos que si el correo no se cambio o actualizo no se envie el email
                    qry("select count(*) validar from cusuarios0 c where email='"+email+"'");
		              String validar =  icuso.dbx.JSTblgetvCampo("validar").toString(); 

                      if(validar.equals("0")){
                            //mandamos llamar API mail
                            String mensaje="";
                            try{
                                 mensaje= connectApiMail(email,usua);
                            }catch(Exception e){
                                throw new Exception("No se pudo enviar el correo");
                            }
                            
                        
                            if(!mensaje.equals("true")){
                                    throw new Exception("No se pudo enviar el correo");
                            }
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
                            if(line.contains("true")){
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
                            }else{
                                mensaje="No se pudo enviar el correo";
                            }

                            
                        }
                    
                    
                }
              

                 return mensaje;
    }
    public void setMail() throws Exception {
		

        String usua = icuso.inData.getString("usua");
        String email = icuso.inData.getString("email");


        //mandamos llamar API mail
        String mensaje= connectApiMail(email,usua);

        if(!mensaje.equals("true")){
                throw new Exception(mensaje);
        }

	}

}

