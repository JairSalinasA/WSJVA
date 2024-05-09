package cusos.esefina;

import srv.cuso;

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
    private String archi;

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

    public void fin() throws Exception {
        super.fin();
    }

    public void graba() throws Exception {

        String dml = icuso.inData.getString("dml");

        if (dml.equals("3")) {
            archi = icuso.inData.getString("archi");

            eje = "delete from tdfolio where archi=" + archi;
            nregis = ejePs(eje);

            eje = "delete from tfolio where archi=" + archi;
            nregis = ejePs(eje);

            eje = "delete from tarchivos where archi=" + archi;
            nregis = ejePs(eje);
        }

        if (dml.equals("1")) {

            String url = icuso.inData.getString("url");
            
            String dirfis = "/srv/UploadFile/archis/";
            if (url.contains("localhost"))
            {
                // dirfis= "/Users/christianmendoza/Documents/Warrior/Proyectos git/nuevos
                // archivos excel/Saldos Bancos/"
                dirfis = "/Users/adolfosalazarvargas/Desarrollo/srvarchinode/archis/";
            }
           //dirfis="/Users/christianmendoza/Desktop/test_folio.xlsx";
            dirfis = dirfis + url.split("archis/")[1];
            
            // regis=archi
            qry("select nextval('pk_dregis') conse");
            archi = icuso.dbx.JSTblgetvCampo("conse").toString();

            String idbanco = icuso.inData.getString("idbanco");

            // header
            eje = "insert into tarchivos (archi ,url, tarchi, larchi,fecha ) values(?,?,?,?,now()) ";
            nregis = ejePs(eje, new String[] {
                    archi, "N",
                    icuso.inData.getString("url"), "C",
                    "16", "N",
                    idbanco, "N"
            });

            leerArchivoHoja1(dirfis,idbanco);
            leerArchivoHoja2(dirfis, idbanco);

            System.out.println("archi: "+archi+ " idbanco: "+idbanco);

        }

        if (!dml.equals("3")) {// Si no es borrar regresa nuevo registro
            eje = "select * from (" + icuso.ponInData(prop.getProperty("lis")) + ") lisQ where archi =" + archi;
            jsResult = ejeQry(eje, new String[] {});
        }
        icuso.outData.put("dml", dml);
    }

    public static String EliminarNotacionCientífica(double número) {

        return new DecimalFormat("#.####################################").format(número);
    }
    //se llena tfolio
    public void leerArchivoHoja1(String archivo, String bancoid) throws Exception {
        String rutaArchivoExcel = archivo;
        System.out.println("Inicio archivo "+bancoid);;
       FileInputStream inputStr = new FileInputStream(rutaArchivoExcel);
       XSSFWorkbook xssfWork = new XSSFWorkbook(inputStr) ;
       XSSFSheet sheet1 = xssfWork.getSheetAt(0);

        
       
       int con=1;
       int conta_columnas=0;
          for(Row row : sheet1){
           if (con==1){conta_columnas=row.getLastCellNum();}
            if (con>1){
              
                       boolean bandera=false;
                            String[] datos = new String[65];
                /* 
                              String[] datos = new String[65];
                                datos[0]=CveFteMT
                                datos[1]=CveCaja
                                datos[2]=CveFecAsi	
                                datos[3]=CveSerFol	
                                datos[4]=CveFolio	
                                datos[5]=ImpRcgRec	
                                datos[6]=ImpReqRec	
                                datos[7]=ImpEmbRec	
                                datos[8]=ImpMtaRec	
                                datos[9]=DtoRcgRec	
                                datos[10]=DtoReqRec	
                                datos[11]=DtoEmbRec	
                                datos[12]=DtoMtaRec	
                                datos[13]=TDtoDetRec	
                                datos[14]=CveCajero	
                                datos[15]=CveOriIng	
                                datos[16]=CveContrib	
                                datos[17]=ContriRec	
                                datos[18]=ConRecibo	
                                datos[19]=EdoRec	
                                datos[20]=HoraRec	
                                datos[21]=UsuRec	
                                datos[22]=NSFormPago	
                                datos[23]=UsuCanRecibo	
                                datos[24]=FecCanRecibo	
                                datos[25]=ConceptoCanRecibo	
                                datos[26]=CveTpoIDia	
                                datos[27]=CveFecIDia	
                                datos[28]=CveFolIDia	
                                datos[29]=DirRecibo	
                                datos[30]=IndDevIng	
                                datos[31]=CveTDocTarRecibo	
                                datos[32]=CveSerTDocTarRecibo	
                                datos[33]=CveFolDocTarRecibo	
                                datos[34]=RFCRecibo	
                                datos[35]=FolioFinal	
                                datos[36]=EjeRecibo	
                                datos[37]=PerRecibo	
                                datos[38]=EjeAntRecibo	
                                datos[39]=PerAntRecibo	
                                datos[40]=CveVehiculo	
                                datos[41]=TipoRecibo	
                                datos[42]=EstadoCuentaRecibo	
                                datos[43]=TipoPagoRecibo	
                                datos[44]=NumeroTipoPagoRecibo	
                                datos[45]=ReciboNoSerieVehiculo	
                                datos[46]=ReciboPlacaVehiculo	
                                datos[47]=ReciboVehiculo	
                                datos[48]=ReciboReferenciaPago	
                                datos[49]=ReciboGrupoId	
                                datos[50]=ReciboTramiteId	
                                datos[51]=ReciboSolicitudId	
                                datos[52]=ReciboObservaciones	
                                datos[53]=ReciboTipoCobro	
                                datos[54]=ReciboElectronicoFolio	
                                datos[55]=FormaValorada	
                                datos[56]=ReciboFacturaCFDI	
                                datos[57]=ReciboCVEGENCFDI	
                                datos[58]=MostrarConceptoCFDI	
                                datos[59]=FechaReal	
                                datos[60]=NoAutorizacion
                */
                       for(int cn=0; cn<row.getLastCellNum(); cn++) {
                               org.apache.poi.ss.usermodel.Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                               
                               String value=cell.toString();
                               value=value.replaceAll("\"", "");
                                value=value.replaceAll("'", "");

                               if(cn==4){
                                   value=EliminarNotacionCientífica(Double.parseDouble(cell.toString()));
                               }


                               if(!value.trim().toString().equals("") && value.length()>=2){
                                           String dato=value.substring(value.length()-2, value.length());
                                       if(dato.equals(".0")){
                                           value=value.substring(0, value.length()-2);
                                       }
                               }   

                               bandera=true;
                               datos[cn]=value; 
                       }
                       if(!bandera){
                           break;
                       }
                       

                            //
                       eje = "insert into  tfolio (CveFteMT,CveCaja,CveFecAsi,CveSerFol,CveFolio,ImpRcgRec,ImpReqRec,ImpEmbRec,ImpMtaRec,DtoRcgRec,DtoReqRec,DtoEmbRec,DtoMtaRec,TDtoDetRec,CveCajero,CveOriIng,CveContrib,ContriRec,ConRecibo,EdoRec,HoraRec,UsuRec,NSFormPago,UsuCanRecibo,FecCanRecibo,ConceptoCanRecibo,CveTpoIDia,CveFecIDia,CveFolIDia,DirRecibo,IndDevIng,CveTDocTarRecibo,CveSerTDocTarRecibo,CveFolDocTarRecibo,RFCRecibo,FolioFinal,EjeRecibo,PerRecibo,EjeAntRecibo,PerAntRecibo,CveVehiculo,TipoRecibo,EstadoCuentaRecibo,TipoPagoRecibo,NumeroTipoPagoRecibo,ReciboNoSerieVehiculo,ReciboPlacaVehiculo,ReciboVehiculo,ReciboReferenciaPago,ReciboGrupoId,ReciboTramiteId,ReciboSolicitudId,ReciboObservaciones,ReciboTipoCobro,ReciboElectronicoFolio,FormaValorada,ReciboFacturaCFDI,ReciboCVEGENCFDI,MostrarConceptoCFDI,FechaReal,NoAutorizacion, bancoid, archi)  \n" + 
                               "values(?,?,'"+datos[2]+"',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'"+datos[20]+"',?,?,?,'"+datos[24]+"',?,?,'"+datos[27]+"',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'"+datos[59]+"',?,?,?) ";
                       nregis = ejePs(eje, new String[] {
                                datos[0],"C", //CveFteMT
                                datos[1],"N",//CveCaja	
                                datos[3],"C",//CveSerFol	
                                datos[4],"N",//CveFolio	
                                datos[5],"N",//ImpRcgRec	
                                datos[6],"N",//ImpReqRec	
                                datos[7],"N",//ImpEmbRec	
                                datos[8],"N",//ImpMtaRec	
                                datos[9],"N",//DtoRcgRec	
                                datos[10],"N",//DtoReqRec	
                                datos[11],"N",//DtoEmbRec	
                                datos[12],"N",//DtoMtaRec	
                                datos[13],"N",//TDtoDetRec	
                                datos[14],"C",//CveCajero	
                                datos[15],"C",//CveOriIng	
                                datos[16],"C",//CveContrib	
                                datos[17],"C",//ContriRec	
                                datos[18],"C",//ConRecibo	
                                datos[19],"C",//EdoRec	
                                datos[21],"C",//UsuRec	
                                datos[22],"N",//NSFormPago	
                                datos[23],"C",//UsuCanRecibo
                                datos[25],"C",//ConceptoCanRecibo	
                                datos[26],"C",//CveTpoIDia	
                                datos[28],"N",//CveFolIDia	
                                datos[29],"C",//DirRecibo	
                                datos[30],"C",//IndDevIng	
                                datos[31],"C",//CveTDocTarRecibo	
                                datos[32],"C",//CveSerTDocTarRecibo	
                                datos[33],"C",//CveFolDocTarRecibo	
                                datos[34],"C",//RFCRecibo	
                                datos[35],"N",//FolioFinal	
                                datos[36],"N",//EjeRecibo	
                                datos[37],"N",//PerRecibo	
                                datos[38],"N",//EjeAntRecibo	
                                datos[39],"N",//PerAntRecibo	
                                datos[40],"N",//CveVehiculo	
                                datos[41],"C",//TipoRecibo	
                                datos[42],"N",//EstadoCuentaRecibo	
                                datos[43],"C",//TipoPagoRecibo	
                                datos[44],"N",//NumeroTipoPagoRecibo	
                                datos[45],"C",//ReciboNoSerieVehiculo	
                                datos[46],"C",//ReciboPlacaVehiculo	
                                datos[47],"C",//ReciboVehiculo	
                                datos[48],"C",//ReciboReferenciaPago	
                                datos[49],"C",//ReciboGrupoId	
                                datos[50],"N",//ReciboTramiteId	
                                datos[51],"C",//ReciboSolicitudId	
                                datos[52],"C",//ReciboObservaciones	
                                datos[53],"C",//ReciboTipoCobro	
                                datos[54],"C",//ReciboElectronicoFolio	
                                datos[55],"C",//FormaValorada	
                                datos[56],"C",//ReciboFacturaCFDI	
                                datos[57],"C",//ReciboCVEGENCFDI	
                                datos[58],"C",//MostrarConceptoCFDI	
                                datos[60],"C",//NoAutorizacion
                                bancoid, "N",
                                archi, "N"
                       });
                       
            }
            con++;
          }

               
                   System.out.println("Cantidad "+con);
                   System.out.println("Se inserto la tabla correctamente el archivo "+bancoid);
                   System.out.println(" ");
                   System.out.println(" ");
                   System.out.println(" ");
  
    }
    //se llena tdfolio
    public void leerArchivoHoja2(String archivo, String bancoid) throws Exception {
        String rutaArchivoExcel = archivo;
        System.out.println("Inicio archivo "+bancoid);;
       FileInputStream inputStr = new FileInputStream(rutaArchivoExcel);
       XSSFWorkbook xssfWork = new XSSFWorkbook(inputStr) ;
       XSSFSheet sheet1 = xssfWork.getSheetAt(1);
       
       int con=1;
       int conta_columnas=0;
          for(Row row : sheet1){
           if (con==1){conta_columnas=row.getLastCellNum();}
            if (con>1){
              
                       boolean bandera=false;
                            String[] datos = new String[15];
                /*    										
                              String[] datos = new String[65];
                                datos[0]=CveFteMT
                                datos[1]=CveCaja
                                datos[2]=CveFecAsi	
                                datos[3]=CveSerFol	
                                datos[4]=CveFolio
                                datos[5]=CveDetFolio	
                                datos[6]=CanFteIng	
                                datos[7]=ImpIniRec	
                                datos[8]=CveFteIng	
                                datos[9]=ReciboDetImpAntesDev	
                                datos[10]=EdoDetRec	
                                	
                */
                       for(int cn=0; cn<row.getLastCellNum(); cn++) {
                               org.apache.poi.ss.usermodel.Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                               
                               String value=cell.toString();
                               value=value.replaceAll("\"", "");
                                value=value.replaceAll("'", "");

                               if(cn==4){
                                   value=EliminarNotacionCientífica(Double.parseDouble(cell.toString()));
                               }


                               if(!value.trim().toString().equals("") && value.length()>=2){
                                           String dato=value.substring(value.length()-2, value.length());
                                       if(dato.equals(".0")){
                                           value=value.substring(0, value.length()-2);
                                       }
                               }   

                               bandera=true;
                               datos[cn]=value; 
                       }
                       if(!bandera){
                           break;
                       }
                       

                       eje = "insert into  tdfolio (CveFteMT,CveCaja,CveFecAsi,CveSerFol,CveFolio,CveDetFolio,CanFteIng,ImpIniRec,CveFteIng,ReciboDetImpAntesDev,EdoDetRec, bancoid, archi) \n" + 
                               "values(?,?,'"+datos[2]+"',?,?,?,?,?,?,?,?,?,?) ";
                       nregis = ejePs(eje, new String[] {
                                datos[0],"C",//CveFteMT
                                datos[1],"N",//CveCaja
                                datos[3],"C",//CveSerFol	
                                datos[4],"N",//CveFolio
                                datos[5],"N",//CveDetFolio	
                                datos[6],"N",//CanFteIng	
                                datos[7],"N",//ImpIniRec	
                                datos[8],"N",//CveFteIng	
                                datos[9],"N",//ReciboDetImpAntesDev	
                                datos[10],"C",//EdoDetRec	
                                bancoid, "N",
                                archi, "N"
                       });
                       
            }
            con++;
          }

               
                   System.out.println("Cantidad "+con);
                   System.out.println("Se inserto la tabla correctamente el archivo "+bancoid);
                   System.out.println(" ");
                   System.out.println(" ");
                   System.out.println(" ");
  
    }
   

}