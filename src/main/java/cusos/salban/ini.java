package cusos.salban;

import srv.cuso;

//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.text.DecimalFormat;

import comun.bkComun;

public class ini extends bkComun {
    private String banco, archi, fecha;

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

        String dml = icuso.inData.getString("dml");       
        banco = icuso.inData.getString("bnco");

        if (dml.equals("3")) {
            eje = "delete from vhsalbandia where bnco='" + banco + "' and archsig is null";
            nregis = ejePs(eje);
        }

        if (dml.equals("2")) {
            String url = icuso.inData.getString("url");
            // String fecha = icuso.inData.getString("fecha");

            eje = "" +
                    " with q as (" +
                    "   insert into vhsalbandia ( bnco ,url ) " +
                    "   values ( '" + banco + "', '" + url + "') returning archi,bnco " +
                    " ) " +
                    " select q.archi archn ,v.* " +
                    " from q " +
                    "   left join vhsalbandia v on v.bnco=q.bnco limit 1 " +
                    "";

            qry(eje);
            archi = icuso.dbx.JSTblgetvCampo("archn").toString();

            nregis = gNumRows();

            String dirfis = "/srv/UploadFile/archis/"; // Donde están los Archivos
            if (url.contains("localhost"))
                // dirfis= "/Users/christianmendoza/Documents/Warrior/Proyectos git/nuevos archivos excel/Saldos Bancos/"
                dirfis = "/Users/adolfosalazarvargas/Desarrollo/srvarchinode/archis/";
            dirfis = dirfis + url.split("archis/")[1];

            eje = "get" + banco;
            Method metodo = this.getClass().getDeclaredMethod(eje, String.class);
            metodo.invoke(this, dirfis);
        }

        eje = "select * from (" + icuso.ponInData(prop.getProperty("lis")) + ") lisQ where bnco='"
                + icuso.inData.getString("bnco") + "'";
        jsResult = ejeQry(eje, new String[] {});

