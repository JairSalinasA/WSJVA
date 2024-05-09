package cusos.contab;

import srv.cuso;
//import org.json.JSONArray;
//import org.json.JSONObject;


import comun.bkComun;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import org.json.JSONArray;
import org.json.JSONObject;

public class ini extends bkComun {

     public ini(cuso inCuso) throws Exception {
          super(inCuso);
     }

     public void fin() throws Exception {
          super.fin();
                
               
     }

/* Nota: 
                                                             Procesos actualizacion
                                                Estados Financieros = call p_acumregiscontab(anio, mes);
                                                Estados Presupuestales= call p_tacumdregiscog(anio, mes);

*/ 

     //Estado Analitico de Ingresos
    public void anaingre() throws Exception {
          
             String anio= icuso.inData.getString("fltejer");
             String mes= icuso.inData.getString("fltper");

             anio="2024";
             mes="12";
             
             
            ejePs( "call p_anaingre("+anio+","+mes+")");
               
          
             //JSONObject tabla 1; 
            jsResult= ejeQry("  select\n" +
                                    "   k conac ,lk rubro\n" +
                                    "  ,monto0 *-1 aprobado\n" +
                                    "  ,monto1 * -1 reduccion\n" +
                                    "  ,(monto0+monto1) * -1 modificado\n" +

                                    "  ,monto2 devengado\n" +
                                    "  ,monto3 recaudado\n" +
                                    "  ,(monto3-monto0) * -1 diferencia\n" +
                                    "   ,tabula \n" +
	                                "   ,tip  \n" +
                                    "   ,final_  \n" +
                                    " from wrepos  where tabla=1  order by k");
            //tabla 2
            JSONObject resul = ejeQry("  select\n" +
                                    "   fnte, k conac ,lk rubro\n" +
                                    "  ,monto0 *-1 aprobado\n" +
                                    "  ,monto1 * -1 reduccion\n" +
                                    "  ,(monto0+monto1) * -1 modificado\n" +
                                    "  ,monto2 devengado\n" +
                                    "  ,monto3 recaudado\n" +
                                    "  ,(monto3-monto0) * -1 diferencia\n" +
                                    "   ,tabula \n" +
	                                "   ,tip  \n" +
                                    "   ,final_  \n" +
                                    " from wrepos  where tabla=2  order by fnte, k");
        
          icuso.outData.put("nombre", "Estado Analitico de Ingresos");
              
               
               
               jsResult.put("etiqueta", getEtiqueta(anio, mes));
               jsResult.put("getDateHoursRightNow", getDateHoursRightNow());
               jsResult.put("names", getNames());
               jsResult.put("tabla2", resul);
                
          icuso.outData.put("tabla", jsResult);
           
     }
     //Estado Analitico de Egresos (por objeto del gasto)
    public void anaegre() throws Exception {
          
             String anio= icuso.inData.getString("fltejer");
             String mes= icuso.inData.getString("fltper");

             anio="2024";
             mes="12";
             
            ejePs( "call p_anaegre("+anio+","+mes+")");
               
          
             //JSONObject tabla 1; 
            jsResult= ejeQry("  select\n" +
                                    "   k conac ,lk rubro\n" +
                                    "  ,monto0 *-1 aprobado\n" +
                                    "  ,monto1 * -1 reduccion\n" +
                                    "  ,(monto0+monto1) * -1 modificado\n" +
                                    "  ,monto2 devengado\n" +
                                    "  ,monto3 pagado\n" +
                                    "  ,(monto0 + monto1 + monto2) * -1 subejercicio\n" +
                                    "   ,tabula \n" +
	                                "   ,tip  \n" +
                                    "   ,final_  \n" +
                                    " from wrepos    order by k");
        
          icuso.outData.put("nombre", "Estado Analitico de Egresos");
              
               
               jsResult.put("etiqueta2","Clasificación por Objeto del Gasto (Capítulo y Concepto)");
               jsResult.put("etiqueta", getEtiqueta(anio, mes));
               jsResult.put("getDateHoursRightNow", getDateHoursRightNow());
               jsResult.put("names", getNames());
            
          icuso.outData.put("tabla", jsResult);
           
     }

