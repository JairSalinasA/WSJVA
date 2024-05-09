package cusos.layo;

import srv.cuso;

import java.io.BufferedReader;
import java.io.File;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.lang.reflect.Method;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import comun.bkComun;

public class ini extends bkComun {
    private String banco, archi, fecha, clabe, fndo;

    public ini(cuso inCuso) throws Exception {
        super(inCuso);
        kComa = "'";
    }

    public void lis() throws Exception {
        super.lis();
    }

    public void cap() throws Exception {
        if (icuso.cuso.equals("layo.ini.cap_repo")) {
            archi = icuso.inData.getString("regis");
            icuso.prop.setProperty("det", ""+
                     "select 'L.C. MARTHA TOMASA ARANA CÁSTULO' nom,"+
					" regis ,to_char(fecha,'dd/mm/yyyy') fecha  ,anio ,mes "+
					" ,case when tt.tpol then 'E' when not tt.tpol then 'I' else 'D' end ctpol "+
					",row_number() over (partition by anio,mes,tt.tpol order by regis) num "+
					" ,r.teven ,tt.lteven "+
					"  ,descrip "+
					" ,monto ,rete ,monto-rete neto "+
					" from tregis r "+
					" inner join tteven tt on tt.teven=r.teven and tt.movs "+
					" where 1=1 and regis= "+archi+" order by regis"
                    + "");
        }
		//sjsVals.put("ejer", icuso.inData.get("ejer"));
		super.cap(); 
        
    }

    public void graba() throws Exception {

        String dml = icuso.inData.getString("dml");

        if (dml.equals("3")) {
            archi = icuso.inData.getString("archi");

            eje = "delete from tdregis where regis in(select regis from tdetlayout where archi="+ archi+")" ;
            nregis = ejePs(eje);

            eje = "delete from tregis where regis in(select regis from tdetlayout where archi="+ archi+")" ;
            nregis = ejePs(eje);

            eje = "delete from tdetlayout_excep where archi=" + archi;
            nregis = ejePs(eje);

            eje = "delete from tdetlayout where archi=" + archi;
            nregis = ejePs(eje);

            eje = "delete from tarchivos where archi=" + archi;
            nregis = ejePs(eje);
        }

        if (dml.equals("1")) {

            banco = icuso.inData.getString("banco");
            clabe = icuso.inData.getString("clabe");
            String url = icuso.inData.getString("url");
            banco = icuso.inData.getString("banco");
            fndo = icuso.inData.getString("fndo");

            String dirfis = "/srv/UploadFile/archis/";
            if (url.contains("localhost"))
            {
                // dirfis= "/Users/christianmendoza/Documents/Warrior/Proyectos git/nuevos
                // archivos excel/Saldos Bancos/"
                dirfis = "/Users/adolfosalazarvargas/Desarrollo/srvarchinode/archis/";
            }
          
            dirfis = dirfis + url.split("archis/")[1];
           //  dirfis = "/Users/christianmendoza/Downloads/123036.xlsx";


            
             

            
            // regis=archi
            qry("select nextval('pk_dregis') conse");
            archi = icuso.dbx.JSTblgetvCampo("conse").toString();

            // header
            eje = "insert into tarchivos (archi ,url, tarchi, larchi,fecha ) values(?,?,?,?,now()) ";
            nregis = ejePs(eje, new String[] {
                    archi, "N",
                    icuso.inData.getString("url"), "C",
                    "15", "N",
                    clabe, "C"
            });



            //validamos que no se pueda subir un archivo con el mismo banco y fecha
            

            eje = "layout" + banco;
            Method metodo = this.getClass().getDeclaredMethod(eje, String.class);

            String miErr = "";
            try {
                metodo.invoke(this, dirfis);
            } catch (Exception ex) {
                miErr = ex.toString();
                if (miErr.contains("reflect.InvocationTargetException")) {
                    miErr = ex.getCause().toString();
                    if (miErr.contains("org.postgresql.util.PSQLException:")) {
                        int det = miErr.indexOf("Detail:");
                        if (det == -1)
                            det = miErr.length();
                        miErr = miErr.substring(41, det);
                    }
                    // regre=ex.getLocalizedMessage();
                }
            }
            if (!miErr.equals(""))
                throw new Exception(miErr);


                ejePs( "call p_layout_det_regis("+archi+")");


        }

        if (!dml.equals("3")) {// Si no es borrar regresa nuevo registro
            eje = "select * from (" + icuso.ponInData(prop.getProperty("lis")) + ") lisQ where archi =" + archi;
            jsResult = ejeQry(eje, new String[] {});
        }
        icuso.outData.put("dml", dml);
    }

    public void validarExisteArchivo(String fecha, String banco, String clabe ) throws Exception{
        qry("select count(*) conta from tdetlayout t where to_char(fecha,'dd/mm/yyyy')='"+fecha+"' and banco ='"+banco+"' " + "and  clabe='"+clabe+"'");
        String valida = icuso.dbx.JSTblgetvCampo("conta").toString();

        if(!valida.equals("0")){

             qry("select lbnco  from tcbancos t  where bnco ='"+banco+"'");
             String lbanco = icuso.dbx.JSTblgetvCampo("lbnco").toString();
             throw new Exception("Ya existe un archivo con fecha: "+fecha+" dado de alta con el banco: "+lbanco+" y la clabe: "+clabe);
        }

    }

