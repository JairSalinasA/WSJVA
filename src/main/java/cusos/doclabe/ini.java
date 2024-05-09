package cusos.doclabe;

import srv.cuso;
import comun.bkComun;
import org.json.JSONArray;
import org.json.JSONObject;

public class ini extends bkComun {
     public ini(cuso inCuso) throws Exception {
          super(inCuso);
          kComa = "'";
     }

     public void lis() throws Exception {

          jsVals.put("clabe", icuso.inData.get("clabe"));
          jsVals.put("rfc", icuso.inData.get("rfc"));
          jsVals.put("lrfc", icuso.inData.get("lrfc"));

          JSONArray jer = new JSONArray();
          jer.put(new Object[] { "RFC: " + icuso.inData.get("rfc") });
          jer.put(new Object[] { "Clabe: " + icuso.inData.get("clabe") });
          icuso.outData.put("jerq", jer);

          super.lis();

     }

     public void cap() throws Exception {
          jsVals.put("clabe", icuso.inData.get("clabe"));
          jsVals.put("archi", icuso.inData.get("archi"));
          super.cap();
          
          JSONObject valFld = new JSONObject();
   
            valFld.put("1", "Contrato Apertura de Cuenta");
            valFld.put("2", "Constancia del Banco");
            valFld.put("3", "INE");
            jsValFlds.put("tipo", valFld);
          
     }

     public void graba() throws Exception {

          String dml = icuso.inData.getString("dml");

          if (dml.equals("1")) {// inserta
              
              String tipo = icuso.inData.getString("tipo");

               if (tipo.equals("")) {
                    throw new Exception("Hace falta capturar el tipo");
               }

               //String rfc = icuso.inData.getString("rfc");

               jsVals.put("clabe", icuso.inData.get("clabe"));

               String url = icuso.inData.getString("url");
               String divarchi = url.replace("http://35.212.157.120:3000/archis/", "");
               String split[] = divarchi.split("\\.");
               jsVals.put("archi", split[0]);
               icuso.inData.put("archi", split[0]);

               // guardamos el archivo
               eje = "insert into tarchivos (archi,url, tipo) values (?,?,?) ";
               nregis = ejePs(eje, new String[] { split[0], "N", url, "C" , tipo,"N"});

          }

          if (dml.equals("2")) {// editar

               jsVals.put("clabe", icuso.inData.get("clabe"));

               String url = icuso.inData.getString("url");
               String divarchi = url.replace("http://35.212.157.120:3000/archis/", "");
               String splitarchi[] = divarchi.split("\\.");

               String archi = icuso.inData.getString("archi");
               eje = "delete from tarchivos where archi=? ";
               nregis = ejePs(eje, new String[] { archi, "N" });

               String tipo = icuso.inData.getString("tipo");

               if (tipo.equals("")) {
                    throw new Exception("Hace falta capturar el tipo");
               }
               
               // guardamos el archivo
               eje = "insert into tarchivos (archi,url,tipo) values (?,?,?) ";
               nregis = ejePs(eje, new String[] { splitarchi[0], "N", url, "C", tipo,"N" });

               jsVals.put("archi", splitarchi[0]);
               icuso.inData.put("archi", splitarchi[0]);

          }
          if (dml.equals("3")) {// eliminar

               String archi = icuso.inData.getString("archi");
               eje = "delete from tarchivos where archi=? ";
               nregis = ejePs(eje, new String[] { archi, "N" });
          }

          super.graba();
     }

     public void fin() throws Exception {
          super.fin();
     }

}