         //Estado Analitico de Egresos Clasificación Económica (por Tipo de Gasto)
    public void anaegre_clasi_econom() throws Exception {
          
            String anio= icuso.inData.getString("fltejer");
            String mes= icuso.inData.getString("fltper");

            anio="2024";
            mes="12";
            
           ejePs( "call p_anaegre_clasi_econom("+anio+","+mes+")");
              
         
            //JSONObject tabla 1; 
           jsResult= ejeQry("  select\n" +
                                   "   k conac ,lk rubro\n" +
                                   "  ,monto0 *-1 aprobado\n" +
                                   "  ,monto1 * -1 reduccion\n" +
                                   "  ,(monto0+monto1) * -1 modificado\n" +
                                   "  ,monto2 devengado\n" +
                                   "  ,monto3 pagado\n" +
                                   "  ,(monto0 + monto1 + monto2) * -1 subejercicio\n" +
                                   "   ,tabula \n" +
                                   "   ,tip  \n" +
                                   "   ,final_  \n" +
                                   " from wrepos    order by k");
       
         icuso.outData.put("nombre", "Estado Analitico de Egresos Clasificación Económica (por Tipo de Gasto)");
             
              
              jsResult.put("etiqueta2","Clasificación Económica (por Tipo de Gasto)");
              jsResult.put("etiqueta", getEtiqueta(anio, mes));
              jsResult.put("getDateHoursRightNow", getDateHoursRightNow());
              jsResult.put("names", getNames());
           
         icuso.outData.put("tabla", jsResult);
          
    }

     //Estado Analitico de Egresos (Clasificación Administrativa)
    public void anaegre_admin() throws Exception {
          
                String anio= icuso.inData.getString("fltejer");
                String mes= icuso.inData.getString("fltper");
    
                anio="2024";
                mes="12";
                
               ejePs( "call p_anaegre_admin("+anio+","+mes+")");
                  
             
                //JSONObject tabla 1; 
               jsResult= ejeQry("  select\n" +
                                       "   k conac ,lk rubro\n" +
                                       "  ,monto0 *-1 aprobado\n" +
                                       "  ,monto1 * -1 reduccion\n" +
                                       "  ,(monto0+monto1) * -1 modificado\n" +
                                       "  ,monto2 devengado\n" +
                                       "  ,monto3 pagado\n" +
                                       "  ,(monto0 + monto1 + monto2) * -1 subejercicio\n" +
                                       "   ,tabula \n" +
                                       "   ,tip  \n" +
                                       "   ,final_  \n" +
                                       " from wrepos    order by k");
           
             icuso.outData.put("nombre", "Estado Analitico de Egresos Clasificación Administrativa");
                 
                  
                  jsResult.put("etiqueta2","Clasificación Administrativa");
                  jsResult.put("etiqueta", getEtiqueta(anio, mes));
                  jsResult.put("getDateHoursRightNow", getDateHoursRightNow());
                  jsResult.put("names", getNames());
               
             icuso.outData.put("tabla", jsResult);
              
        }

     //Estado de Actividades
     public void activ() throws Exception {
          
             String anio= icuso.inData.getString("fltejer");
             String mes= icuso.inData.getString("fltper");

             anio="2024";
             mes="12";
             
             
            ejePs( "call p_activ("+anio+","+mes+")");
               
             //JSONObject resul; 
             jsResult= ejeQry("  select            \n" +
             "                          k, \n" +
             "                          lk, \n" +
             "                          monto0 mnto_act, \n" +
             "                          monto1 mnto_ant ,             \n" +
             "                          tip \n" +
             "                         from wrepos order by k ");
            icuso.outData.put("nombre", "Estado de Actividades");
                
                    
                    
                    jsResult.put("etiqueta", getEtiqueta(anio, mes));
                    jsResult.put("getDateHoursRightNow", getDateHoursRightNow());
                    jsResult.put("names", getNames());
                    
                    
                    
            icuso.outData.put("tabla", jsResult);
           
     }

