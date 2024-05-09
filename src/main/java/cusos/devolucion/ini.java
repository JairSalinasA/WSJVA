/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cusos.devolucion;

import comun.bkComun;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import srv.cuso;

/**
 *
 * @author christianmendoza
 */

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

        // Insertamos los Datos del Archivo en Temporal
        if (icuso.inData.getString("dml").equals("1")) {
            eje = icuso.inData.getString("archidat");
            String nombre_archi = eje.substring(eje.lastIndexOf("/") + 1);

            eje = ""
            + " CREATE GLOBAL TEMPORARY TABLE wdatos ("
            + "   clabe varchar "
            + "  ,monto numeric(16,2) "
            + " ) ON COMMIT DROP";
            nregis = ejePs(eje, new String[] {});

            //String rutArchi="/srv/UploadFile/archis/" ; //---> Revisa Parámetro
            String rutArchi = "/Users/adolfosalazarvargas/desarrollo/srvarchinode/archis/";
            File archivo = new File(rutArchi + nombre_archi);

            FileReader fr;
            BufferedReader br;
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String strng;

            int con = 0;
            while ((strng = br.readLine()) != null) {

                if (con > 0) {
                    String[] separar = strng.split(",");
                    String clabeInter = separar[0];
                    String monto = separar[1];
                    monto = monto.replace("$", "").replace(",", "");

                    nregis = ejePs("insert into wdatos (clabe,monto) values (?,?) ", new String[] {
                            clabeInter, "C",
                            monto, "N"
                    });
                }
                con++;
            }
            br.close();
            fr.close();
        }

        super.graba();
    }

    public void fin() throws Exception {
        super.fin();
    }
}