    // Banamex
    public void layout002(String archivo) throws Exception {
              String rutaArchivoExcel = archivo;
            FileInputStream inputStr = new FileInputStream(rutaArchivoExcel);
            XSSFWorkbook xssfWork = new XSSFWorkbook(inputStr) ;
            XSSFSheet sheet1 = xssfWork.getSheetAt(0);
            int numero_registros=0;
            int nrow=1;
            String cuenta="";
             String clabe_int="";
           
            boolean detalle=false; 
           
                DataFormatter formatter = new DataFormatter();
                FormulaEvaluator evaluator = xssfWork.getCreationHelper().createFormulaEvaluator();
               //barremos las filas
               for(Row row : sheet1){
                   
                    org.apache.poi.ss.usermodel.Cell cell_ = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    
                    if(cell_.toString().equals("Cuenta")){
                     
                        org.apache.poi.ss.usermodel.Cell cell1 = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        cuenta=formatter.formatCellValue(cell1, evaluator);
                         clabe_int= convertirClabe(cuenta, banco);

                        if (!clabe_int.equals(cambiarClabeBanamex(clabe))) {
                            throw new Exception("Las Clabe Ingresada no es la misma Clabe que contiene el archivo");
                        }

                        
                    }
                    
                       if(detalle==true){// no entra a encabezados
                           org.apache.poi.ss.usermodel.Cell cell2 = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                           
                             if(!cell2.toString().equals("")){
                                /*
                                datos[0]=Fecha
                                datos[1]=Descripción
                                datos[2]=Depósitos cargos
                                datos[3]=Retiros   abonos
                                datos[4]=Saldo
                                datos[5]=tmov
                                datos[6]=monto
                                datos[7]=Numerica cref
                                datos[8]=Alfanumerico nref
                                datos[9]=Autorizacion
                                */
                                
                               String[] datos = new String[13];
                                for(int cn=0; cn<row.getLastCellNum(); cn++) {
                                        org.apache.poi.ss.usermodel.Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                        String value="";
                                        
                                        switch (cn) { 
                                            case 0://fecha
                                                
                                             value=obtenerFechaFormateada(formatter.formatCellValue(cell, evaluator), "dd/MM/yyyy");//formato de fecha
                                             break;
                                            case 1://sacamos erl alfanumero
                                                
                                                //numerica
                                                String separar[]= formatter.formatCellValue(cell, evaluator).split("Referencia Númerica:");
                                                String alfa[]=separar[1].split("Referencia Alfanúmerica:");
                                                datos[7]=alfa[0].trim();
                                                
                                                //alfanumerica
                                                String numerica[]=alfa[1].split("Autorización:");
                                                datos[8]=numerica[0].trim();
                                                
                                                
                                                 //autorizacion
                                                String autoriza[]=numerica[1].split("Autorización:");
                                                datos[9]=autoriza[0].trim();
                                                
                                                value=  formatter.formatCellValue(cell, evaluator);
                                                
                                             
                                             break;
                                            case 2 :
                                                
                                                if(!cell.toString().equals("-")){
                                                        double res=Double.parseDouble(cell.toString());
                                                        value=EliminarNotacionCientífica(res);
                                                        datos[6]=value;
                                                        datos[5]="1";
                                                }else{
                                                        value=  formatter.formatCellValue(cell, evaluator);
                                                }
                                             
                                             break;
                                            case 3 :
                                                
                                                if(!cell.toString().equals("-")){
                                                        double res=Double.parseDouble(cell.toString());
                                                        value=EliminarNotacionCientífica(res);
                                                        datos[6]=value;
                                                        datos[5]="-1";
                                                }else{
                                                        value=  formatter.formatCellValue(cell, evaluator);
                                                }
                                                
                                             break;
                                            
                                            default:
                                             // Default secuencia de sentencias.
                                          }
                                        
                                        
                                        datos[cn]=value; 
                                }
                                
                                      
                                        datos[6]=datos[6].replace("$", "").replace(",", "").replace("-", "");
                                        datos[2]=datos[2].replace("$", "").replace(",", "").replace("-", "");
                                        datos[3]=datos[3].replace("$", "").replace(",", "").replace("-", "");

                                        
                                    
                                        numero_registros++;


                                        

                                        System.out.println("Fecha: "+datos[0]+" Descripción: "+datos[1]+" Depósitos: "+datos[2]+
                                        " Retiros: "+datos[3]+" Saldo: "+datos[4]+
                                        " Tmov: "+datos[5]+" Monto: "+datos[6]+
                                        " Numerico nref: "+datos[7] +" Alfanumerico cref: "+datos[8]+
                                        " Autorizacion : "+datos[9]+ " Cuenta: "+cuenta+ " Clabe: "+ clabe);

                                        qry("select nextval('pk_dregis') conse");
                                        String regb = icuso.dbx.JSTblgetvCampo("conse").toString();

                                             //
                                        eje = " insert into tdetlayout(archi, fecha, fndo, item, tmov, monto, crefer, nrefer, clabe, banco, descrip, linea)\n" + 
                                                "values(?,?,?,?,?,?,?,?,?,?,?,?) ";
                                        nregis = ejePs(eje, new String[] {
                                            archi, "N",
                                            datos[0], "F", //fecha
                                            fndo, "N",     //fndo
                                            regb, "N",     //item
                                            datos[5], "N", //tmov
                                            datos[6], "N", //monto
                                            datos[8], "C", //crefer
                                            datos[9], "N",  //nrefer
                                            clabe, "C",
                                            banco, "C",
                                            datos[1], "C", //descrip
                                            datos[0]+"|"+datos[1]+"|"+datos[2]+"|"+datos[3]+"|"+datos[4]+"|"+datos[5]+"|"+datos[6]+"|"+datos[7]+"|"+datos[8]+"|"+datos[9], "C"//linea          

                                        });


                            }
                        }
                       
                   if(cell_.toString().equals("Fecha")){
                            detalle=true;
                   }
                          nrow++;
               }
              
               System.out.println("Total de registros leidos: "+numero_registros);
        
    }
    // Santander
    public void layout014(String archivo) throws Exception {
                    String rutaArchivoExcel = archivo;
            FileInputStream inputStr = new FileInputStream(rutaArchivoExcel);
            XSSFWorkbook xssfWork = new XSSFWorkbook(inputStr) ;
            XSSFSheet sheet1 = xssfWork.getSheetAt(0);
            int numero_registros=0;
            int nrow=1;
            int numero_columnas=0;
            
                DataFormatter formatter = new DataFormatter();
                FormulaEvaluator evaluator = xssfWork.getCreationHelper().createFormulaEvaluator();
                boolean salto=true;
               //barremos las filas
               for(Row row : sheet1){

                if(numero_registros==0){
                  numero_columnas=  row.getLastCellNum();
                }
                   
                        if(nrow>=2){// no entra a encabezados
                                /*
                                datos[0]=Cuenta
                                datos[1]=Fecha
                                datos[2]=Hora
                                datos[3]=Sucursal
                                datos[4]=Descripción
                                datos[5]=Cargo/Abono
                                datos[6]= Importe 
                                datos[7]=Saldo
                                datos[8]=Referencia
                                datos[9]=Concepto
                                datos[10]=Banco Participante
                                datos[11]=Clave Beneficiario
                                datos[12]=Nombre Beneficiario
                                datos[13]=Cta Ordenante
                                datos[14]=Nombre Ordenante
                                datos[15]=Código Devolución
                                datos[16]=Causa Devolución
                                datos[17]=RFC Beneficiario
                                datos[18]=RFC Ordenante
                                datos[19]=Clave de Rastreo
                                datos[20]=Combinacion del datos[4]+ datos[9]
                            
                                */
                                
                               String[] datos = new String[21];
                                for(int cn=0; cn<numero_columnas; cn++) {
                                        org.apache.poi.ss.usermodel.Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                        String value="";
                                        
                                        switch (cn) { 
                                            case 0://fecha
                                                
                                                  value=cell.toString().trim();//  formatter.formatCellValue(cell, evaluator);
                                                      value=value.replace("'", "").trim();
                                                  
                                              break;
                                            case 1://sacamos erl alfanumero
                                                String val=cell.toString();
                                                
                                                value=val.substring(1,3)+"/"+ val.substring(3,5)+"/"+val.substring(5,9);
                                             
                                             break;
                                            case 5 :
                                                if(cell.toString().equals("+")){
                                                    value="1";
                                                }else{
                                                    value="-1";
                                                }
                                              
                                             break;
                                            case 6 : 
                                                        String monn=cell.toString();
                                                        monn=monn.replace("'", "");
                                                          value=EliminarNotacionCientífica(Double.parseDouble(monn));
                                                    
                                             break;
                                             case 7 :
                                                        String monn_=cell.toString();
                                                        monn_=monn_.replace("'", "");
                                                        value=EliminarNotacionCientífica(Double.parseDouble(monn_));
  
                                             break;
                                              case 8 :
                                                        value=  cell.toString();
                                                        if(!value.equals("")){

                                                            if(value.length()>1){
                                                                value=value.substring(1, value.length()-1).trim();
                                                            }
                                                            

                                                        }
                                                         
  
                                             break;
                                              case 9 :
                                                       if(cell!=null){
                                                         value=  cell.toString();
                                                       }else{
                                                        value="";
                                                       }
                                                       
                                                        datos[20]=datos[4]+value;

                                                        if(value.contains("'")){
                                                            String arre[]=value.split("'");
                                                            String cadena=arre[1];
                                                            
                                                            String arre1[]=cadena.split(" ");
                                                            String cadena2=arre1[0];

                                                            boolean isNumeric = (cadena2 != null && cadena2.matches("[0-9]+"));
               
                                                            if(isNumeric){
                                                                value=cadena2;

                                                            }else{
                                                                value="";
                                                            }

                                                             
                                                        }else{
                                                            value="";
                                                        }
  
                                             break;
                                            
                                            default:
                                             // Default secuencia de sentencias.
                                                value=  cell.toString();
                                          }
                                        
                                        datos[cn]=value;
                                        
                                }

                                        
                                 
                                        datos[6]=datos[6].replace("$", "").replace(",", "").replace("-", "");

                                        if(datos[9]!=null){
                                             datos[9]=datos[9].replaceAll("'", "");
                                        }else{
                                            datos[9]="";
                                        }
                                       

                                        

                                       String clabe_int= convertirClabe(datos[0], banco);

                                        if (!clabe_int.equals(cambiarClabe(clabe))) {
                                            throw new Exception("Las Clabe Ingresada no es la misma Clabe que contiene el archivo");
                                        }

                                        
                                        
                                        numero_registros++;
                                        System.out.println("Cuenta: "+datos[0]+" Fecha: "+datos[1]+" Hora: "+datos[2]+
                                        "  Sucursal: "+datos[3]+" Descripción: "+datos[4]+
                                        "  Cargo/Abono: "+datos[5]+"  Importe : "+datos[6]+
                                        "  Saldo: "+datos[7] +"  Referencia   crefer: "+datos[9]+
                                        "  Concepto  : "+datos[9]+
                                        "  Banco Participante: "+datos[10] +"  Clave Beneficiario: "+datos[11]+
                                        "  Nombre Beneficiario: "+datos[12] +"  Cta Ordenante: "+datos[13]+
                                        "  Nombre Ordenante: "+datos[14] +" Código Devolución: "+datos[15]+
                                        "  Causa Devolución: "+datos[16] +"  RFC Beneficiario: "+datos[17]+
                                        "  RFC Ordenante: "+datos[18] + "  Clave de Rastreo: "+datos[19] 
                                                 
                                        );

                                        qry("select nextval('pk_dregis') conse");
                                        String regb = icuso.dbx.JSTblgetvCampo("conse").toString();
                                        /* 
                                        // header
                                        eje = "insert into tdregis( regis, dregis, auxi , cnta, tmov,  monto , ldregis, feceven, fndo, crefer, item, even, cntap) values(?,?,?,?,?,?,?,?,?,?,?,?,?) ";
                                        nregis = ejePs(eje, new String[] {
                                                regis, "N",
                                                regb, "N",
                                                clabe, "C",
                                                "11121", "C",
                                                datos[5], "N", //tmov
                                                datos[6], "N", //monto
                                                datos[20], "C", //descrip
                                                datos[1], "F", //fecha
                                                fndo, "N",     //fndo
                                                datos[9], "C", //crefer
                                                regb, "N",//IDREF
                                                regis, "N",//SOLICITUD
                                                 "8121", "N"

                                        });*/

                                        if(datos[9].equals("08540210045140241276")){
                                            System.out.println("rutaArchivoExcel");
                                        }

                                             //
                                        eje = " insert into tdetlayout(archi, fecha, fndo, item, tmov, monto, crefer, nrefer, clabe, banco, descrip, linea)\n" + 
                                                "values(?,?,?,?,?,?,?,?,?,?,?,?) ";
                                        nregis = ejePs(eje, new String[] {
                                            archi, "N",
                                            datos[1], "F", //fecha
                                            fndo, "N",     //fndo
                                            regb, "N",     //item
                                            datos[5], "N", //tmov
                                            datos[6], "N", //monto
                                            datos[9], "C", //crefer
                                            "", "N",  //nrefer
                                            clabe, "C",
                                            banco, "C",
                                            datos[20], "C", //descrip
                                            datos[0]+"|"+datos[1]+"|"+datos[2]+"|"+datos[3]+"|"+datos[4]+"|"+datos[5]+"|"+datos[6]+"|"+datos[7]+"|"+datos[8]+"|"+datos[9]+"|"+
                                            datos[10]+"|"+datos[11]+"|"+datos[12]+"|"+datos[13]+"|"+datos[14]+"|"+datos[15]+"|"+datos[16]+"|"+datos[17]+"|"+datos[18]+"|"+datos[19]+"|"+
                                            datos[20] , "C"//linea          

                                        });

                            
                        }
                          nrow++;
               }
              
               System.out.println("Total de registros leidos: "+numero_registros);
        
    }


