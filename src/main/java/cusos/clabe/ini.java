package cusos.clabe;

import srv.cuso;
import comun.bkComun;
import org.json.JSONArray;
public class ini extends bkComun {
     public ini(cuso inCuso) throws Exception {
          super(inCuso);
          kComa = "'";
     }

     public void lis() throws Exception {
          super.lis();
          jsVals.put("rfc", icuso.inData.get("rfc"));
          jsVals.put("nombre", icuso.inData.get("nombre"));

          JSONArray jer = new JSONArray();
          jer.put(new Object[] { icuso.inData.get("rfc"), icuso.inData.get("nombre") });
          icuso.outData.put("jerq", jer);
          //jsVals.put("rfc", icuso.inData.get("rfc"));
     }

     public void cap() throws Exception {
          jsVals.put("rfc", icuso.inData.get("rfc"));
          
          super.cap();
          

     }

     public void graba() throws Exception {

          String dml = icuso.inData.getString("dml");

          if (dml.equals("1")) {

               String clabe = icuso.inData.getString("clabe");
               

               if (clabe.equals("")) {
                    throw new Exception("Hace falta capturar la Clabe");
               }
               
               String validar_clabe=clabe.substring(0, 3);
               
               qry("select count(*) conta from tcbancos t  where bnco='"+validar_clabe+"'");
		String val =  icuso.dbx.JSTblgetvCampo("conta").toString(); 
               
                 if(val.equals("0")){
                      throw new Exception("La clabe ingresada no es valida ya que no  esta ligada a ningun banco");
                 }
               

               if (clabe.length() != 18) {
                    throw new Exception("La clabe debe contener 18 digitos");
               }


               String fndo = icuso.inData.getString("fndo");

               if (fndo.equals("")) {
                    throw new Exception("Hace falta capturar el Fondo");
               }
               
              /* String coment = icuso.inData.getString("coment");

               if (coment.equals("")) {
                    throw new Exception("Hace falta capturar el comentario");
               }
               */
              

          }

          super.graba();
     }

     public void fin() throws Exception {
          super.fin();
     }

}
