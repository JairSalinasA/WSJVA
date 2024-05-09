package cusos.concilia;

import srv.cuso;

import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import comun.bkComun;

public class ini extends bkComun {

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

      String mov = icuso.inData.getString("mov");

      if (dml.equals("3")) {

         eje = "delete from tdmovban where mov = " + mov;
         nregis = ejePs(eje);

         eje = "delete from tmovban where mov = " + mov;
         nregis = ejePs(eje);

         eje = "delete from tarchivos where archi = " + mov;
         nregis = ejePs(eje);
      }

      if (dml.equals("1")) {

         String url = icuso.inData.getString("url");
         String banco = icuso.inData.getString("banco");
         String clabe = icuso.inData.getString("clabe");
         String anio = icuso.inData.getString("anio");
         String mes = icuso.inData.getString("mes");

         String dirfis = "/srv/UploadFile/archis/";
         dirfis = dirfis + url.split("archis/")[1];

         //dirfis = "/Users/jorgedelgado/Downloads/24002.xlsx";

         // mov = archi
         qry("select nextval('pk_dregis') conse");
         mov = icuso.dbx.JSTblgetvCampo("conse").toString();

         insertFile(mov, url, banco);

         insertHeader(mov, anio, mes, clabe);

         insertDetail(mov, dirfis);

      }

      if (!dml.equals("3")) {// Si no es borrar regresa nuevo registro
         eje = "select * from (" + icuso.ponInData(prop.getProperty("lis")) + ") lisQ where mov =" + mov;
         jsResult = ejeQry(eje, new String[] {});
      }

      icuso.outData.put("dml", dml);
   }

   private void insertFile(String mov, String url, String banco) throws Exception {

      // Guardar el archivo subido por el usuario
      eje = "insert into tarchivos (archi ,url, tarchi, larchi,fecha ) values(?,?,?,?,now()) ";

      nregis = ejePs(eje, new String[] {
            mov, "N",
            url, "C",
            "20", "N",
            banco, "C"
      });

   }

   private void insertHeader(String mov, String anio, String mes, String clabe) throws Exception {

      // Insertar el Header de "tmovban"
      eje = "insert into tmovban (mov, anio, mes, clabe) values(?,?,?,?) ";

      nregis = ejePs(eje, new String[] {
            mov, "N",
            anio, "N",
            mes, "N",
            clabe, "C",
      });
   }

   private void insertDetail(String mov, String rutaArchivoExcel) throws Exception {

      FileInputStream inputStr = new FileInputStream(rutaArchivoExcel);
      XSSFWorkbook xssfWork = new XSSFWorkbook(inputStr);
      XSSFSheet sheet = xssfWork.getSheetAt(0);

      int numero_registros = 0;
      Double Total = 0.0;

      for (Row row : sheet) {

         numero_registros++;

         if (numero_registros == 1)
            continue;

         String Concepto = getConcepto(row.getCell(0).toString());
         String Fecha = row.getCell(1).toString();
         String Referencia = row.getCell(2).toString();
         String Cargo = row.getCell(3).toString();
         String Abono = row.getCell(4).toString();
         String Cuenta = deleteScientificNotation(Double.parseDouble(row.getCell(6).toString()));
         String Codigo = row.getCell(7).toString();

         String tmov = "";
         String Monto = "";

         if(Double.parseDouble(Cargo) == 0.0){
            tmov = "-1";
            Monto = Abono;
         }
         else {
            tmov = "1";
            Monto = Cargo;
         }

         qry("select nextval('pk_dregis') conse");
         String dmov = icuso.dbx.JSTblgetvCampo("conse").toString();

         eje = "insert into tdmovban( dmov, mov, concepto, fecha, referencia,  tmov, monto, cuenta, codigo) " + 
         " values(?,?,?,?,?,?,?,?,?) ";

         nregis = ejePs(eje, new String[] {
               dmov, "N",
               mov, "N",
               Concepto, "C",
               Fecha, "C",
               Referencia, "N", 
               tmov, "N", 
               Monto, "N",
               Cuenta, "C",
               Codigo, "C" 
         });

         Total = Total + Double.parseDouble(Monto);

      }

      eje = "update tmovban set total = ? where mov = ? ";
	   nregis = ejePs(eje, new String[] { Total + "", "N", mov, "N" });

   }

   private String deleteScientificNotation(Double numero) {
      return new DecimalFormat("#.####################################").format(numero);
   }

   private String getConcepto(String cadena) {

      try{
         cadena = deleteScientificNotation(Double.parseDouble(cadena));
      }catch(Exception ex){}

      return cadena;
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