    // Azteca
    public void layout127(String archivo) throws Exception {
        String rutaArchivoExcel = archivo;
            FileInputStream inputStr = new FileInputStream(rutaArchivoExcel);
            XSSFWorkbook xssfWork = new XSSFWorkbook(inputStr) ;
            XSSFSheet sheet1 = xssfWork.getSheetAt(0);
            int numero_registros=0;
            int nrow=1;
           
                DataFormatter formatter = new DataFormatter();
                FormulaEvaluator evaluator = xssfWork.getCreationHelper().createFormulaEvaluator();
                boolean salto=true;
               //barremos las filas
               for(Row row : sheet1){
                   
                        if(nrow>=2){// no entra a encabezados
                            
                            org.apache.poi.ss.usermodel.Cell cell1 = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                            if(!cell1.toString().equals("")){
                                
                                    /*
                                    datos[0]=NUMERO DE CUENTA
                                    datos[1]=FECHA DE OPERACION
                                    datos[2]=FECHA DE APLICACION
                                    datos[3]=CONCEPTO
                                    datos[4]=IMPORTE
                                    datos[5]=SALDO
                                    datos[6]=MOVIMIENTO
                                    datos[7]=campo sin titulo
                                    datos[8]=tmov

                                    */

                                            String[] datos = new String[10];
                                             for(int cn=0; cn<row.getLastCellNum(); cn++) {
                                                     org.apache.poi.ss.usermodel.Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                                     
                                                    if(cn==3){
                                                        if(cell.toString().toUpperCase().contains("ABONO")){
                                                            datos[8]="1";
                                                        }else{
                                                             if(cell.toString().toUpperCase().contains("CONTRATO")){
                                                                 datos[8]="1";
                                                             }else{
                                                                 datos[8]="-1";
                                                             }
                                                           
                                                        }
                                                    }
                                                     
                                                    if(cn==1|| cn==2){
                                                        String fecha[]=cell.toString().split("-");
                                                        datos[cn]=fecha[2].substring(0,2)+"/"+fecha[1]+"/"+fecha[0]+fecha[2].substring(2);

                                                    }else{
                                                            String value=cell.toString();
                                                            datos[cn]=value;
                                                    }
                                             }

                                              


                                            String clabe_int= convertirClabe(datos[0].substring(4), banco);
                                            if (!clabe_int.equals(cambiarClabe(clabe))) {
                                                throw new Exception("Las Clabe Ingresada no es la misma Clabe que contiene el archivo");
                                            }
                                            
                                            
                                            numero_registros++;
                                            System.out.println("NUMERO DE CUENTA: "+datos[0]+" FECHA DE OPERACION: "+datos[1]+" FECHA DE APLICACION "+datos[2]+
                                            " CONCEPTO: "+datos[3]+" IMPORTE: "+datos[4]+
                                            " SALDO: "+datos[5]+"  MOVIMIENTO CREF : "+datos[6]+
                                            " CAMPO SIN TITULO: "+datos[7]  +" TMOV: "+datos[8]

                                            );

                                            qry("select nextval('pk_dregis') conse");
                                             String regb = icuso.dbx.JSTblgetvCampo("conse").toString();
                                            /* 
                                            // header
                                            eje = "insert into tdregis( regis, dregis, auxi , cnta, tmov,  monto , ldregis, feceven, fndo, crefer, item, even, cntap) values(?,?,?,?,?,?,?,?,?,?,?,?,?) ";
                                            nregis = ejePs(eje, new String[] {
                                                    regis, "N",
                                                    regb, "N",
                                                    clabe, "C",
                                                    "11121", "C",
                                                    datos[8], "N", //tmov
                                                    datos[4], "N", //monto
                                                    datos[3], "C", //descrip
                                                    datos[1], "F", //fecha
                                                    fndo, "N",     //fndo
                                                    datos[6], "C", //crefer
                                                    regb, "N",//IDREF
                                                    regis, "N",//SOLICITUD
                                                     "8121", "N"

                                            });*/

                                                //
                                                eje = " insert into tdetlayout(archi, fecha, fndo, item, tmov, monto, crefer, nrefer, clabe, banco, descrip, linea)\n" + 
                                                        "values(?,?,?,?,?,?,?,?,?,?,?,?) ";
                                                nregis = ejePs(eje, new String[] {
                                                    archi, "N",
                                                    datos[1], "F", //fecha
                                                    fndo, "N",     //fndo
                                                    regb, "N",     //item
                                                    datos[8], "N", //tmov
                                                    datos[4], "N", //monto
                                                    datos[6], "C", //crefer
                                                    "", "N",  //nrefer
                                                    clabe, "C",
                                                    banco, "C",
                                                    datos[3], "C", //descrip
                                                    datos[0]+"|"+datos[1]+"|"+datos[2]+"|"+datos[3]+"|"+datos[4]+"|"+datos[5]+"|"+datos[6]+"|"+datos[7]+"|"+datos[8], "C"//linea          

                                                });


                            }
                            
                        }
                          nrow++;
               }
              
               System.out.println("Total de registros leidos: "+numero_registros);
        
    }

