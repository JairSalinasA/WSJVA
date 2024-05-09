package cusos.salban;

import comun.bkComun;
import srv.cuso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;

public class sal extends bkComun {
    private String banco, archi, clabe, regis;

    public sal(cuso inCuso) throws Exception {
        super(inCuso);
        kComa = "'";
    }

    public void lis() throws Exception {
        JSONArray jer = new JSONArray();
        String fecha;
        String fep[]=icuso.inData.get("fecha").toString().split("-");
        fecha=fep[2].substring(0,2)+"/"+fep[1]+"/"+fep[0];
        jer.put(new Object[] { ""
                + "<i>Id: </i>"
                + icuso.inData.get("archi")
                + "&nbsp;&nbsp;&nbsp;"
                + icuso.inData.get("bnco")+" - "+icuso.inData.get("lbnco")
                + "&nbsp;&nbsp;&nbsp;"
                +fecha

        });

        icuso.outData.put("jerq", jer);
        jsVals.put("archi", icuso.inData.get("archi"));
        jsVals.put("bnco", icuso.inData.get("bnco"));
        jsVals.put("lbnco", icuso.inData.get("lbnco"));
        jsVals.put("fecha", icuso.inData.get("fecha"));
       
        super.lis();
    }


    public void cap() throws Exception {

        jsVals.put("archi", icuso.inData.get("archi"));
        jsVals.put("clabe", icuso.inData.get("clabe"));
        jsVals.put("bnco", icuso.inData.get("bnco"));
            super.cap();
    }

     public void borrar() throws Exception {
        String regis = icuso.inData.getString("regis");
        archi = icuso.inData.getString("archi");
        clabe = icuso.inData.getString("clabe");

        eje = "delete from tarchivos where archi=?";
        nregis = ejePs(eje, new String[] { regis, "N" });

        eje = "delete from tdregis where regis=?";
        nregis = ejePs(eje, new String[] { regis, "N" });

        eje = "delete from tregis where regis=?";
        nregis = ejePs(eje, new String[] { regis, "N" });

         // header
        eje = "update tsalbandia set regis=null where archi=? and clabe=? ";
        nregis = ejePs(eje, new String[] { archi, "N", clabe,"C" });

    }

    public void graba() throws Exception {  

        String dml = icuso.inData.getString("dml");
         clabe = icuso.inData.getString("clabe");


        if (dml.equals("2")) {// inserta

            banco = icuso.inData.getString("bnco");
            String url = icuso.inData.getString("url");

            String dirfis = "/srv/UploadFile/archis/"; // Donde están los Archivos
            if (url.contains("localhost"))
                // dirfis= "/Users/christianmendoza/Documents/Warrior/Proyectos git/nuevos archivos excel/Saldos Bancos/"
                dirfis = "/Users/adolfosalazarvargas/Desarrollo/srvarchinode/archis/";
            dirfis = dirfis + url.split("archis/")[1];
           
            eje = "getdetail" + banco;
            Method metodo = this.getClass().getDeclaredMethod(eje, String.class);
            metodo.invoke(this, dirfis);

        }

        if (dml.equals("3")) {// eliminar
            borrar();
            icuso.outData.put("dml", "2");
        }

        eje = "select * from (" + icuso.ponInData(prop.getProperty("lis")) + ") lisQ where clabe='"
                + icuso.inData.getString("clabe") + "'";
        jsResult = ejeQry(eje, new String[] {});

        icuso.outData.put("dml", "2");

    }