     //Estado de Situacion Financiera
     public void sitfin() throws Exception {
          
                String anio= icuso.inData.getString("fltejer");
                String mes= icuso.inData.getString("fltper");

                 anio="2024";
                 mes="12";
        
                 ejePs( "call p_sitfin("+anio+","+mes+")");
                
                //JSONObject resul; 
                jsResult= ejeQry("select tip, k, lk, monto0, monto1 from wrepos  where ((k like '2%') or k like '3%')  order by k ");
                jsResult.put("datos2", ejeQry(" select tip, k, lk, monto0, monto1 from wrepos  where k like '1%'  order by k ")) ;
                    
                icuso.outData.put("nombre", "Estado de Situacion Financiera");
                
                
                
                jsResult.put("etiqueta", getEtiqueta(anio, mes));
                jsResult.put("getDateHoursRightNow", getDateHoursRightNow());
                jsResult.put("names", getNames());
                    
                
            icuso.outData.put("tabla", jsResult);
    } 

    //Estado Cambio de Situacion Financiera
    public void camsit() throws Exception {
          
        String anio= icuso.inData.getString("fltejer");
        String mes= icuso.inData.getString("fltper");

        anio="2024";
        mes="12";
    
        ejePs( "call p_camsit("+anio+","+mes+")");
        
            //JSONObject resul; 
        jsResult= ejeQry("    select\n" +
                                    "  tip,\n" +
                                    "  k,\n" +
                                    "  lk,\n" +
                                    " (case when monto0 <0 then abs(monto0) else (case when monto0 is null then null else 0 end) end) origen,-- resultado negativo\n" +
                                    " (case when monto0 >=0 then abs(monto0) else (case when monto0 is null then null else 0 end) end)  aplicacion\n" +
                                    "  FROM wrepos\n" +
                                    "order by k");
        icuso.outData.put("nombre", "Estado de Cambios en la Situación Financiera");
            
            jsResult.put("etiqueta", getEtiqueta(anio, mes));
            jsResult.put("getDateHoursRightNow", getDateHoursRightNow());
            jsResult.put("names", getNames());
                
            
        icuso.outData.put("tabla", jsResult);
           
    }
    //Estado de Variación en la Hacienda Pública
    public void varhac() throws Exception {
           
        String anio= icuso.inData.getString("fltejer");
        String mes= icuso.inData.getString("fltper");

        anio="2024";
        mes="12";
       ejePs( "call p_varhac("+anio+","+mes+")");

        //JSONObject resul; 
       jsResult= ejeQry("   select \n" +
                                   "tip ,\n" +
                                   "k,\n" +
                                   "lk, \n" +
                                   "monto0 *-1 mont0,\n" +
                                   "monto1 *-1 mont1,\n" +
                                   "monto2 *-1 mont2,\n" +
                                   "monto3 *-1 mont3,\n" +
                                   "monto4 *-1 mont4,\n" +
                                   "anio\n" +
                                   "from wrepos  order by  anio, k");
     icuso.outData.put("nombre", "Estado de Variación en la Hacienda Pública");
          
          jsResult.put("etiqueta", getEtiqueta(anio, mes));
          jsResult.put("getDateHoursRightNow", getDateHoursRightNow());
          jsResult.put("names", getNames());
            
           
     icuso.outData.put("tabla", jsResult);
           
    }
    //Estado Analitico del Activo
    public void anactiv() throws Exception {

                String anio= icuso.inData.getString("fltejer");
                String mes= icuso.inData.getString("fltper");

                anio="2024";
                mes="12"; 

            ejePs( "call p_anactiv("+anio+","+mes+")");
                //JSONObject resul; 
            jsResult= ejeQry(" select\n" +
                                    "  tip,\n" +
                                    "  k,\n" +
                                    "  lk,\n" +
                                    "  monto0 anterior,\n" +
                                    "  monto1 p_cargo,\n" +
                                    "  monto2 p_abono,\n" +
                                    "  monto3 saldo,\n" +
                                    "  monto4 saldomes\n" +
                                    "  FROM wrepos\n" +
                                    "order by k");
               icuso.outData.put("nombre", "Estado Analitico del Activo");
                
                jsResult.put("etiqueta", getEtiqueta(anio, mes));
                jsResult.put("getDateHoursRightNow", getDateHoursRightNow());
                jsResult.put("names", getNames());
                    
                
            icuso.outData.put("tabla", jsResult);
           
    }