        icuso.outData.put("dml", "2");
    }

    /// BANAMEX
    public void get002(String archivo) throws Exception {
        String rutaArchivoExcel = archivo;
        FileInputStream inputStr = new FileInputStream(rutaArchivoExcel);
        XSSFWorkbook xssfWork = new XSSFWorkbook(inputStr);
        XSSFSheet sheet1 = xssfWork.getSheetAt(0);
        // int contaregis = sheet1.getPhysicalNumberOfRows();
        int conta = 1;
        DataFormatter formatter = new DataFormatter();
        FormulaEvaluator evaluator = xssfWork.getCreationHelper().createFormulaEvaluator();
        // barremos las filas
        for (Row row : sheet1) {
            if (conta != 1) {// no entra al encabezado
                /*
                 * datos[0]=Numero
                 * datos[1]=Nombre/Alias
                 * datos[2]=Tipo de cuenta
                 * datos[3]=Sucursal
                 * datos[4]=Cuenta
                 * datos[5]=Saldo
                 * datos[6]=Moneda
                 * datos[7]=Mensaje de Error
                 */

                String[] datos = new String[8];
                for (int cn = 0; cn < row.getLastCellNum(); cn++) {

                    org.apache.poi.ss.usermodel.Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String value = formatter.formatCellValue(cell, evaluator);
                    datos[cn] = value;
                }

                if (datos[5].equals("$ -.-")) {
                    datos[5] = "0";
                }

                if (datos[7] == null) {
                    datos[7] = "";
                }

                datos[4]=convertirClabe(datos[4],banco);

                datos[5] = datos[5].replace(",", ".");

                System.out.println("cuenta:" + datos[4] + " descripcion: " + datos[1] + " fecha: " + "sysdate"
                        + " saldo: " + datos[5]);
                // header
                eje = "insert into tsalbandia (clabe, saldo ,archi, bnco, sucursal, msjerror ) values(?,?,?,?,?,?) ";
                nregis = ejePs(eje, new String[] { datos[4], "C", datos[5], "N", archi, "N", banco, "C", datos[3], "N",
                        datos[7], "C" });

            }

            conta++;
        }
        conta--;
    }

    /// SANTANDER
    public void get014(String archivo) throws Exception {
        String rutaArchivoExcel = archivo;
        FileInputStream inputStr = new FileInputStream(rutaArchivoExcel);
        XSSFWorkbook xssfWork = new XSSFWorkbook(inputStr);
        XSSFSheet sheet1 = xssfWork.getSheetAt(0);
        int contaregis = sheet1.getPhysicalNumberOfRows();
        int conta = 1;
        DataFormatter formatter = new DataFormatter();
        FormulaEvaluator evaluator = xssfWork.getCreationHelper().createFormulaEvaluator();
        // barremos las filas
        for (Row row : sheet1) {
            /* barremos el nombre de las columnas */

            if (conta != 1) {// no entra al encabezado

                if (conta != contaregis) {// no leemos la ultima fila

                    /*
                     * datos[0]=cuenta
                     * datos[1]=descripcion
                     * datos[2]=fecha
                     * datos[3]=hora
                     * datos[4]=disponible
                     * datos[5]=sbc
                     * datos[6]=total
                     */

                    String[] datos = new String[7];
                    for (int cn = 0; cn < row.getLastCellNum(); cn++) {

                        org.apache.poi.ss.usermodel.Cell cell = row.getCell(cn,
                                Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        String value = formatter.formatCellValue(cell, evaluator);
                        datos[cn] = value;
                    }
                    datos[2] = datos[2].substring(6) + "/" + datos[2].substring(4, 6) + "/" + datos[2].substring(0, 4);
                    datos[2] = datos[2] + " " + datos[3];
                    datos[4] = datos[4].substring(1, datos[4].length());
                    datos[5] = datos[5].substring(1, datos[5].length());
                    datos[6] = datos[6].substring(1, datos[6].length());

                    datos[0]=convertirClabe(datos[0],banco);

                    System.out.println("cuenta:" + datos[0] + " descripcion: " + datos[1] + " fecha: " + datos[2]
                            + " hora: " + datos[3] + " disponible: " + datos[4] + " sbc: " + datos[5] + " total: "
                            + datos[6]);

                    // header
                    eje = "insert into tsalbandia (clabe, saldo ,archi, bnco, fecha ) values(?,?,?,?,?) ";
                    nregis = ejePs(eje,
                            new String[] { datos[0], "C", datos[4], "N", archi, "N", banco, "C", datos[2], "F" });
                }
            }
            conta++;
        }
        conta--;
    }

    //BBVA BANCOMER
    public void get012(String archivo) throws Exception {
            int vfinal = 0;
    
            String rutaArchivoExcel = archivo;
            FileInputStream inputStr = new FileInputStream(rutaArchivoExcel);
            XSSFWorkbook xssfWork = new XSSFWorkbook(inputStr);
            XSSFSheet sheet1 = xssfWork.getSheetAt(0);
            // int contaregis=1;
            // int contaregis=sheet1.getPhysicalNumberOfRows();
            int numero_registros = 0;
            int nrow = 1;
    
            DataFormatter formatter = new DataFormatter();
            FormulaEvaluator evaluator = xssfWork.getCreationHelper().createFormulaEvaluator();
            // barremos las filas
            for (Row row : sheet1) {
    
                if (nrow > 1) {// no entra a encabezados
                    /*
                     * datos[0]=Cuenta
                     * datos[1]=Alias
                     * datos[2]=Divisa
                     * datos[3]=Saldo
                     * datos[4]=Disponible
                     * datos[5]=Crédito Disponible
                     * datos[6]=Línea de Crédito
                     * datos[7]=Retenido
                     * datos[8]=Salvo Buen Cobro
                     * datos[9]=Salvo Buen Cobro Ext.
                     * datos[10]=Fecha/Hora
                     * 
                     */
    
                    String[] datos = new String[11];
                    for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                        org.apache.poi.ss.usermodel.Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        String value = "";
    
                        if (cn >= 3 && cn <= 9) {
                            value = cell.toString();
                            value = value.replace(",", "");
                            value = value.replace("'", "");
                        } else {
                            value = formatter.formatCellValue(cell, evaluator);
                        }
    
                        if (cn == 10) {
                            String fechap[] = value.split("-");
                            value = fechap[2].substring(0, 2) + "/" + fechap[1] + "/" + fechap[0] + fechap[2].substring(2);
    
                        }
    
                        if (cn == 0 && value.equals("Total")) {
                            vfinal = 1;
                            break;
                        }
    
                        datos[cn] = value;
                    }
    
                    if (vfinal == 1) {
                        break;
                    }

                    datos[0]=eliminar_0_izquierda(datos[0]);
                    datos[0]=convertirClabe(datos[0],banco);
    
                    numero_registros++;
                    System.out.println("Cuenta: " + datos[0] + " Alias: " + datos[1] + " Divisa: " + datos[2] + " Saldo: "
                            + datos[3] + " Disponible: " + datos[4] + " Crédito Disponible: " + datos[5]
                            + " Línea de Crédito: " + datos[6] + " Retenido: " + datos[7] + " Salvo Buen Cobro: " + datos[8]
                            + " Salvo Buen Cobro Ext.: " + datos[9] + " Fecha/Hora: " + datos[10]);
                    // System.out.println(" Saldo: "+datos[3]);
    
                    // header
                    eje = "insert into tsalbandia (clabe,  saldo ,archi, bnco, fecha ) values(?,?,?,?,?) ";
                    nregis = ejePs(eje,
                            new String[] { datos[0], "C", datos[3], "N", archi, "N", banco, "C", datos[10], "F" });
    
                }
    
                nrow++;
            }
            System.out.println("Total de registros leidos: " + numero_registros);
    
    }
    ////HSBC
    public void get021(String archivo) throws Exception {
            int vfinal = 0;
    
            String rutaArchivoExcel = archivo;
            FileInputStream inputStr = new FileInputStream(rutaArchivoExcel);
            XSSFWorkbook xssfWork = new XSSFWorkbook(inputStr);
            XSSFSheet sheet1 = xssfWork.getSheetAt(0);
            // int contaregis=1;
            int contaregis = sheet1.getPhysicalNumberOfRows();
            int numero_registros = 0;
            int nrow = 1;
    
            DataFormatter formatter = new DataFormatter();
            FormulaEvaluator evaluator = xssfWork.getCreationHelper().createFormulaEvaluator();
            // barremos las filas
            for (Row row : sheet1) {
    
                if (nrow > 1) {// no entra a encabezados
                    /*
                     * datos[0]=Ubicación
                     * datos[1]=Institución
                     * datos[2]=Moneda
                     * datos[3]=Número de cuenta
                     * datos[4]=Nombre de cuenta
                     * datos[5]=Actual disponible
                     * datos[6]=Disponible en libros
                     * datos[7]=Disponible actual con línea de crédito
                     * datos[8]=A la fecha/hora
                     * datos[9]=este es un comentario agregado cuando el monto tenga la leyenda
                     * disponible
                     * 
                     */
    
                    String[] datos = new String[10];
                    for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                        org.apache.poi.ss.usermodel.Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        String value = "";
    
                        if (cell.toString().equals("Subtotal")) {
                            vfinal = 1;
                            break;
                        }
    
                        if (cn == 5) {
                            if (cell.toString().equals("No disponible")) {
                                value = "0";
                                datos[9] = "No disponible";
                            } else {
    
                              //  double res = Double.parseDouble(cell.toString());
                                value =cell.toString();// EliminarNotacionCientífica(res);
                            }
    
                        } else {
    
                            value = formatter.formatCellValue(cell, evaluator);
                        }
    
                        datos[cn] = value;
                    }
    
                    if (vfinal == 1) {
                        break;
                    }
    
                    if (datos[9] == null) {
                        datos[9] = "";
                    }
    
                    if (datos[8].equals("No disponible")) {
                        datos[8] = "";
                    }

                    datos[3]=convertirClabe(datos[3],banco);
    
                    numero_registros++;
                    System.out.println("Ubicación: " + datos[0] + " Institución: " + datos[1] + " Moneda: " + datos[2]
                            + " Número de cuenta: " + datos[3] + " Nombre de cuenta: " + datos[4] + " Actual disponible: "
                            + datos[5] + " Disponible en libros: " + datos[6] + " Disponible actual con línea de crédito: "
                            + datos[7] + " A la fecha/hora: " + datos[8] + " Comentario: " + datos[9]);
                    // header
                    eje = "insert into tsalbandia (clabe,  saldo ,archi, bnco, fecha ) values(?,?,?,?,?) ";
                    nregis = ejePs(eje,
                            new String[] { datos[3], "C", datos[5], "N", archi, "N", banco, "C", datos[8], "F" });
    
                }
    
                nrow++;
            }
            nrow--;
    
            System.out.println("Total de registros leidos: " + numero_registros);
    }

    // Afirme
    public void get062(String archivo) throws Exception {
            int vfinal = 0;
    
            String rutaArchivoExcel = archivo;
            FileInputStream inputStr = new FileInputStream(rutaArchivoExcel);
            XSSFWorkbook xssfWork = new XSSFWorkbook(inputStr);
            XSSFSheet sheet1 = xssfWork.getSheetAt(0);
            // int contaregis=1;
            int contaregis = sheet1.getPhysicalNumberOfRows();
            int numero_registros = 0;
            int nrow = 1;
    
            DataFormatter formatter = new DataFormatter();
            FormulaEvaluator evaluator = xssfWork.getCreationHelper().createFormulaEvaluator();
            // barremos las filas
            for (Row row : sheet1) {
    
                if (nrow >= 7) {// no entra a encabezados
                    /*
                     * datos[0]=Descripcion
                     * datos[1]=Cuenta
                     * datos[2]=Clabe
                     * datos[3]=Moneda
                     * datos[4]=Titular/Alias
                     * datos[5]=Disponible
                     * datos[6]=Saldo total
                     * 
                     */
    
                    String[] datos = new String[7];
                    for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                        org.apache.poi.ss.usermodel.Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        String value = "";
    
                        if (cell.toString().contains("MXP Totales")) {
                            vfinal = 1;
                            break;
                        }
    
                        switch (cn) {
                            case 2:
                                double res = Double.parseDouble(cell.toString());
                                value = EliminarNotacionCientífica(res);
                                break;
                            case 5:
                                value = cell.toString();
                                break;
                            case 6:
                                value = cell.toString();
                                break;
    
                            default:
                                value = formatter.formatCellValue(cell, evaluator);
                        }
                        datos[cn] = value;
    
                    }
    
                    if (vfinal == 1) {
                        break;
                    }

                    datos[2]="0"+datos[2];

                    if(numero_registros==31){
                        String hola="";
                    }

                     datos[1]=convertirClabe(datos[1],banco);
    
                    numero_registros++;
                    System.out.println("Descripcion:" + datos[0] + " Cuenta: " + datos[1] + " Clabe: " + datos[2]
                            + " Moneda: " + datos[3] + " Titular/Alias: " + datos[4] + " Disponible: " + datos[5]
                            + " Saldo total: " + datos[6]);
                    // header
                    eje = "insert into tsalbandia (clabe,  saldo ,archi, bnco ) values(?,?,?,?) ";
                    nregis = ejePs(eje, new String[] { datos[1], "C", datos[5], "N", archi, "N", banco, "C" });
                }
                nrow++;
            }
            nrow--;
    
            System.out.println("Total de registros leidos: " + numero_registros);
    }

    // Banorte
    public void get072(String archivo) throws Exception {
            String rutaArchivoExcel = archivo;
            FileInputStream inputStr = new FileInputStream(rutaArchivoExcel);
            XSSFWorkbook xssfWork = new XSSFWorkbook(inputStr);
            XSSFSheet sheet1 = xssfWork.getSheetAt(0);
            // int contaregis=1;
            // int contaregis=sheet1.getPhysicalNumberOfRows();
            int numero_registros = 0;
            int nrow = 1;
    
            DataFormatter formatter = new DataFormatter();
            FormulaEvaluator evaluator = xssfWork.getCreationHelper().createFormulaEvaluator();
            // barremos las filas
            for (Row row : sheet1) {
    
                if (nrow > 1) {// no entra a encabezados
                    /*
                     * datos[0]=CUENTA
                     * datos[1]=TITULAR / PERSONALIZACIÓN
                     * datos[2]=MONEDA
                     * datos[3]=CLABE
                     * datos[4]=SALDO ACTUAL
                     * datos[5]=SALDO DISPONIBLE
                     * datos[6]=SALDO RETENIDO
                     * datos[7]=CONFIRMACIÓN
                     * 
                     */
    
                    String[] datos = new String[11];
                    for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                        org.apache.poi.ss.usermodel.Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        String value = "";
    
                        if (cn >= 4 && cn <= 6) {
                            double res = Double.parseDouble(cell.toString());
                            value = EliminarNotacionCientífica(res);
                        } else {
                            value = formatter.formatCellValue(cell, evaluator);
                        }
    
                        datos[cn] = value;
                    }
    
                    datos[0] = datos[0].substring(1);
                    datos[3] = datos[3].substring(1);
    
                    numero_registros++;
                    System.out.println("CUENTA: " + datos[0] + " TITULAR / PERSONALIZACIÓN: " + datos[1] + " MONEDA: "
                            + datos[2] + " CLABE: " + datos[3] + " SALDO ACTUAL: " + datos[4] + " SALDO DISPONIBLE: "
                            + datos[5] + " SALDO RETENIDO: " + datos[6] + " CONFIRMACIÓN: " + datos[7]);
                    // header
                    eje = "insert into tsalbandia (clabe,  saldo ,archi, bnco ) values(?,?,?,?) ";
                    nregis = ejePs(eje, new String[] { datos[3], "C", datos[4], "N", archi, "N", banco, "C" });
    
                }
    
                nrow++;
            }
    
            System.out.println("Total de registros leidos: " + numero_registros);
    
    }
    //SCOTIABANK
    public void get044(String archivo) throws Exception {
        
             String rutaArchivoExcel= archivo;
             FileInputStream inputStr = new FileInputStream(rutaArchivoExcel);
             XSSFWorkbook xssfWork = new XSSFWorkbook(inputStr) ;
             XSSFSheet sheet1 = xssfWork.getSheetAt(0);
             //int contaregis=1;
             //int contaregis=sheet1.getPhysicalNumberOfRows();
             int numero_registros=0;
             int nrow=1;
            
                 DataFormatter formatter = new DataFormatter();
                 FormulaEvaluator evaluator = xssfWork.getCreationHelper().createFormulaEvaluator();
                //barremos las filas
                for(Row row : sheet1){
                    
                         if(nrow>=6){// no entra a encabezados
                                 /*
                                 datos[0]=Producto
                                 datos[1]=País
                                 datos[2]=Ciudad
                                 datos[3]=Cuenta
                                 datos[4]=Nombre del Cliente
                                 datos[5]=Moneda
                                 datos[6]=Saldo
                                 datos[7]=Estado de la Cuenta
                                 */
                                 
                                String[] datos = new String[10];
                                 for(int cn=0; cn<row.getLastCellNum(); cn++) {
                                         org.apache.poi.ss.usermodel.Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                         String value="";
                                        
                                        value=  formatter.formatCellValue(cell, evaluator);
                                         
                                         
                                         datos[cn]=value;
                                 }
                                 
                                 if(datos[0].equals("")){
                                     break;
                                 }
                                 
                                 datos[3]=convertirClabe(datos[3],banco);
                                 datos[6]=datos[6].replace(",", "").trim().replace("  ", "");
                                 
                                 numero_registros++;
                                 System.out.println("Producto: "+datos[0]+" País: "+datos[1]
                                 +" Ciudad: "+datos[2]+" Cuenta: "+datos[3]+" Nombre del Cliente: "+datos[4]
                                 +" Moneda: "+datos[5]+" Saldo: "+datos[6]+" Estado de la Cuenta: "+datos[7]);

                                  // header
                                eje = "insert into tsalbandia (clabe,  saldo ,archi, bnco ) values(?,?,?,?) ";
                                nregis = ejePs(eje, new String[] { datos[3], "C", datos[6], "N", archi, "N", banco, "C" });
           
                         }
 
                           nrow++;
                }
               
                System.out.println("Total de registros leidos: "+numero_registros);
         
    }
     //AZTECA
    public void get127(String archivo) throws Exception {
        String rutaArchivoExcel = archivo;
        FileInputStream inputStr = new FileInputStream(rutaArchivoExcel);
        XSSFWorkbook xssfWork = new XSSFWorkbook(inputStr) ;
        XSSFSheet sheet1 = xssfWork.getSheetAt(0);
        //int contaregis=1;
        //int contaregis=sheet1.getPhysicalNumberOfRows();
        int numero_registros=0;
        int nrow=1;
       
            DataFormatter formatter = new DataFormatter();
            FormulaEvaluator evaluator = xssfWork.getCreationHelper().createFormulaEvaluator();
           //barremos las filas
           for(Row row : sheet1){
               
                    if(nrow>=5){// no entra a encabezados
                            /*
                            datos[0]=Empresa
                            datos[1]=Titular / Alias
                            datos[2]=No. de Cuenta
                            datos[3]=CLABE
                            datos[4]=Tipo de Cuenta
                            datos[5]=Moneda
                            datos[6]=Total
                            datos[7]=Retenido
                            datos[8]=Disponible
                            */
                            
                           String[] datos = new String[10];
                            for(int cn=0; cn<row.getLastCellNum(); cn++) {
                                    org.apache.poi.ss.usermodel.Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                    String value="";
                                    
                                    
                                    if(cn>=6){
                                       value=cell.toString();
                                       value=value.replace(",", "");
                                    }else{
                                         value=  formatter.formatCellValue(cell, evaluator);
                                    }
                                   
                                  
                                    
                                    
                                    datos[cn]=value;
                            }
                            
                            if(datos[0].equals("")){
                                break;
                            }
                            
                            datos[6]=datos[6].replace("$", "").replace(",", "");
                            datos[7]=datos[7].replace("$", "").replace(",", "");
                            datos[8]=datos[8].replace("$", "").replace(",", "");
                            
                            numero_registros++;
                            System.out.println("Empresa: "+datos[0]+" Titular / Alias: "+datos[1]+" No. de Cuenta: "+datos[2]+
                            " CLABE: "+datos[3]+" Tipo de Cuenta: "+datos[4]+" Moneda: "+datos[5]+
                            " Total: "+datos[6]+" Retenido: "+datos[7]+" Disponible: "+datos[8]);

                              // header
                              eje = "insert into tsalbandia (clabe,  saldo ,archi, bnco ) values(?,?,?,?) ";
                              nregis = ejePs(eje, new String[] { datos[3], "C", datos[8], "N", archi, "N", banco, "C" });
              
                          
                        
                    }

                      nrow++;
           }
          
           System.out.println("Total de registros leidos: "+numero_registros);
    
    } 
    //AUTOFIN
    public void get128(String archivo) throws Exception {
             int vfinal=0;
             String rutaArchivoExcel = archivo;
             FileInputStream inputStr = new FileInputStream(rutaArchivoExcel);
             XSSFWorkbook xssfWork = new XSSFWorkbook(inputStr) ;
             XSSFSheet sheet1 = xssfWork.getSheetAt(0);
             //int contaregis=1;
             //int contaregis=sheet1.getPhysicalNumberOfRows();
             int numero_registros=0;
             int nrow=1;
            
                 DataFormatter formatter = new DataFormatter();
                 FormulaEvaluator evaluator = xssfWork.getCreationHelper().createFormulaEvaluator();
                //barremos las filas
                for(Row row : sheet1){
                    
                         if(nrow>=7){// no entra a encabezados
                                 /*
                                 datos[0]=Cuenta
                                 datos[3]=Nombre
                                 datos[6]=Saldo
                                 datos[9]=Inversiones
                                 datos[12]=Creditos
                                 */
                                 
                                String[] datos = new String[15];
                                 for(int cn=0; cn<7; cn++) {
                                         org.apache.poi.ss.usermodel.Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                         String value="";
                                         
                                         
                                         
                                         
                                         if(cn==6){
                                            value=cell.toString();
                                            value=value.replace(",", "");
                                            double res=Double.parseDouble(cell.toString());
                                            value=EliminarNotacionCientífica(res);
                                         }else{
                                             
                                              value=  formatter.formatCellValue(cell, evaluator);
                                         }
                                         
                                         datos[cn]=value;
                                         
                                         if(datos[0].equals("")){
                                             vfinal=1;
                                             break;
                                         }
                                         
                                 }
                                 
                                 if(datos[0].equals("")){
                                     break;
                                 }
                                 
                                 datos[0]=convertirClabe(datos[0],banco);
                                 
                                 numero_registros++;
                                 System.out.println("Cuenta: "+datos[0]+" Nombre: "+datos[3]+" Saldo: "+datos[6]+
                                 " Inversiones: "+datos[9]+" Creditos: "+datos[12]);
                               

                              // header
                              eje = "insert into tsalbandia (clabe,  saldo ,archi, bnco ) values(?,?,?,?) ";
                              nregis = ejePs(eje, new String[] { datos[0], "C", datos[6], "N", archi, "N", banco, "C" });
              
                             
                         }
 
                           nrow++;
                }
               
                System.out.println("Total de registros leidos: "+numero_registros);
         
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

}