    // Afirme
    public void getdetail062(String archivo) throws Exception {
               String rutaArchivoExcel = archivo;
            FileInputStream inputStr = new FileInputStream(rutaArchivoExcel);
            XSSFWorkbook xssfWork = new XSSFWorkbook(inputStr) ;
            XSSFSheet sheet1 = xssfWork.getSheetAt(0);
            int numero_registros=0;
            int nrow=1;
           
                DataFormatter formatter = new DataFormatter();
                FormulaEvaluator evaluator = xssfWork.getCreationHelper().createFormulaEvaluator();
               //barremos las filas
               for(Row row : sheet1){
                   
                        if(nrow>=2){// no entra a encabezados
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
                                datos[10]=monto
                                */
                                
                               String[] datos = new String[13];
                                for(int cn=0; cn<row.getLastCellNum(); cn++) {
                                        org.apache.poi.ss.usermodel.Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                        String value;
                                        if(cn>=3 && cn<=5){
                                            
                                           double res=Double.parseDouble(cell.toString());
                                           value=EliminarNotacionCientífica(res);
                                           
                                        }else{
                                             value=  formatter.formatCellValue(cell, evaluator);
                                        }
                                        
                                        datos[cn]=value;
                                }
                                
                                if(datos[3].equals("0")){
                                    datos[9]="-1";
                                    datos[10]=datos[4];
                                }else{
                                    datos[9]="1";
                                    datos[10]=datos[3];
                                }
                                
                                if(datos[0].equals("")){
                                    break;
                                }
                                
                                datos[5]=datos[5].replace("$", "").replace(",", "").replace("-", "");
                                datos[1]=obtenerFechaFormateada(datos[1], "dd/MM/yyyy");//formato de fecha
                                datos[6]=convertirClabe(datos[6],"062");

                                if(!datos[6].equals(clabe)){
                                    throw new Exception("La clabe seleccionada no es la misma del archivo");
                                }
                                
                                
                                numero_registros++;
                                System.out.println("Concepto: "+datos[0]+" Fecha (DD/MM/AA): "+datos[1]+" Referencia: "+datos[2]+
                                " Cargo: "+datos[3]+" Abono: "+datos[4]+" Saldo: "+datos[5]+
                                " Cuenta: "+datos[6]+" Código: "+datos[7]+" No. Secuencia: "+datos[8]+
                                " Tmov: "+datos[9]+" Monto: "+datos[10]);

                                String fec[]=datos[1].split("/");
                                String dia=fec[0];
                                String mes=fec[1];
                                String anio=fec[2];

                                //insertamos el header en tregis
                                if(nrow==2){
                                        
                                        //regis=archi detalle
                                        qry("select nextval('pk_dregis') conse");
                                         regis = icuso.dbx.JSTblgetvCampo("conse").toString();

                                                // header
                                        eje = "insert into tarchivos (archi ,url, tarchi, larchi,fecha ) values(?,?,?,?,now()) ";
                                        nregis = ejePs(eje, new String[] {
                                            regis, "N",
                                            archivo, "C",
                                            "6", "N",
                                            banco, "C"
                                            });

                                        // header
                                        eje = "insert into tregis ( regis, teven, descrip ,fecha, refer, clabe ) values(?,?,?,?,?,?) ";
                                        nregis = ejePs(eje, new String[] {
                                            regis, "N",
                                            "15", "N", 
                                            "Ingresos Bancos", "C", 
                                            datos[1],"F",
                                            clabe, "C", 
                                            clabe, "C" 
                                        });
                                        archi = icuso.inData.getString("archi");
                                        // header
                                        eje = "update tsalbandia set regis=? where archi=? and clabe=? ";
                                        nregis = ejePs(eje, new String[] {regis, "N", archi, "N", clabe,"C" });


                                }
                                

                                 qry("select nextval('pk_dregis') conse");
                                String regb = icuso.dbx.JSTblgetvCampo("conse").toString();

                                // header
                            eje = "insert into tdregis( regis, dregis, auxi , cnta, per, ejer, tmov,  monto , ldregis) values(?,?,?,?,?,?,?,?,?) ";
                            nregis = ejePs(eje, new String[] {
                                regis, "N", 
                                regb, "N", 
                                clabe, "C", 
                                "11121", "C", 
                                mes, "N", 
                                anio, "N",  
                                datos[9], "N", 
                                datos[10], "N", 
                                datos[0], "C"
                            });
                            
                            
                        }
                          nrow++;
               }
              
               System.out.println("Total de registros leidos: "+numero_registros);
    }

    

    public void getDetalleSantander(File archivo, String banco, String archi, String clabe, String url) throws Exception {


        qry("select nextval('pk_dregis') conse");
        String archidet = icuso.dbx.JSTblgetvCampo("conse").toString();

        // header
        eje = "insert into tarchivos (archi ,url, tarchi, larchi,fecha ) values(?,?,?,?,now()) ";
        nregis = ejePs(eje, new String[] {
            archidet, "N",
            url, "C",
            "6", "N",
            banco, "C"
            });

        eje = "update tsalbandia set archidet=? where clabe=? and archi=? ";
        nregis = ejePs(eje, new String[] {
            archidet, "N",
            clabe, "C",
            archi, "N"
            });


            qry(" select to_char(fecha,'YYYY/MM/DD') fec from thsalbandia t  where archidet ="+archi);
            String fech = icuso.dbx.JSTblgetvCampo("fec").toString();



        FileReader fr = null;
        BufferedReader br = null;

        fr = new FileReader (archivo);
        br = new BufferedReader(fr);

        // Lectura del fichero
        String linea;

        while((linea=br.readLine())!=null){
                qry("select nextval('pk_dregis') conse");
                String regb = icuso.dbx.JSTblgetvCampo("conse").toString();
                String clabearchi=linea.substring(0, 11);

                        
                if(!clabearchi.equals(clabe)){
                    throw new Exception("La clabe no es igual a la del archivo");
                }

              
                        
                        String descrip=linea.substring(11,47);
                        descrip=descrip.trim();
                        String tmov=linea.substring(72, 73);

                        String fechap=descrip.substring(0, 8);
                        String dia=fechap.substring(0, 2);
                        String mes=fechap.substring(2, 4);
                        String anio=fechap.substring(4, 8);
                        String fecha=anio+"/"+mes+"/"+dia;

                        if (fech.equals(fecha)){

                            if(tmov.equals("-")){
                                tmov="-1";
                            }else{
                                tmov="1";
                            }
    
    
                            String montop = linea.substring(73,86);
                            String monto=montop.substring(0,11)+"."+montop.substring(11,13);
                
                            // header
                            eje = "insert into tregbnco (regb, archi, clabe ,dia, mes, anio, tmov,  monto , texto, descrip  ) values(?,?,?,?,?,?,?,?,?,?) ";
                            nregis = ejePs(eje, new String[] {regb, "N", archidet, "N",  clabe, "C", dia, "N", mes, "N", anio, "N",  tmov, "N", monto, "N", linea, "C", descrip, "C" });
                            
                

                        }
                        



            
        }

     

         fr.close();   
         br.close(); 
         System.out.println(linea);
    }