    // HSBC
    public void layout021(String archivo) throws Exception {
        String rutaArchivoExcel = archivo;
        FileInputStream inputStr = new FileInputStream(rutaArchivoExcel);
        XSSFWorkbook xssfWork = new XSSFWorkbook(inputStr) ;
        XSSFSheet sheet1 = xssfWork.getSheetAt(0);
        int numero_registros=0;
        int nrow=1;
       
            DataFormatter formatter = new DataFormatter();
            FormulaEvaluator evaluator = xssfWork.getCreationHelper().createFormulaEvaluator();
            boolean salto=true;
           //barremos las filas
           for(Row row : sheet1){
               
                    if(nrow>=2){// no entra a encabezados
                            /*
                            datos[0]=Nombre de cuenta
                            datos[1]=Número de cuenta
                            datos[2]=Nombre del banco
                            datos[3]=Moneda
                            datos[4]=Ubicación
                            datos[5]=BIC
                            datos[6]= IBAN 
                            datos[7]=Estatus de cuenta
                            datos[8]=Tipo de cuenta
                            datos[9]=Saldo en libros al cierre
                            datos[10]=Saldo en libros final al cierre del ejercicio anterior de
                            datos[11]=Saldo disponible al cierre
                            datos[12]=Saldo final disponible del ejercicio anterior de
                            datos[13]=Saldo actual en libros
                            datos[14]=Saldo actual en libros al
                            datos[15]=Saldo actual disponible
                            datos[16]=Saldo actual disponible al
                            datos[17]=Referencia bancaria
                            datos[18]=Narrativa
                            datos[19]=Referencia de cliente
                            datos[20]=Tipo de TRN
                            datos[21]=Fecha valor
                            datos[22]=Importe abonado
                            datos[23]=Importe cargado
                            datos[24]=Saldo
                            datos[25]=Tiempo
                            datos[26]=Fecha contable
                            datos[27]=monto
                            datos[28]=tmov
                        
                        
                        
                            */
                            
                           String[] datos = new String[30];
                            for(int cn=0; cn<row.getLastCellNum(); cn++) {
                                    org.apache.poi.ss.usermodel.Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                    
                                    String value=cell.toString();
                                    datos[cn]=value;

                                    if(cn==22){
                                        if(!cell.toString().equals("")){
                                             datos[28]="1";
                                             datos[27]=cell.toString();
                                        }
                                    }
                                    if(cn==23){
                                        if(!cell.toString().equals("")){
                                             datos[28]="-1";
                                             datos[27]=cell.toString();
                                        }
                                    }
                                    
                            }
                                 
                                   
                                   
                                    String clabe_int= convertirClabe(datos[1], banco);

                                    if (!clabe_int.equals(cambiarClabe(clabe))) {
                                        throw new Exception("Las Clabe Ingresada no es la misma Clabe que contiene el archivo");
                                    }

                                    

                                     numero_registros++;
                                    System.out.println(Integer.toString(numero_registros)+  " -- Fecha: " + datos[0] + " Descripción: " + datos[1] + " Depósitos: " + datos[2] +
                                            " Retiros: " + datos[3] + " Saldo: " + datos[4] +
                                            " Tmov: " + datos[5] + " Monto: " + datos[6] +
                                            " Numerico nref: " + datos[7] + " Alfanumerico cref: " + datos[8] +
                                            " Autorizacion : " + datos[9] + " Cuenta: " + " Clabe: " + clabe);

                                    qry("select nextval('pk_dregis') conse");
                                             String regb = icuso.dbx.JSTblgetvCampo("conse").toString();
                                    /* 
                                    // header
                                    eje = "insert into tdregis( regis, dregis, auxi , cnta, tmov,  monto , ldregis, feceven, fndo, crefer, item, even, cntap) values(?,?,?,?,?,?,?,?,?,?,?,?,?) ";
                                    nregis = ejePs(eje, new String[] {
                                            regis, "N",
                                            regb, "N",
                                            clabe, "C",
                                            "11121", "C",
                                            datos[28], "N", //tmov
                                            datos[27], "N", //monto
                                            "", "C", //descrip
                                            datos[26], "F", //fecha
                                            fndo, "N",     //fndo
                                            datos[18], "C", //crefer
                                            regb, "N",//IDREF
                                            regis, "N",//SOLICITUD
                                             "8121", "N"

                                    });*/

                                    //
                                        eje = " insert into tdetlayout(archi, fecha, fndo, item, tmov, monto, crefer, nrefer, clabe, banco, descrip, linea)\n" + 
                                                "values(?,?,?,?,?,?,?,?,?,?,?,?) ";
                                        nregis = ejePs(eje, new String[] {
                                            archi, "N",
                                            datos[26], "F", //fecha
                                            fndo, "N",     //fndo
                                            regb, "N",     //item
                                            datos[28], "N", //tmov
                                            datos[27], "N", //monto
                                            datos[18], "C", //crefer
                                            "", "N",  //nrefer
                                            clabe, "C",
                                            banco, "C",
                                            "", "C", //descrip
                                            datos[0]+"|"+datos[1]+"|"+datos[2]+"|"+datos[3]+"|"+datos[4]+"|"+datos[5]+"|"+datos[6]+"|"+datos[7]+"|"+datos[8]+"|"+datos[9]+"|"+
                                            datos[10]+"|"+datos[11]+"|"+datos[12]+"|"+datos[13]+"|"+datos[14]+"|"+datos[15]+"|"+datos[16]+"|"+datos[17]+"|"+datos[18]+"|"+datos[19]+"|"+
                                            datos[20]+"|"+datos[21]+"|"+datos[22]+"|"+datos[23]+"|"+datos[24]+"|"+datos[25]+"|"+datos[26]+"|"+datos[27]+"|"+datos[28]
                                            , "C"//linea          

                                        });
                                     

                           

                        
                    }
                      nrow++;
           }
          
           System.out.println("Total de registros leidos: "+numero_registros);
    
    }