    public void ldf_sitfin() throws Exception {
          
        String anio= icuso.inData.getString("fltejer");
        String mes= icuso.inData.getString("fltper");

        anio="2024";
        mes="12";
        
        ejePs( "call p_ldf_sitfin("+anio+","+mes+")");
     
        //JSONObject resul; 
        jsResult= ejeQry(" select tip, k, lk, monto0, monto1 from wrepos  where k like '1%'  order by k");
        jsResult.put("datos2", ejeQry(" select tip, k, lk, monto0, monto1 from wrepos  where ((k like '2%') or k like '3%')  order by k")) ;
            
            
            icuso.outData.put("nombre", "Estado de Situacion Financiera LDF");
        jsResult.put("etiqueta", getEtiquetaAnteior(anio, mes));
        jsResult.put("getDateHoursRightNow", getDateHoursRightNow());
        jsResult.put("names", getNames());
        icuso.outData.put("tabla", jsResult);
    }

    public void flujo() throws Exception {
          
        String anio= icuso.inData.getString("fltejer");
        String mes= icuso.inData.getString("fltper");

        anio="2024";
        mes="12";
        
        ejePs( "call p_flujo("+anio+","+mes+")");
     
        //JSONObject resul; 
                        //JSONObject resul; 
            jsResult= ejeQry(" select\n" +
                                    "  tip,\n" +
                                    "  k,\n" +
                                    "  lk,\n" +
                                    "  monto0 anterior,\n" +
                                    "  monto1 p_cargo,\n" +
                                    "  monto2 p_abono,\n" +
                                    "  monto3 saldo,\n" +
                                    "  monto4 saldomes\n" +
                                    "  FROM wrepos\n" +
                                    "order by k");
            
        icuso.outData.put("nombre", "Estado de Flujo de Efectivo");
        jsResult.put("etiqueta", getEtiquetaAnteior(anio, mes));
        jsResult.put("getDateHoursRightNow", getDateHoursRightNow());
        jsResult.put("names", getNames());
        icuso.outData.put("tabla", jsResult);
    }
    
    //actualizacion cuentas presupuetales
    public void updatep() throws Exception {
          
        String anio= icuso.inData.getString("fltejer");
        String mes= icuso.inData.getString("fltper");

        anio="2024";
        mes="12";
        
        ejePs( "call p_tacumdregiscoganio("+anio+")");

         ejePs( "commit");


          if(1==1){
            throw new Exception("Se actualizo correctamente");
        }

    }
    //actualizacion cuentas contables
    public void update() throws Exception {
          
        String anio= icuso.inData.getString("fltejer");
        String mes= icuso.inData.getString("fltper");

        anio="2024";
        mes="12";
        
        ejePs( "call p_acumdregisanio("+anio+")");

        ejePs( "commit");

        

        if(1==1){
            throw new Exception("Se actualizo correctamente");
        }

    }

     
        
        /**********************************************************************************************************************
         *****************************************       Etiquetas      *******************************************************
         **********************************************************************************************************************
        */
        
        
        public JSONArray getNames(){
            JSONArray Name=new JSONArray();
            
            Name.put("Lic. Pedro Torres González<br> Subsecretario de Egresos");//
            Name.put("C.P.C Raymundo Segura Estrada<br> Secretario de Finanzas y Administración");//

            
            return Name;
        }

