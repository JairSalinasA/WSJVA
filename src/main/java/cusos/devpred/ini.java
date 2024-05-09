package cusos.devpred;

import comun.bkComun;
import java.util.regex.Pattern;
import srv.cuso;

//import cusos.comun;

public class ini extends bkComun {

    private final String[] UNIDADES = { "", "un ", "dos ", "tres ", "cuatro ", "cinco ", "seis ", "siete ", "ocho ",
            "nueve " };
    private final String[] DECENAS = { "diez ", "once ", "doce ", "trece ", "catorce ", "quince ", "dieciseis ",
            "diecisiete ", "dieciocho ", "diecinueve", "veinte ", "treinta ", "cuarenta ",
            "cincuenta ", "sesenta ", "setenta ", "ochenta ", "noventa " };
    private final String[] CENTENAS = { "", "ciento ", "doscientos ", "trecientos ", "cuatrocientos ", "quinientos ",
            "seiscientos ",
            "setecientos ", "ochocientos ", "novecientos " };

    public ini(cuso inCuso) throws Exception {
        super(inCuso);
        kComa = "'";
    }

    public void lis() throws Exception {
        super.lis(true);
    }

    public void cap() throws Exception {

        if (icuso.cuso.equals("devpred.ini.cap_repo")) {
            icuso.prop.setProperty("lis", ""
                    + " select "
                    + "'C.P. ARISTEO ORTEGA SIERRA' nombre1, "
                    + "'JEFE DEL DEPARTAMENTO DEL REGISTRO Y CONTROL DE INGRESOS'   puesto1, "
                    + "'C.P. JORGE CASTRO SANTIAGO'   nombre3, "
                    + "'DIRECTOR GENERAL DE RECAUDACIÓN'   puesto3, "
                    + "'LIC. BENJAMIN VALENZO CANTOR'   nombre2, "
                    + "'SUBSECRETARIO DE INGRESOS'   puesto2, "
                    + "  comp even ,anio ejer ,mes per ,fecha ,monto ,ofic ,url ,comp tram ,comp from vdevpred "
                    + " order by comp "
                    + "");
                    // {comp} {ejer} {per} {ofic}
        }


        eje= "select solp from vdevpredet where comp="+ icuso.inData.getString("comp")+ " and cmprb is ";

        if (icuso.cuso.equals("devpred.ini.cap_envia")) {
            ejeQry(eje+ "null");
            if (gNumRows() > 0)
                throw new Exception("Faltan comprobantes de capturar");
        }

        if (icuso.cuso.equals("devpred.ini.cap_3")) {
            ejeQry(eje+ "not null");
            if (gNumRows() > 0)
                throw new Exception("Trámite ya tiene comprobantes no se puede borrar");
        }

        jsVals.put("anio", icuso.inData.get("anio"));
        super.cap();
    }

    public void graba() throws Exception {
        if (icuso.inData.getString("dml").equals("a")) {
            ejePs("call p_devpred_snd("
                    + icuso.inData.getString("comp")
                    + ",'" + icuso.inData.getString("url")
                    + "')");

            eje = ""
                    + " select "
                    + "   comp ,anio ,mes ,fecha ,monto ,ofic ,url  "
                    + " from vdevpred "
                    + " where comp=" + icuso.inData.getString("comp");

            jsResult = ejeQry(eje);
            icuso.outData.put("dml", 2);

        } else
            super.graba();
    }

    public void fin() throws Exception {
        super.fin();
    }

    public void repo() throws Exception {

        String even = icuso.inData.getString("even");
        qry("select descrip from tregis where regis=" + even);
        String noficio = icuso.dbx.JSTblgetvCampo("descrip").toString();

        String ejer = icuso.inData.getString("anio");
        noficio = "SFA/SI/DGR/CI/" + noficio + "/" + ejer;

        eje = ""
                + " select "
                + "   munpo ,lbenef ldregis ,to_char(fecha, 'DD-MON-YYYY') fecha ,monto "
                + "  ,4002 fi ,montoedo estado, 99557 fueni ,montompo munpo "
                + "   ,lbnco ,substring(clabe,7,11) ctabnca ,clabe "
                + " from vdevpredet v "
                + " where comp=" + even
                + "  and munpo "
                + "";
        // tabla 2 hoja 3
        jsResult = ejeQry(eje + " in (1,30) ");
        jsResult.put("datos2", ejeQry(eje.replace("0.03", "0.05") + " not in(1,30)"));

        icuso.outData.put("nombre", "Reporte");
        jsResult.put("noficio", noficio);

        String nombre1 = icuso.inData.getString("nombre1");
        String puesto1 = icuso.inData.getString("puesto1");
        String nombre2 = icuso.inData.getString("nombre2");
        String puesto2 = icuso.inData.getString("puesto2");
        String nombre3 = icuso.inData.getString("nombre3");
        String puesto3 = icuso.inData.getString("puesto3");

        qry(" select to_char(now(),'dd/mm/yyyy') fecha");
        String fecha = icuso.dbx.JSTblgetvCampo("fecha").toString();
        String fecha_letra = getFechaNombre(fecha);

        jsResult.put("fecha_letra", fecha_letra);

        jsResult.put("firma1", nombre1 + " <br> " + puesto1);
        jsResult.put("firma2", nombre2 + " <br> " + puesto2);
        jsResult.put("firma3", nombre3 + " <br> " + puesto3);
        jsResult.put("nombre2", nombre2);
        jsResult.put("puesto2", puesto2);

        icuso.outData.put("tabla", jsResult);

    }