    public static String notacionci(double número) {
        String d = "####################################";
        return new DecimalFormat("#." + d + d + d).format(número);
    }

    // Afirme
    public void layout062(String archivop) throws Exception {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

     archivo = new File (archivop);
     fr = new FileReader (archivo);
     br = new BufferedReader(fr);
     
      /*
                                datos[0]=Concepto
                                datos[1]=Fecha (DD/MM/AA)
                                datos[2]=Referencia
                                datos[3]=Cargo
                                datos[4]=Abono
                                datos[5]=Saldo
                                datos[6]=Cuenta
                                datos[7]=Código
                                datos[8]=No. Secuencia
                                datos[9]=Monto
                                datos[10]=tmov
                                datos[11]=Concepto
                                monto
                                tmov
                                concepto

     */
    
     // Lectura del fichero
     String linea;
     
     int numero_registros=0;
      SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yy");
       SimpleDateFormat formato1 = new SimpleDateFormat("dd/MM/yyyy");
     while((linea=br.readLine())!=null){
                        numero_registros++;
                        
                        String datos[]=linea.split(",");
                        String monto="";
                        String tmov="";
                        String concepto;
                        
                        if(numero_registros>1){
                            //datos[0]
                            boolean caracter = ( datos[0].matches("[A-Z].*") || datos[0].matches("[a-z].*"));

                            if(caracter){
                                datos[0]="";
                                concepto=datos[0];
                            }else{
                                concepto=datos[0];
                            }
                            
                            //datos[1]
                            datos[1]=datos[1].trim();//  formatter.formatCellValue(cell, evaluator);
                            datos[1]=datos[1].replace("'", "").trim();
                            Date fecha = formato.parse(datos[1]);
                            datos[1]=formato1.format(fecha);

                            
                            //datos[3]
                            if(!datos[3].equals("0.00")){
                                tmov="-1";
                                monto=datos[3];
                                
                            }
                            //datos[4]
                            if(!datos[4].equals("0.00")){
                                tmov="1";
                                monto=datos[4];
                                
                            }
                            //datos[6]
                            double res=Double.parseDouble(datos[6]);
                            datos[6]=notacionci(res);
                            
                        
                        
                                System.out.println("crefer: "+datos[0]+" Fecha (DD/MM/AA): "+datos[1]+" Referencia:  "+datos[2]+
                                " Cargo: "+datos[3]+" Abono: "+datos[4]+
                                " Saldo: "+datos[5]+" Cuenta : "+datos[6]+
                                " Código: "+datos[7]  +" No. Secuencia: "+datos[8]+
                                " Monto: "+monto  +" tmov: "+tmov +" concepto: "+concepto

                                );

                                qry("select nextval('pk_dregis') conse");
                                String regb = icuso.dbx.JSTblgetvCampo("conse").toString();
                               
                             eje = " insert into tdetlayout(archi, fecha, fndo, item, tmov, monto, crefer, nrefer, clabe, banco, descrip, linea)\n" + 
                                        "values(?,?,?,?,?,?,?,?,?,?,?,?) ";
                                nregis = ejePs(eje, new String[] {
                                    archi, "N",
                                    datos[1], "F", //fecha
                                    fndo, "N",     //fndo
                                    regb, "N",     //item
                                    tmov, "N", //tmov
                                    monto, "N", //monto
                                    datos[0], "C", //crefer
                                    "", "N",  //nrefer
                                    clabe, "C",
                                    banco, "C",
                                    concepto, "C", //descrip
                                    linea,"C"// linea
                                      

                                });

        }
         
          
     }
       
     System.out.println("Total de registros leidos: "+numero_registros);
  
  
     // En el finally cerramos el fichero, para asegurarnos
     // que se cierra tanto si todo va bien como si salta 
     // una excepcion.
     try{                    
        if( null != fr ){   
           fr.close();     
        }                  
     }catch (Exception e2){ 
        e2.printStackTrace();
     }
     
     
    
}
    // Banorte
    public void layout072(String archivo) throws Exception {
        String rutaArchivoExcel = archivo;
        FileInputStream inputStr = new FileInputStream(rutaArchivoExcel);
        XSSFWorkbook xssfWork = new XSSFWorkbook(inputStr) ;
        XSSFSheet sheet1 = xssfWork.getSheetAt(0);
        int numero_registros=0;
        int nrow=1;
       
            DataFormatter formatter = new DataFormatter();
            FormulaEvaluator evaluator = xssfWork.getCreationHelper().createFormulaEvaluator();
            boolean salto=true;
           //barremos las filas
           for(Row row : sheet1){
               
                    if(nrow>=2){// no entra a encabezados
                        
                        org.apache.poi.ss.usermodel.Cell cell1 = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        if(!cell1.toString().equals("")){
                            
                                /*
                                datos[0]=CUENTA
                                datos[1]=FECHA DE OPERACIÓN
                                datos[2]=FECHA
                                datos[3]=REFERENCIA
                                datos[4]=DESCRIPCIÓN
                                datos[5]=COD. TRANSAC
                                datos[6]=SUCURSAL
                                datos[7]=DEPÓSITOS -- cargos
                                datos[8]=RETIROS -- abono
                                datos[9]=SALDO
                                datos[10]=MOVIMIENTO
                                datos[11]=DESCRIPCIÓN DETALLADA
                                datos[12]=CHEQUE
                                datos[13]=MONTO
                                datos[14]=TMOV

                                */

                                        String[] datos = new String[15];
                                         for(int cn=0; cn<row.getLastCellNum(); cn++) {
                                                 org.apache.poi.ss.usermodel.Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                                 String value="";
                                                 
                                                 switch (cn) { 
                                                    case 0://crefer
                                                        value=cell.toString().replace("'", "").trim();
                                                        

                                                    break;
                                                    case 1://fecha
                                                        
                                                      value=obtenerFechaFormateada(formatter.formatCellValue(cell, evaluator), "dd/MM/yyyy");//formato de fecha
                                                         

                                                     break;
                                                     case 2://fecha
                                                        
                                                      value=obtenerFechaFormateada(formatter.formatCellValue(cell, evaluator), "dd/MM/yyyy");//formato de fecha
                                                         

                                                     break;
                                                    case 7 :
                                                        if(!cell.toString().equals("0")){
                                                            datos[14]="1";
                                                             datos[13]=cell.toString();
                                                        }

                                                     break;
                                                    case 8 :
                                                        if(!cell.toString().equals("-")){
                                                            datos[14]="-1";
                                                             datos[13]=cell.toString();
                                                        }

                                                     break;
                                                    default:
                                                     // Default secuencia de sentencias.
                                                        value=  cell.toString();
                                                  }
                                                 
                                                 
                                                  datos[cn]=value;

                                         }
                                         
                                         numero_registros++;

                                        String clabe_int= convertirClabe(datos[0], banco);

                                        if (!clabe_int.equals(cambiarClabe(clabe))) {
                                            throw new Exception("Las Clabe Ingresada no es la misma Clabe que contiene el archivo");
                                        }

                                       

                                        
                                        System.out.println("CUENTA: "+datos[0]+" FECHA DE OPERACIÓN: "+datos[1]+" FECHA:  "+datos[2]+
                                        " REFERENCIA: "+datos[3]+" DESCRIPCIÓN: "+datos[4]+
                                        " COD. TRANSAC: "+datos[5]+" SUCURSAL : "+datos[6]+
                                        " DEPÓSITOS: "+datos[7]  +" RETIROS: "+datos[8]+
                                        " SALDO: "+datos[9]  +" MOVIMIENTO: "+datos[10] +
                                        " DESCRIPCIÓN DETALLADA: "+datos[11]  +" CHEQUE: "+datos[12] +
                                        " MONTO: "+datos[13] +" TMOV: "+datos[14]

                                        );

                                        qry("select nextval('pk_dregis') conse");
                                        String regb = icuso.dbx.JSTblgetvCampo("conse").toString();
                                        /* 
                                       // header
                                       eje = "insert into tdregis( regis, dregis, auxi , cnta, tmov,  monto , ldregis, feceven, fndo, crefer, item, even, cntap) values(?,?,?,?,?,?,?,?,?,?,?,?,?) ";
                                       nregis = ejePs(eje, new String[] {
                                               regis, "N",
                                               regb, "N",
                                               clabe, "C",
                                               "11121", "C",
                                               datos[14], "N", //tmov
                                               datos[13], "N", //monto
                                               datos[11], "C", //descrip
                                               datos[1], "F", //fecha
                                               fndo, "N",     //fndo
                                               datos[3], "C", //crefer
                                               regb, "N",//IDREF
                                               regis, "N",//SOLICITUD
                                                "8121", "N"

                                       });
                                       */
                                      eje = " insert into tdetlayout(archi, fecha, fndo, item, tmov, monto, crefer, nrefer, clabe, banco, descrip, linea)\n" + 
                                                "values(?,?,?,?,?,?,?,?,?,?,?,?) ";
                                        nregis = ejePs(eje, new String[] {
                                            archi, "N",
                                            datos[1], "F", //fecha
                                            fndo, "N",     //fndo
                                            regb, "N",     //item
                                            datos[14], "N", //tmov
                                            datos[13], "N", //monto
                                            datos[3], "C", //crefer
                                            "", "N",  //nrefer
                                            clabe, "C",
                                            banco, "C",
                                            datos[11], "C", //descrip
                                            datos[0]+"|"+datos[1]+"|"+datos[2]+"|"+datos[3]+"|"+datos[4]+"|"+datos[5]+"|"+datos[6]+"|"+datos[7]+"|"+datos[8]+"|"+datos[9]+"|"+
                                            datos[10]+"|"+datos[11]+"|"+datos[12]+"|"+datos[13]+"|"+datos[14]
                                            , "C"//linea          

                                        });
                           
                        }
                        
                    }
                      nrow++;
           }
          
           System.out.println("Total de registros leidos: "+numero_registros);

    } 

