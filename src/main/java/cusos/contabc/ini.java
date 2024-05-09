package cusos.contabc;

import comun.bkComun;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import org.json.JSONArray;
import org.json.JSONObject;


import srv.cuso;
import org.json.JSONArray;
import org.json.JSONObject;

import comun.bkComun;

public class ini extends bkComun {

	public ini(cuso inCuso) throws Exception {
		super(inCuso);
		kComa = "'";
	}

	public void lis() throws Exception {
		String niv_cta = "1";
		String ctap = "";
		boolean niv = icuso.inData.has("lay") && icuso.inData.getString("lay").equals("1");

		String fltper = "1", fltejer = "2022";

		if (!icuso.inData.has("deIni") && icuso.inData.has("fltper")) {
			fltper = icuso.inData.getString("fltper");
			fltejer = icuso.inData.getString("fltejer");
		}

		if (niv) {
			ctap = icuso.inData.getString("cnta");
			niv_cta = Integer.toString(ctap.length() + 1);

			jsRel.append("datos", new Object[] {
					"regresa",
					"Regresa",
					"/img/awesome/solid/angle-double-left.svg",					
					"regre();"
			});

		} else {
			lisFil = new JSONObject();
			lisFil.put("Filtros", new Object[] { "ejer", "per" }); // Listado y en Orden
			lisFil.put("nFiltros", new Object[] { "Ejercicio", "Periodo" }); // Nombres de Filtros
			lisFil.put("sFiltros", new Object[] { "", "8" }); // Tamaño de Filtros

			JSONArray ejer = new JSONArray();
			while (barre("select cast(ejer as varchar) ejer from tejercicios order by ejer desc")) {
				eje = valStrFld("ejer");
				ejer.put(new Object[] { eje, eje });
			};
			lisFil.put("ejer", ejer);
			
			JSONArray per = new JSONArray();
			while (barre("select cast(mes as varchar) mes,lmes from vmeses")) {
				eje = valStrFld("mes");
				per.put(new Object[] { valStrFld("lmes"), eje });
			};
			
			lisFil.put("per", per);
		}

		eje = ""
		+ " select '' \"Estados Financieros\""
		+ "";
	
		jsResult = ejeQry(eje);

		icuso.outData.put("repinta", "x");
		lisOffset = 0;

		
		qry("select lmes from vmeses where mes=" + fltper);
		eje = gStrFld("lmes");

		icuso.outData.put("lcuso", "Saldos a " + eje + " " + fltejer);
		jsVals.put("fltper", fltper);
		jsVals.put("fltejer", fltejer);

	}


	public void fin() throws Exception {
		super.fin();
	}
        
        public void actualizar() throws Exception {
		   String anio= icuso.inData.getString("fltejer");
                   String mes= icuso.inData.getString("fltper");
                   String DateName="";

				   anio="2024";
				   mes="12";
                    ejePs( "call p_acumregiscontab("+anio+","+mes+")");
                    
                    switch (mes) { 
            case "1":
             DateName="De Enero del "+anio;
             break;
            case "2":
            DateName="De Febrero del "+anio;
             break;
             case "3":
             DateName="De  Marzo del "+anio;
             break;
            case "4":
            DateName="De Abril del "+anio;
             break;
             case "5":
             DateName="De  Mayo del "+anio;
             break;
            case "6":
             DateName="De  Junio del "+anio;
             break;
             case "7":
             DateName="De Julio del "+anio;
             break;
            case "8":
             DateName="De  Agosto del "+anio;
             break;
             case "9":
             DateName="De  Septiembre del "+anio;
             break;
            case "10":
             DateName="De Octubre del "+anio;
             break;
             case "11":
             DateName="De  Noviembre del "+anio;
             break;
            case "12":
             DateName="De Diciembre del "+anio;
             break;
            default:
             // Default secuencia de sentencias.
          }
                    
                    
                     //JSONObject resul; 
            jsResult= ejeQry(" select 'Se actualizaron Correctamente los Estados Presupuestales "+DateName+" '  as mensaje ");
            icuso.outData.put("nombre", "Actualizacion Estados Presupuestales");
              
               
               
               
                 
                
          icuso.outData.put("tabla", jsResult);
              
             
	}

}