    public String getFechaNombre(String fecha) {
        String LetraFec = "";

        String[] array = fecha.split("/");
        String dia = array[0];
        String mes = array[1];
        String anio = array[2];

        switch (mes) {
            case "01":
                LetraFec = dia + " de Enero del " + anio;
                break;
            case "02":
                LetraFec = dia + " de Febrero del " + anio;
                break;
            case "03":
                LetraFec = dia + " de Marzo del " + anio;
                break;
            case "04":
                LetraFec = dia + " de Abril del " + anio;
                break;
            case "05":
                LetraFec = dia + " de Mayo del " + anio;
                break;
            case "06":
                LetraFec = dia + " de Junio del " + anio;
                break;
            case "07":
                LetraFec = dia + " de Julio del " + anio;
                break;
            case "08":
                LetraFec = dia + " de Agosto del " + anio;
                break;
            case "09":
                LetraFec = dia + " de Septiembre del " + anio;
                break;
            case "10":
                LetraFec = dia + " de Octubre del " + anio;
                break;
            case "11":
                LetraFec = dia + " de Noviembre del " + anio;
                break;
            case "12":
                LetraFec = dia + " de Diciembre del " + anio;
                break;

            default:
                LetraFec = "";
                break;
        }

        return LetraFec;
    }

    public String Convertir(String numero, boolean mayusculas) {
        String literal = "";
        String parte_decimal;
        // si el numero utiliza (.) en lugar de (,) -> se reemplaza
        numero = numero.replace(".", ",");
        // si el numero no tiene parte decimal, se le agrega ,00
        if (numero.indexOf(",") == -1) {
            numero = numero + ",00";
        }
        // se valida formato de entrada -> 0,00 y 999 999 999,00
        if (Pattern.matches("\\d{1,9},\\d{1,2}", numero)) {
            // se divide el numero 0000000,00 -> entero y decimal
            String Num[] = numero.split(",");
            // de da formato al numero decimal
            parte_decimal = " " + Num[1] + "/100 M.N.";
            // se convierte el numero a literal
            if (Integer.parseInt(Num[0]) == 0) {// si el valor es cero
                literal = "cero ";
            } else if (Integer.parseInt(Num[0]) > 999999) {// si es millon
                literal = getMillones(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 999) {// si es miles
                literal = getMiles(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 99) {// si es centena
                literal = getCentenas(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 9) {// si es decena
                literal = getDecenas(Num[0]);
            } else {// sino unidades -> 9
                literal = getUnidades(Num[0]);
            }
            // devuelve el resultado en mayusculas o minusculas
            if (mayusculas) {
                return (literal + parte_decimal).toUpperCase();
            } else {
                return (literal + parte_decimal);
            }
        } else {// error, no se puede convertir
            return literal = null;
        }
    }

    /* funciones para convertir los numeros a literales */
    private String getUnidades(String numero) {// 1 - 9
        // si tuviera algun 0 antes se lo quita -> 09 = 9 o 009=9
        String num = numero.substring(numero.length() - 1);
        return UNIDADES[Integer.parseInt(num)];
    }

    private String getDecenas(String num) {// 99
        int n = Integer.parseInt(num);
        if (n < 10) {// para casos como -> 01 - 09
            return getUnidades(num);
        } else if (n > 19) {// para 20...99
            String u = getUnidades(num);
            if (u.equals("")) { // para 20,30,40,50,60,70,80,90
                return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8];
            } else {
                return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8] + "y " + u;
            }
        } else {// numeros entre 11 y 19
            return DECENAS[n - 10];
        }
    }

    private String getCentenas(String num) {// 999 o 099
        if (Integer.parseInt(num) > 99) {// es centena
            if (Integer.parseInt(num) == 100) {// caso especial
                return " cien ";
            } else {
                return CENTENAS[Integer.parseInt(num.substring(0, 1))] + getDecenas(num.substring(1));
            }
        } else {// por Ej. 099
            // se quita el 0 antes de convertir a decenas
            return getDecenas(Integer.parseInt(num) + "");
        }
    }

    private String getMiles(String numero) {// 999 999
        // obtiene las centenas
        String c = numero.substring(numero.length() - 3);
        // obtiene los miles
        String m = numero.substring(0, numero.length() - 3);
        String n = "";
        // se comprueba que miles tenga valor entero
        if (Integer.parseInt(m) > 0) {
            n = getCentenas(m);
            return n + "mil " + getCentenas(c);
        } else {
            return "" + getCentenas(c);
        }

    }

    private String getMillones(String numero) { // 000 000 000
        // se obtiene los miles
        String miles = numero.substring(numero.length() - 6);
        // se obtiene los millones
        String millon = numero.substring(0, numero.length() - 6);
        String n = "";
        if (millon.length() > 1) {
            n = getCentenas(millon) + "millones ";
        } else {
            n = getUnidades(millon) + "millon ";
        }
        return n + getMiles(miles);
    }
}