    // Bancomer BBVA
    public void layout012(String archivo) throws Exception {
        String rutaArchivoExcel = archivo;
            FileInputStream inputStr = new FileInputStream(rutaArchivoExcel);
            XSSFWorkbook xssfWork = new XSSFWorkbook(inputStr) ;
            XSSFSheet sheet1 = xssfWork.getSheetAt(0);
            int numero_registros=0;
            SimpleDateFormat formato_fecha = new SimpleDateFormat("dd/MM/yyyy");

            int nrow=1;
            String clabep="";
           
                DataFormatter formatter = new DataFormatter();
                FormulaEvaluator evaluator = xssfWork.getCreationHelper().createFormulaEvaluator();
                boolean detalle=false;
                
               //barremos las filas
               for(Row row : sheet1){
                   
                   org.apache.poi.ss.usermodel.Cell cell_ = row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    
                    if(cell_.toString().contains("Cuenta:")){
                     
                        
                        String separar[]=cell_.toString().split("Cuenta:");
                        clabep= convertirClabe(separar[1].trim(), banco);
                        
                    }
                     
                        if(detalle){// no entra a encabezados
                           
                           
                                    /*
                                    datos[0]=Fecha de operación
                                    datos[1]=Fecha valor
                                    datos[2]=Concepto
                                    datos[3]=Cargo
                                    datos[4]=Abono
                                    datos[5]=Saldo
                                    datos[6]=Montto
                                    datos[7]=tmov
                                

                                    */

                                            String[] datos = new String[8];
                                             for(int cn=0; cn<row.getLastCellNum(); cn++) {
                                                     org.apache.poi.ss.usermodel.Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                                      
                                                     if(!detalle){
                                                            break;
                                                      }
                                                     
                                                     String value="";
                                                     
                                                     switch (cn) { 
                                                        case 0://fecha
                                                            
                                                            if(cell.toString().equals("")){
                                                                detalle=false;
                                                                break;
                                                            }
                                                           
                                                            value = formato_fecha.format(cell.getDateCellValue());
                                                            
                                                         break;
                                                        case 1://fecha
                                                          
                                                            value = formato_fecha.format(cell.getDateCellValue());

                                                         break;
                                                        case 3 :
                                                            if(!cell.toString().equals("")){
                                                                datos[7]="-1";
                                                                 datos[6]=cell.toString();
                                                            }

                                                         break;
                                                        case 4 :
                                                            if(!cell.toString().equals("")){
                                                                datos[7]="1";
                                                                 datos[6]=cell.toString();
                                                            }

                                                         break;
                                                        default:
                                                         // Default secuencia de sentencias.
                                                            value=  cell.toString();
                                                      }
                                                     
                                                     
                                                      datos[cn]=value;

                                             }

                                             if(!detalle){
                                                break;
                                             }
                                             numero_registros++;

                                             String clabe_int= clabep;
                                             if (!clabe_int.equals(cambiarClabe(clabe))) {
                                                    throw new Exception("Las Clabe Ingresada no es la misma Clabe que contiene el archivo");
                                              }

                                           

                                            
                                            System.out.println("Fecha de operación: "+datos[0]+" Fecha valor: "+datos[1]+" Concepto:  "+datos[2]+
                                            " Cargo: "+datos[3]+" Abono: "+datos[4]+
                                            " Saldo: "+datos[5]+" Monto : "+datos[6]+
                                            " Tmov: "+datos[7] 

                                            );

                                            if(datos[6].equals("-152.46")){
                                                String hola="";
                                            }

                                            

                                             qry("select nextval('pk_dregis') conse");
                                             String regb = icuso.dbx.JSTblgetvCampo("conse").toString();
                                              /* 
                                            // header
                                            eje = "insert into tdregis( regis, dregis, auxi , cnta, tmov,  monto , ldregis, feceven, fndo, crefer, item, even, cntap) values(?,?,?,?,?,?,?,?,?,?,?,?,?) ";
                                            nregis = ejePs(eje, new String[] {
                                                    regis, "N",
                                                    regb, "N",
                                                    clabe, "C",
                                                    "11121", "C",
                                                    datos[7], "N", //tmov
                                                    datos[6], "N", //monto
                                                    datos[2], "C", //descrip
                                                    datos[0], "F", //fecha
                                                    fndo, "N",     //fndo
                                                    "", "C", //crefer
                                                    regb, "N",//IDREF
                                                    regis, "N",//SOLICITUD
                                                     "8121", "N"

                                            });*/

                                            eje = " insert into tdetlayout(archi, fecha, fndo, item, tmov, monto, crefer, nrefer, clabe, banco, descrip, linea)\n" + 
                                                "values(?,?,?,?,?,?,?,?,?,?,?,?) ";
                                          nregis = ejePs(eje, new String[] {
                                            archi, "N",
                                            datos[0], "F", //fecha
                                            fndo, "N",     //fndo
                                            regb, "N",     //item
                                            datos[7], "N", //tmov
                                            datos[6], "N", //monto
                                            "", "C", //crefer
                                            "", "N",  //nrefer
                                            clabe, "C",
                                            banco, "C",
                                            datos[2], "C", //descrip
                                            datos[0]+"|"+datos[1]+"|"+datos[2]+"|"+datos[3]+"|"+datos[4]+"|"+datos[5]+"|"+datos[6]+"|"+datos[7], "C"//linea          

                                        });
                               
                            
                            
                        }
                        
                    if(cell_.toString().contains("Abono")){
                        detalle=true;
                    }
                          nrow++;
               }
              
               System.out.println("Total de registros leidos: "+numero_registros);
    }

