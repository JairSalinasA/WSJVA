package cusos.informe;

import srv.cuso;
//import org.json.JSONArray;
//import org.json.JSONObject;

import comun.bkComun;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import org.json.JSONArray;

public class ini extends bkComun {

     public ini(cuso inCuso) throws Exception {
          super(inCuso);
     }

     public void activ() throws Exception {
            
             String anio= icuso.inData.getString("fltejer");
             String mes= icuso.inData.getString("fltper");
             
           
        
            ejePs( "call p_estadoactividades("+anio+","+mes+")");
              
                
          
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

     public void sitfin() throws Exception {
          
             String anio= icuso.inData.getString("fltejer");
             String mes= icuso.inData.getString("fltper");
             
           
        
            ejePs( "call p_edositufinan("+anio+","+mes+")");
             
               
          
              //JSONObject resul; 
          jsResult= ejeQry("select tip, k, lk, monto0, monto1 from wrepos  where ((k like '2%') or k like '3%')  order by k ");
                jsResult.put("datos2", ejeQry(" select tip, k, lk, monto0, monto1 from wrepos  where k like '1%'  order by k ")) ;
                  
          icuso.outData.put("nombre", "Estado de Situacion Financiera");
              
               
               
               jsResult.put("etiqueta", getEtiqueta(anio, mes));
               jsResult.put("getDateHoursRightNow", getDateHoursRightNow());
               jsResult.put("names", getNames());
                 
                
          icuso.outData.put("tabla", jsResult);
     }   
        
        public void ldf_sitfin() throws Exception {
          
             String anio= icuso.inData.getString("fltejer");
             String mes= icuso.inData.getString("fltper");
             
            
        
            ejePs( "call p_edositufinanldf("+anio+","+mes+")");
             
               
          
             //JSONObject resul; 
            jsResult= ejeQry(" select tip, k, lk, monto0, monto1 from wrepos  where k like '1%'  order by k");
                  jsResult.put("datos2", ejeQry(" select tip, k, lk, monto0, monto1 from wrepos  where ((k like '2%') or k like '3%')  order by k")) ;
                  
                  
          icuso.outData.put("nombre", "Estado de Situacion Financiera LDF");
              
               
               
               jsResult.put("etiqueta", getEtiquetaAnteior(anio, mes));
               jsResult.put("getDateHoursRightNow", getDateHoursRightNow());
               jsResult.put("names", getNames());
                 
                
          icuso.outData.put("tabla", jsResult);
     }

     public void fin() throws Exception {
          super.fin();
                
               
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
        
        public void objgsto() throws Exception {
          
             String anio= icuso.inData.getString("fltejer");
             String mes= icuso.inData.getString("fltper");
             
           
        
            ejePs( "call p_repanaegregasto("+anio+","+mes+")");
             
               
          
             //JSONObject resul; 
            jsResult= ejeQry("  select\n" +
                                    " tip,  k conac ,lk rubro\n" +
                                    "  ,monto0 *-1 aprobado\n" +
                                    "  ,monto1 * -1 reduccion\n" +
                                    "  ,(monto0+monto1) * -1 modificado\n" +
                                    "  ,monto2 devengado\n" +
                                    "  ,monto3 pagado\n" +
                                    "  ,(monto0+monto1+monto2) * -1 subejercicio\n" +
                                    "from wrepos order by k");
          icuso.outData.put("nombre", "Estado Analitico de Egresos (Clasificacion Objeto del Gasto)");
              
               
               
               jsResult.put("etiqueta", getEtiqueta(anio, mes));
               jsResult.put("getDateHoursRightNow", getDateHoursRightNow());
               jsResult.put("names", getNames());
                
          icuso.outData.put("tabla", jsResult);
           
     }
        
      public void result() throws Exception {
          
             String anio= icuso.inData.getString("fltejer");
             String mes= icuso.inData.getString("fltper");
             
           /* 
        
            ejePs( "call p_repanaegregasto("+anio+","+mes+")");
             
               
          
             //JSONObject resul; 
            jsResult= ejeQry("  select\n" +
                                    " tip,  k conac ,lk rubro\n" +
                                    "  ,monto0 *-1 aprobado\n" +
                                    "  ,monto1 * -1 reduccion\n" +
                                    "  ,(monto0+monto1) * -1 modificado\n" +
                                    "  ,monto2 devengado\n" +
                                    "  ,monto3 pagado\n" +
                                    "  ,(monto0+monto1+monto2) * -1 subejercicio\n" +
                                    "from wrepos order by k");
          icuso.outData.put("nombre", "Estado Analitico de Egresos (Clasificacion Objeto del Gasto)");
              
               
               
               jsResult.put("etiqueta", getEtiqueta(anio, mes));
               jsResult.put("getDateHoursRightNow", getDateHoursRightNow());
               jsResult.put("names", getNames());
             
             
                
          icuso.outData.put("tabla", jsResult);
             
          */
           
            //JSONObject resul; 
            jsResult= ejeQry("  SELECT NOW()");
     }
        
        public void func() throws Exception {
          
             String anio= icuso.inData.getString("fltejer");
             String mes= icuso.inData.getString("fltper");
             
           
        
            ejePs( "call p_analiegreclasfuncional("+anio+","+mes+")");
             
               
          
             //JSONObject resul; 
            jsResult= ejeQry("  select \n" +
                                        "  tip, k conac ,lk rubro\n" +
                                        "  ,monto0 *-1 aprobado\n" +
                                        "  ,monto1 * -1 reduccion\n" +
                                        "  ,(monto0+monto1) * -1 modificado\n" +
                                        "  ,monto2 devengado\n" +
                                        "  ,monto3 pagado\n" +
                                        "  ,(monto0+monto1+monto2) * -1 subejercicio\n" +
                                        "from wrepos \n" +
                                        "order by cast(k as decimal)");
          icuso.outData.put("nombre", "Estado Analitico de Egresos Clasificación Funcional (Finalidad y Función)");
              
               
               
               jsResult.put("etiqueta", getEtiqueta(anio, mes));
               jsResult.put("getDateHoursRightNow", getDateHoursRightNow());
               jsResult.put("names", getNames());
                
          icuso.outData.put("tabla", jsResult);
                
                
                
               
                
     }
        
        
        public void anactiv() throws Exception {
          
             String anio= icuso.inData.getString("fltejer");
             String mes= icuso.inData.getString("fltper");
             
           
        
            ejePs( "call p_estadoanaliactivo("+anio+","+mes+")");
             
               
          
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
        
        public void camsit() throws Exception {
          
             String anio= icuso.inData.getString("fltejer");
             String mes= icuso.inData.getString("fltper");
             
           
        
            ejePs( "call p_edocambiosituafinan("+anio+","+mes+")");
             
               
          
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
        
         public void varhac() throws Exception {
           
             String anio= icuso.inData.getString("fltejer");
             String mes= icuso.inData.getString("fltper");
             
           
        
            ejePs( "call p_edovariacionhacienda("+anio+","+mes+")");
             
               
          
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
        
        
        
        public void admva() throws Exception {
          
             String anio= icuso.inData.getString("fltejer");
             String mes= icuso.inData.getString("fltper");
             
           
        
            ejePs( "call p_repanaegreadmin("+anio+","+mes+")");
             
               
          
             //JSONObject resul; 
            jsResult= ejeQry("select\n" +
                                    "  tip, k conac ,lk rubro\n" +
                                    "  ,monto0 *-1 aprobado\n" +
                                    "  ,monto1 * -1 reduccion\n" +
                                    "  ,(monto0+monto1) * -1 modificado\n" +
                                    "  ,monto2 devengado\n" +
                                    "  ,monto3 pagado\n" +
                                    "  ,(monto0+monto1+monto2) * -1 subejercicio\n" +
                                    "from wrepos order by k");
          icuso.outData.put("nombre", "Estado Analitico de Egresos Clasificacion Administrativa");
              
               
               
               jsResult.put("etiqueta", getEtiqueta(anio, mes));
               jsResult.put("getDateHoursRightNow", getDateHoursRightNow());
               jsResult.put("names", getNames());
                 
                
          icuso.outData.put("tabla", jsResult);
                
              
                
     }
        public void serper() throws Exception {
          
             String anio= icuso.inData.getString("fltejer");
             String mes= icuso.inData.getString("fltper");
             
           
        
            ejePs( "call p_analiegreclaserviciosperso("+anio+","+mes+")");
             
               
          
             //JSONObject resul; 
            jsResult= ejeQry("  select \n" +
                                        "  tip, k conac ,lk rubro\n" +
                                        "  ,monto0 *-1 aprobado\n" +
                                        "  ,monto1 * -1 reduccion\n" +
                                        "  ,(monto0+monto1) * -1 modificado\n" +
                                        "  ,monto2 devengado\n" +
                                        "  ,monto3 pagado\n" +
                                        "  ,(monto0+monto1+monto2) * -1 subejercicio\n" +
                                        "from wrepos \n" +
                                        "order by cast(k as decimal)");
          icuso.outData.put("nombre", "Estado Analitico de Egresos Clasificación de Servicios Personales por Categoría");
              
               
               
               jsResult.put("etiqueta", getEtiqueta(anio, mes));
               jsResult.put("getDateHoursRightNow", getDateHoursRightNow());
               jsResult.put("names", getNames());
                 
                
          icuso.outData.put("tabla", jsResult);
                
              
                
     }
        
        
        
        public JSONArray getNames(){
            JSONArray Name=new JSONArray();
            
            Name.put("Lic. Pedro Torres González<br> Subsecretario de Egresos");//
            Name.put("C.P.C Raymundo Segura Estrada<br> Secretario de Finanzas y Administración");//

            
            return Name;
        }
         
         
        
        

}