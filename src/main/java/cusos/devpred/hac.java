/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cusos.devpred;

import comun.bkComun;
import java.util.regex.Pattern;
import srv.cuso;

public class hac extends bkComun {

    private final String[] UNIDADES = { "", "un ", "dos ", "tres ", "cuatro ", "cinco ", "seis ", "siete ", "ocho ",
            "nueve " };
    private final String[] DECENAS = { "diez ", "once ", "doce ", "trece ", "catorce ", "quince ", "dieciseis ",
            "diecisiete ", "dieciocho ", "diecinueve", "veinte ", "treinta ", "cuarenta ",
            "cincuenta ", "sesenta ", "setenta ", "ochenta ", "noventa " };
    private final String[] CENTENAS = { "", "ciento ", "doscientos ", "trecientos ", "cuatrocientos ", "quinientos ",
            "seiscientos ",
            "setecientos ", "ochocientos ", "novecientos " };

    public hac(cuso inCuso) throws Exception {
        super(inCuso);
    }

    public void cap() throws Exception {
        jsVals.put("solp", icuso.inData.get("solp"));
        jsVals.put("even", icuso.inData.get("even"));
        super.cap();
    }

    public void graba() throws Exception {
        if (icuso.inData.getString("cmprb").equals(""))
            prop.setProperty("dml2", "delete from tarchivos where archi=#dregis and tarchi=0");
        super.graba();
    }

    public void solp() throws Exception {

        String munpo = icuso.inData.getString("munpo");
        String lmunpo = icuso.inData.getString("lbenef");
        String monto = icuso.inData.getString("monto");
        String monto_c = icuso.inData.getString("monto");
        monto = monto.replace("$", "").replace(",", "");
        String clabe = icuso.inData.getString("clabe");
        String cuenta;
        cuenta = clabe.substring(6, clabe.length() - 1);
        String monto_letra = Convertir(monto + "", true);

        qry(" SELECT to_char(current_timestamp, 'dd/mm/yyyy HH12:MI:SS') actual");
        String actual = icuso.dbx.JSTblgetvCampo("actual").toString();

        qry(" select a.usua usuario_actual, a.usua||' - ' ||c.nombre nombre from tempses a inner join cusuarios0 c on cast(a.usua as integer)=c.usua ");
        String usuario_actual = icuso.dbx.JSTblgetvCampo("usuario_actual").toString();
        String nombre = icuso.dbx.JSTblgetvCampo("nombre").toString();

        qry(" select to_char(now(),'dd/mm/yyyy') fecha");
        String fecha = icuso.dbx.JSTblgetvCampo("fecha").toString();
        String fecha_letra = getFechaNombre(fecha);

        String sol_pago = icuso.inData.getString("solp");
        eje = ""
            + " select "
            + "  dr.tram ,to_char(r.fecha,'dd/mm/yyyy') fecha ,dr.fndo ,f.lfndo ,dr.cnta ,te.lteven "
            + " from tdregis dr "
            + "  left join tfondos f on f.fndo = dr.fndo "
            + "  left join tregis r on r.regis =dr.regis "
            + "  left join tteven te on te.teven =dr.teven "
            + "where dregis=" + sol_pago;

        qry(eje);
        String tram = icuso.dbx.JSTblgetvCampo("tram").toString();
        String fechap = icuso.dbx.JSTblgetvCampo("fecha").toString();
        String fndo = icuso.dbx.JSTblgetvCampo("fndo").toString();
        String lfndo = icuso.dbx.JSTblgetvCampo("lfndo").toString();
        String cnta = icuso.dbx.JSTblgetvCampo("cnta").toString();
        String concepto_movimiento = icuso.dbx.JSTblgetvCampo("lteven").toString();

        String ban = clabe.substring(0, 3);

        qry(" select lbnco  from tcbancos t where bnco='" + ban + "'");
        String banco = icuso.dbx.JSTblgetvCampo("lbnco").toString();

        // JSONObject resul;
        jsResult = ejeQry(" select to_char(now(),'dd/mm/yyyy') fecha");

        munpo = munpo + " " + lmunpo;
        jsResult.put("solicitud_pago", sol_pago);
        jsResult.put("munpo", munpo);
        jsResult.put("lmunpo", lmunpo);
        jsResult.put("monto", monto_c);
        jsResult.put("monto_letra", monto_letra);
        jsResult.put("fecha", fecha);
        jsResult.put("fecha_letra", fecha_letra);
        jsResult.put("clabe", clabe);
        jsResult.put("cuenta", cuenta);

        jsResult.put("tram", tram);
        jsResult.put("fechap", fechap);
        jsResult.put("fndo", fndo);
        jsResult.put("lfndo", lfndo);
        jsResult.put("cnta", cnta);
        jsResult.put("concepto_movimiento", concepto_movimiento);
        jsResult.put("banco", banco);
        jsResult.put("actual", actual);

        jsResult.put("usuario_actual", usuario_actual);
        jsResult.put("nombre", nombre);

        icuso.outData.put("tabla", jsResult);

    }

    public void fin() throws Exception {
        super.fin();

    }

    public String getFechaNombre(String fecha) {
        String LetraFec = "";

        String[] array = fecha.split("/");
        String dia = array[0];
        String mes = array[1];
        String anio = array[2];

        switch (mes) {
            case "01":
                LetraFec = dia + " DE ENERO DE " + anio;
                break;
            case "02":
                LetraFec = dia + " DE FEBRERO DE " + anio;
                break;
            case "03":
                LetraFec = dia + " DE MARZO DE " + anio;
                break;
            case "04":
                LetraFec = dia + " DE ABRIL DE " + anio;
                break;
            case "05":
                LetraFec = dia + " DE MAYO DE " + anio;
                break;
            case "06":
                LetraFec = dia + " DE JUNIO DE " + anio;
                break;
            case "07":
                LetraFec = dia + " DE JULIO DE " + anio;
                break;
            case "08":
                LetraFec = dia + " DE AGOSTO DE " + anio;
                break;
            case "09":
                LetraFec = dia + " DE SEPTIEMBRE DE " + anio;
                break;
            case "10":
                LetraFec = dia + " DE OCTUBRE DE " + anio;
                break;
            case "11":
                LetraFec = dia + " DE NOVIEMBRE DE " + anio;
                break;
            case "12":
                LetraFec = dia + " DE DICIEMBRE DE " + anio;
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
