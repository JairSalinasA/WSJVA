package cusos.salcon;

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

			// jsRel.append("datos", new Object[] {
			// 		"regresa",
			// 		"Regresa",
			// 		"/img/awesome/solid/angle-double-left.svg",					
			// 		"regre();"
			// });

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
				+ " select "
				+ "   substr(v.cnta,1," + niv_cta + ") cnta  "
				+ "  ,max(lcnta)  lcnta   "
				+ "  ,cast( sum(monto * tmov * nat * case when mes=" + fltper
				+ " then 0 else 1 end) as varchar) salini "
				+ "  ,cast( sum(monto * case when mes=" + fltper + " and tmov=1 then 1 else 0 end) as varchar) cargo "
				+ "  ,cast( sum(monto * case when mes=" + fltper + " and tmov=-1 then 1 else 0 end) as varchar) abono "
				+ "  ,cast( sum(monto * tmov * nat) as varchar) salfin "
				+ " from vmovxcnta v  "
				+ "   left join tcntas c on rtrim(c.cnta)= substr(v.cnta,1," + niv_cta + ")   "
				+ " where anio=" + fltejer + " and mes <= " + fltper
				+ "  and v.cnta like '" + ctap + "%'"
				+ "  and v.cnta != '" + ctap + "' "
				+ " group by substr(v.cnta,1," + niv_cta + ") "
				+ " order by 1  "
				+ "";

		jsResult = ejeQry(eje);

		icuso.outData.put("repinta", "x");
		lisOffset = 0;

		/// Vemos si hay mas
		eje = " select cnta from xdetmov where mes=" + fltper + " and cnta like '" + ctap
				+ "%' and length(cnta)>length('" + ctap + "')+1 limit 1";
		qry(eje);

		if (gNumRows() > 0)
			jsRel.append("datos", new Object[] {
					icuso.pack + "." + icuso.clas + ".lis",
					"Siguiente Nivel",
					"/img/awesome/solid/angle-double-right.svg",
					"1",					
					//"ldCuso('" + icuso.pack + "." + icuso.clas + ".lis',1);"
			});
		else {
			// jsRel.append("datos", new Object[] {
			// 		"Detalle",
			// 		"Detalle",
			// 		"/img/awesome/solid/angle-double-right.svg",					
			// 		"ldCuso('" + icuso.pack + "." + icuso.clas + ".lisDet',1);"
			// });

		}

		// Pasamos Jerarquía
		JSONArray jer = new JSONArray();

		if (ctap.length() > 0)
			for (int i = 0; i < ctap.length(); i++) {
				eje = " select cnta ,lcnta from tcntas where rtrim(cnta)='" + ctap.substring(0, i + 1) + "'";
				ejeQry(eje);
				jer.put(new Object[] { gStrFld("cnta"), gStrFld("lcnta") });
			}

		icuso.outData.put("jerq", jer);
		qry("select lmes from vmeses where mes=" + fltper);
		eje = gStrFld("lmes");

		icuso.outData.put("lcuso", "Saldos a " + eje + " " + fltejer);
		jsVals.put("fltper", fltper);
		jsVals.put("fltejer", fltejer);

	}

	public void lisDet() throws Exception {

		String cnta, per, ejer;
		if (icuso.inData.has("cnta"))
			cnta = (String) icuso.inData.get("cnta");
		else
			throw new Exception("No viene dato de cuenta");

		if (icuso.inData.has("fltper"))
			per = (String) icuso.inData.get("fltper");
		else
			throw new Exception("No viene dato de periodo");

		if (icuso.inData.has("fltejer"))
			ejer = (String) icuso.inData.get("fltejer");
		else
			throw new Exception("No viene dato de ejercicio");

		jsVals.put("fltper", (String) icuso.inData.get("fltper"));
		jsVals.put("fltejer", (String) icuso.inData.get("fltejer"));
		jsVals.put("cnta", (String) icuso.inData.get("cnta"));

		eje = ""
				+ " select "
				+ "   reg ,dreg ,fndo ,cog ,tmov ,cast(monto as varchar) monto ,cvenue ,cvepol "
				+ " ,congenmov ,conmov ,benefmov "
				+ " from xdetmov x "
				+ " where anio="+ ejer+ " and mes <= " + per
				+ "   and cnta='" + cnta + "'"
				+ " limit 500 "
				+ "";
		jsResult = ejeQry(eje);

		jsRel.append("datos", new Object[] {
				"regresa",
				"Regresa",
				"/img/awesome/solid/angle-double-left.svg",
				"regre();"
		});

		lisFil = new JSONObject();
		lisFil.put("Filtros", new Object[] { "tmov" }); // Listado y en Orden
		lisFil.put("nFiltros", new Object[] { "T Mov" }); // Nombres de Filtros
		lisFil.put("sFiltros", new Object[] { "10" }); // Tamaño de Filtros

		JSONArray tmov = new JSONArray();
		tmov.put(new Object[] { "Cargo", "1" });
		tmov.put(new Object[] { "Abono", "-1" });
		lisFil.put("tmov", tmov);

		JSONArray jer = new JSONArray();

		for (int i = 0; i < cnta.length(); i++) {
			eje = " select cnta ,lcnta from tcntas where rtrim(cnta)='" + cnta.substring(0, i + 1) + "'";
			ejeQry(eje);
			jer.put(new Object[] { gStrFld("cnta"), gStrFld("lcnta") });
		}

		icuso.outData.put("jerq", jer);
		qry("select lmes from vmeses where mes=" + per);
		eje = gStrFld("lmes");

		icuso.outData.put("lcuso", "Detalle a " + eje + " " + ejer);
	}

	public void fin() throws Exception {
		super.fin();
	}
        
         public void actualizar() throws Exception {
		   String anio= icuso.inData.getString("fltejer");
                   String mes= icuso.inData.getString("fltper");
                    String DateName="";
                    ejePs( "call p_acumcontab("+anio+","+mes+")");
                    
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
            jsResult= ejeQry(" select 'Se actualizaron Correctamente los Estados Contables "+DateName+" '  as mensaje ");
            icuso.outData.put("nombre", "Actualizacion Estados Presupuestales");
              
               
               
               
                 
                
          icuso.outData.put("tabla", jsResult);
              
             
	}

}