    public void getDetalleSCOTIABANK(File archivo, String banco, String archi, String clabe, String url) throws Exception {


        qry("select nextval('pk_dregis') conse");
        String archidet = icuso.dbx.JSTblgetvCampo("conse").toString();

        // header
        eje = "insert into tarchivos (archi ,url, tarchi, larchi,fecha ) values(?,?,?,?,now()) ";
        nregis = ejePs(eje, new String[] {
            archidet, "N",
            url, "C",
            "6", "N",
            banco, "C"
            });

        eje = "update tsalbandia set archidet=? where clabe=? and archi=? ";
        nregis = ejePs(eje, new String[] {
            archidet, "N",
            clabe, "C",
            archi, "N"
            });


            qry(" select to_char(fecha,'YYYY/MM/DD') fec from thsalbandia t  where archidet ="+archi);
            String fech = icuso.dbx.JSTblgetvCampo("fec").toString();



        FileReader fr = null;
        BufferedReader br = null;

        fr = new FileReader (archivo);
        br = new BufferedReader(fr);

        // Lectura del fichero
        String linea;

        while((linea=br.readLine())!=null){
                qry("select nextval('pk_dregis') conse");
                String regb = icuso.dbx.JSTblgetvCampo("conse").toString();
                String fecha=linea.substring(26, 36);

                if (fech.equals(fecha)){
                        String fec[]=fecha.split("/");

                        String dia = fec[2];
                        String mes = fec[1];
                        String anio = fec[0];
                        String descrip=linea.substring(88,173);

                        String tmov=linea.substring(63, 68);

                        if(tmov.equals("Abono")){
                            tmov="-1";
                        }else{
                            tmov="1";
                        }

                        String clabearchi=linea.substring(16, 26);

                        clabearchi=banco+clabearchi;

                        qry("select  substring('"+clabe+"' from 1 for 13) cuenta");
                        String clabeb = icuso.dbx.JSTblgetvCampo("cuenta").toString();



                        if(!clabearchi.equals(clabeb)){
                            throw new Exception("La clabe no es igual a la del archivo");
                        }
                        

                        String montop = linea.substring(46,63);
            
                        // header
                        eje = "insert into tregbnco (regb, archi, clabe ,dia, mes, anio, tmov,  monto , texto, descrip  ) values(?,?,?,?,?,?,?,?,?,?) ";
                        nregis = ejePs(eje, new String[] {regb, "N", archidet, "N",  clabe, "C", dia, "N", mes, "N", anio, "N",  tmov, "N", montop, "N", linea, "C", descrip, "C" });
                        
            }

            
        }

     

         fr.close();   
         br.close(); 
         System.out.println(linea);
    }

       public void fin() throws Exception {
        super.fin();
    }

    String obtenerFechaFormateada(String fechap, String formato) {
            long fecha=Date.parse(fechap);
            SimpleDateFormat sdf = new SimpleDateFormat(formato);
            return sdf.format(fecha);
    }
    
   public String convertirClabe(String dato, String banco) {
        int tama_cuenta = dato.length();
        while (tama_cuenta < 11) {
            dato = "0" + dato;
            tama_cuenta++;
        }

        return dato = banco + "XXX" + dato + "X";
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
    
     public String convertirClabe(String clabe) {
        int conta = clabe.length();
        for (int x = conta; x < 18; x++) {
            clabe = "0" + clabe;
        }
        return clabe;
    }

    public static String EliminarNotacionCientífica(double número) {
      
        return new DecimalFormat("#.####################################").format(número);
    }



   

    


}