    // Inverlat Scotiabank
    public void layout044(String narchivo) throws Exception {
            File archivo = null;
            FileReader fr = null;
            BufferedReader br = null;

      
         // Apertura del fichero y creacion de BufferedReader para poder
         // hacer una lectura comoda (disponer del metodo readLine()).
         archivo = new File (narchivo);
         fr = new FileReader (archivo);
         br = new BufferedReader(fr);
         
          /*
            datos[0]=Fecha 
            datos[1]=Monto
            datos[2]=tipo
            datos[3]=detalle
            datos[4]=crefer
            datos[5]=tmov
            datos[6]=clabe
                             
           */

        String[] datos = new String[8];
         // Lectura del fichero
         String linea;
         int conta=0;
         while((linea=br.readLine())!=null){
             
            datos[0]=linea.substring(26, 36);
            String fechap[]=datos[0].split("/");
             
            datos[0] = fechap[2]+"/"+fechap[1]+"/"+fechap[0];
            datos[1]=linea.substring(46, 63);
            datos[2]=linea.substring(63, 68);
            datos[3]=linea;
            datos[4]=linea.substring(174, 194);
           
             
            boolean caracter = ( datos[4].matches("[A-Z].*") || datos[4].matches("[a-z].*"));                                             
            if(caracter){
                datos[4]="";
            }
           
            if(datos[2].equals("Abono")){
                 datos[5]="1";
            }else{
                 datos[5]="-1";
            }
            
             datos[6]=linea.substring(15, 26);
             
             System.out.println("Fecha: "+datos[0]+" Monto: "+datos[1]+" Tipo:  "+datos[2]+
                                             " tmov: "+datos[5]+" crefer: "+datos[4]+
                                             " clabe: "+ datos[6] +
                                             " detalle: "+datos[3] 

                                            );

                String clabe_int= convertirClabe(datos[6], banco);
                if (!clabe_int.equals(cambiarClabe(clabe))) {
                    throw new Exception("Las Clabe Ingresada no es la misma Clabe que contiene el archivo");
                }
                

              qry("select nextval('pk_dregis') conse");
              String regb = icuso.dbx.JSTblgetvCampo("conse").toString();
                    /* 
                // header
                eje = "insert into tdregis( regis, dregis, auxi , cnta, tmov,  monto , ldregis, feceven, fndo, crefer, item, even, cntap) values(?,?,?,?,?,?,?,?,?,?,?,?,?) ";
                nregis = ejePs(eje, new String[] {
                        regis, "N",
                        regb, "N",
                        clabe, "C",
                        "11121", "C",
                        datos[5], "N", //tmov
                        datos[1], "N", //monto
                        datos[3], "C", //descrip
                        datos[0], "F", //fecha
                        fndo, "N",     //fndo
                        datos[4], "C", //crefer
                        regb, "N",//IDREF
                        regis, "N",//SOLICITUD
                         "8121", "N"

                });
                */
                  eje = " insert into tdetlayout(archi, fecha, fndo, item, tmov, monto, crefer, nrefer, clabe, banco, descrip, linea)\n" + 
                                                "values(?,?,?,?,?,?,?,?,?,?,?,?) ";
                        nregis = ejePs(eje, new String[] {
                        archi, "N",
                        datos[0], "F", //fecha
                        fndo, "N",     //fndo
                        regb, "N",     //item
                        datos[5], "N", //tmov
                        datos[1], "N", //monto
                        datos[4], "C", //crefer
                        "", "N",  //nrefer
                        clabe, "C",
                        banco, "C",
                        datos[3], "C", //descrip
                        linea, "C"//linea          

                    });

                conta++;
         }
            br.close();
            System.out.println("Total de registros leidos: "+conta);

         // En el finally cerramos el fichero, para asegurarnos
                    // que se cierra tanto si todo va bien como si salta 
                    // una excepcion.
                    try{                    
                        if( null != fr ){   
                        fr.close();     
                        }                  
                    }catch (Exception e2){ 
                        e2.printStackTrace();
                    }
    }