        public JSONArray getEtiqueta(String anio, String mes){
        String DateName="";
        String Date="";
        JSONArray Head=new JSONArray();
        LocalDate now = LocalDate.of(Integer.parseInt(anio),Integer.parseInt(mes), 1);//anio, mes, dia
        LocalDate lastDay = now.with(TemporalAdjusters.lastDayOfMonth()); //obtemos el ultimo dia del mes
        //System.out.println(lastDay.atStartOfDay());
        
        

        String dia= String.valueOf(lastDay.getDayOfMonth()); 
        switch (mes) { 
            case "1":
             DateName="Del 1 de Enero al "+dia+" de Enero del "+anio;
             
             break;
            case "2":
            DateName="Del 1 de Enero al "+dia+" de Febrero del "+anio;
             break;
             case "3":
             DateName="Del 1 de Enero al "+dia+" de Marzo del "+anio;
             break;
            case "4":
            DateName="Del 1 de Enero al "+dia+" de Abril del "+anio;
             break;
             case "5":
             DateName="Del 1 de Enero al "+dia+" de Mayo del "+anio;
             break;
            case "6":
             DateName="Del 1 de Enero al "+dia+" de Junio del "+anio;
             break;
             case "7":
             DateName="Del 1 de Enero al "+dia+" de Julio del "+anio;
             break;
            case "8":
             DateName="Del 1 de Enero al "+dia+" de Agosto del "+anio;
             break;
             case "9":
             DateName="Del 1 de Enero al "+dia+" de Septiembre del "+anio;
             break;
            case "10":
             DateName="Del 1 de Enero al "+dia+" de Octubre del "+anio;
             break;
             case "11":
             DateName="Del 1 de Enero al "+dia+" de Noviembre del "+anio;
             break;
            case "12":
             DateName="Del 1 de Enero al "+dia+" de Diciembre del "+anio;
             break;
            default:
             // Default secuencia de sentencias.
          }
        
        
        Head.put(anio);//anio reporte
        Head.put(mes);//mes reporte
        Head.put(DateName);// fecha en nombre del reporte
        Head.put(Date= dia+"/"+mes+"/"+anio);// fecha del reporte en formato dd/mm/yyyy 
        Head.put(getDateHoursRightNow());// fecha actual formato dd/mm/yyyy
        Head.put(Integer.parseInt(anio)-1);//anio anterior 
        
        return Head;
    }
        
        public JSONArray getEtiquetaAnteior(String anio, String mes){
        String DateName="";
        String Date="";
        JSONArray Head=new JSONArray();
        LocalDate now = LocalDate.of(Integer.parseInt(anio),Integer.parseInt(mes), 1);//anio, mes, dia
        LocalDate lastDay = now.with(TemporalAdjusters.lastDayOfMonth()); //obtemos el ultimo dia del mes
        //System.out.println(lastDay.atStartOfDay());
        
        int aniop=Integer.parseInt(anio);

        String dia= String.valueOf(lastDay.getDayOfMonth()); 
        switch (mes) { 
            case "1":
             DateName="Al 31 de diciembre de "+(aniop-1)+" y al "+dia+" de Enero del "+anio;
             
             break;
            case "2":
            DateName="Al 31 de diciembre de "+(aniop-1)+" y al "+dia+" y de Febrero del "+anio;
             break;
             case "3":
             DateName="Al 31 de diciembre de "+(aniop-1)+" y al "+dia+" de Marzo del "+anio;
             break;
            case "4":
            DateName="Al 31 de diciembre de "+(aniop-1)+" y al "+dia+" de Abril del "+anio;
             break;
             case "5":
             DateName="Al 31 de diciembre de "+(aniop-1)+" y al "+dia+" de Mayo del "+anio;
             break;
            case "6":
             DateName="Al 31 de diciembre de "+(aniop-1)+" y al "+dia+" de Junio del "+anio;
             break;
             case "7":
             DateName="Al 31 de diciembre de "+(aniop-1)+" y al "+dia+" de Julio del "+anio;
             break;
            case "8":
             DateName="Al 31 de diciembre de "+(aniop-1)+" y al "+dia+" de Agosto del "+anio;
             break;
             case "9":
             DateName="Al 31 de diciembre de "+(aniop-1)+" y al "+dia+" de Septiembre del "+anio;
             break;
            case "10":
             DateName="Al 31 de diciembre de "+(aniop-1)+" y al "+dia+" de Octubre del "+anio;
             break;
             case "11":
             DateName="Al 31 de diciembre de "+(aniop-1)+" y al "+dia+" de Noviembre del "+anio;
             break;
            case "12":
             DateName="Al 31 de diciembre de "+(aniop-1)+" y al "+dia+" de Diciembre del "+anio;
             break;
            default:
             // Default secuencia de sentencias.
          }
        
        
        Head.put(anio);//anio reporte
        Head.put(mes);//mes reporte
        Head.put(DateName);// fecha en nombre del reporte
        Head.put(Date= dia+"/"+mes+"/"+anio);// fecha del reporte en formato dd/mm/yyyy 
        Head.put(getDateHoursRightNow());// fecha actual formato dd/mm/yyyy
        Head.put(Integer.parseInt(anio)-1);//anio anterior
        
        return Head;
    }
    
        public String  getDateHoursRightNow(){
            String DateHour;

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            DateHour=dtf.format(LocalDateTime.now());



            return DateHour;
        }
         
         
        
        

}