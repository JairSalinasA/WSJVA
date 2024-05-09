package cusos.reportes;


import srv.cuso;
//import org.json.JSONArray;
//import org.json.JSONObject;

import comun.bkComun;
import org.json.JSONArray;

public class ini extends bkComun {

     public ini(cuso inCuso) throws Exception {
          super(inCuso);
     }

     public void fin() throws Exception {
        super.fin();
             
   }

     public void ingresos() throws Exception {
            
           //  String anio= icuso.inData.getString("fltejer");
           //  String mes= icuso.inData.getString("fltper");
          
             //JSONObject resul; 
            jsResult= ejeQry("  select  now() ");
          icuso.outData.put("nombre", "Estado de Presupuestal Ingresos");
 
                
          icuso.outData.put("tabla", jsResult);

                
         }

    public void egresos() throws Exception {
            
            //  String anio= icuso.inData.getString("fltejer");
            //  String mes= icuso.inData.getString("fltper");
           
              //JSONObject resul; 
             jsResult= ejeQry("  select  now() ");
           icuso.outData.put("nombre", "Estado de Presupuestal Egresos");
  
                 
           icuso.outData.put("tabla", jsResult);
 
                 
          }

 
      
        

}