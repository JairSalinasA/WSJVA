package cusos.layout;

import srv.cuso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

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

    
    public String registro() throws Exception {

       
        String banco = icuso.inData.getString("banco");//3 digitos
        String url = icuso.inData.getString("url");
        String fecha = icuso.inData.getString("fecha");
        String clabe=  icuso.inData.getString("clabe");
        String archi="";
        String regb="";

            qry("select nextval('pk_dregis') conse");
            archi = icuso.dbx.JSTblgetvCampo("conse").toString();


        // header
        eje = "insert into tarchivos (archi ,url, tarchi, larchi, item,fecha ) values(?,?,?,?,?,'"+fecha+"') ";
        nregis = ejePs(eje, new String[] {
            archi, "N",
            url, "C",
            "3", "N",
            clabe,"C",
            banco, "N"
         });

        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        //ruta de mi local
        //archivo = new File ("/Users/christianmendoza/Documents/Warrior/Proyectos git/ejemplo archivos/LA UNION ENERO 2023.txt");
        //ruta del servidor
        String [] nombre= url.split("archis/");
        archivo = new File ("/srv/UploadFile/archis/"+nombre[1]);

        fr = new FileReader (archivo);
        br = new BufferedReader(fr);

        // Lectura del fichero
        String linea;
        while((linea=br.readLine())!=null){

            qry("select nextval('pk_dregis') conse");
            regb = icuso.dbx.JSTblgetvCampo("conse").toString();

            //cuenta= linea.substring(0, 11);
            String dia = linea.substring(16, 18);
            String mes = linea.substring(18, 20);
            String anio = linea.substring(20, 24);
            
            //String fechap = dia+"/"+mes+"/"+anio;
           // String Descrip  = linea.substring(32, 62);
            String tmov=linea.substring(72, 73);
            String montop = linea.substring(73, 87);// los ultimos caracteres son decimales hay que concatenarle el punto

            montop=montop.substring(0, montop.length()-2)+"."+montop.substring(montop.length()-2, montop.length());

                    if(tmov.equals("+")){
                        tmov="1";
                    }else{
                        tmov="-1";
                    }


                // header
                eje = "insert into tregbnco (regb, archi, clabe ,dia, mes, anio, tmov,  monto , texto  ) values(?,?,?,?,?,?,?,?,?) ";
                nregis = ejePs(eje, new String[] {regb, "N", archi, "N",  clabe, "C", dia, "N", mes, "N", anio, "N",  tmov, "N", montop, "N", linea, "C" });

        }

     

         fr.close();    
         System.out.println(linea);
        return archi;

    }



    public void borrar() throws Exception {
        String archi = icuso.inData.getString("archi");

        eje = "delete from tarchivos where archi=?";
        nregis = ejePs(eje, new String[] { archi, "N" });

        eje = "delete from tregbnco where archi=?";
        nregis = ejePs(eje, new String[] { archi, "N" });

    }

    public void graba() throws Exception {  

        String dml = icuso.inData.getString("dml");

        String archi = icuso.inData.getString("archi");



        if (dml.equals("1")) {// inserta
            archi = registro();

        }

        if (dml.equals("3")) {// eliminar
            borrar();
        }

        if (!dml.equals("3")) {// Si no es borrar regresa nuevo registro
            eje = "select * from (" + icuso.ponInData(prop.getProperty("lis")) + ") lisQ where archi =" + archi;
            jsResult = ejeQry(eje, new String[] {});
        }
        icuso.outData.put("dml", dml);

    }

    public void fin() throws Exception {
        super.fin();
    }

    

}