    String obtenerFechaFormateada(String fechap, String formato) {
        long fecha = Date.parse(fechap);
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        return sdf.format(fecha);
    }

    public static String EliminarNotacionCientífica(double número) {

        return new DecimalFormat("#.####################################").format(número);
    }

      

    public String convertirClabe(String dato, String banco) {
        int tama_cuenta = dato.length();
        while (tama_cuenta < 11) {
            dato = "0" + dato;
            tama_cuenta++;
        }
        
        if(dato.length()==14){
            dato=  banco  + "XXX"+ dato.substring(3) + "X";
        }else{
            dato = banco + "XXX" + dato + "X";
        }

        return dato;
    }

    public String eliminar_0_izquierda(String dato) {
        int tam = dato.length();
        int inicial = 0;
        for (int x = 0; x < tam; x++) {
            char a = dato.charAt(x);

            if (a != '0') {
                inicial = x;
                break;
            }

        }

        return dato = dato.substring(inicial, tam);

    }

    public String cambiarClabeBanamex(String clabe) {
        int tam = clabe.length();
        
        String resul="";
        String pase="";
        for (int x = 0; x < tam; x++) {
            char a = clabe.charAt(x);

            if(x>=3&& x<=5){
                pase="X";
            }else{

                if(x==17){
                    pase="X";
               }else{
                   pase=a+"";
               }
               
               if(x>=6&&x<=9){
                    pase="0";
               }
                
            }

            resul=resul+pase;

         }

        return resul;

    }

       public String cambiarClabe(String clabe) {
        int tam = clabe.length();
        
        String resul="";
        String pase="";
        for (int x = 0; x < tam; x++) {
            char a = clabe.charAt(x);

            if(x>=3&& x<=5){
                pase="X";
            }else{

                if(x==17){
                     pase="X";
                }else{
                    pase=a+"";
                }
                
            }

            resul=resul+pase;

         }

        return resul;

    }

    public String validarClabes(String clabe) throws Exception {
        String clabes = "";

        qry("SELECT count(*) vali FROM tfondos where clabe is not null and CONCAT(substring(clabe from 1 for 3), substring(clabe from 7 for 11))='"
                + clabe + "'");
        String dato = icuso.dbx.JSTblgetvCampo("vali").toString();

        if (dato.equals("0")) {
            clabes = clabe + ",\n";
        }

        return clabes;
    }

    public String convertirClabe(String clabe) {

        int conta = clabe.length();

        for (int x = conta; x < 18; x++) {
            clabe = "0" + clabe;
        }

        return clabe;
    }

    public void fin() throws Exception {
        super.fin();
    }

    public void repo() throws Exception {

                String archip = icuso.inData.getString("archi");


                //contable
                eje = ""+
                        "select \n" + //
    " row_number() over() as id,\n" + //
    " cnta, lcnta, auxi, ldregis,\n" + //
    " sum(cargo) cargo,\n" + //
    " sum(abono) abono\n" + //
    " from ( \n" + //
    " select  tmov, \n" + //
    " case when tmov=1 then monto else 0 end cargo,\n" + //
    " case when tmov=-1 then monto else 0 end abono,\n" + //
    " case when t.cnta like '4%' then '-4'else t.cnta end cnta,\n" + //
    " case when t.cnta like '4%' then 'INGRESOS Y OTROS BENEFICIOS' else t.lcnta end lcnta,\n" + //
    " d.auxi,\n" + //
    " ' ' ldregis\n" + //
    " from tdregis d \n" + //
    " left join tcntas t on trim(d.cnta)=trim(t.cnta)\n" + //
    " where regis= " +archip+ //
    " and signo !=0 \n" + //
    " )g group by cnta, lcnta, auxi, ldregis, tmov order by tmov  "
                        + "";
                // tabla 2 hoja 3
                jsResult = ejeQry(eje);

               


                qry(" SELECT to_char(current_timestamp, 'dd/mm/yyyy HH12:MI:SS') actual");
                String actual = icuso.dbx.JSTblgetvCampo("actual").toString();

                String nombre1 = icuso.inData.getString("nom");

                

                 qry(" select descrip,  to_char(fecha,'dd/mm/yyyy') fecha   from tregis where regis="+archip);
                String concepto = icuso.dbx.JSTblgetvCampo("descrip").toString();
                String fecha = icuso.dbx.JSTblgetvCampo("fecha").toString();

                String tipo="INGRESOS";
                String poliza="";

                jsResult.put("nombre", "Reporte");
                jsResult.put("concepto", concepto);
                jsResult.put("poliza", poliza);
                jsResult.put("fecha", fecha);
                jsResult.put("actual", actual);
                jsResult.put("firma1", nombre1);
                jsResult.put("regis", archip);
                jsResult.put("tipo", tipo);
        

            

                

                icuso.outData.put("tabla", jsResult);

    }

}