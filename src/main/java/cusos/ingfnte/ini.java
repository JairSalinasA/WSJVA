package cusos.ingfnte;
import org.json.JSONArray;
import org.json.JSONObject;

import comun.bkComun;
import srv.cuso;

    public class ini extends bkComun {
        private String banco, archi, fecha, clabe, regis, fndo;
    
        public ini(cuso inCuso) throws Exception {
            super(inCuso);
            kComa = "'";
        }
    
        public void lis() throws Exception {
           
            String pmes = "12", panio = "2023";


            lisFil = new JSONObject();
			lisFil.put("Filtros", new Object[] { "anio", "mes" }); // Listado y en Orden
			lisFil.put("nFiltros", new Object[] { "Ejercicio", "Periodo" }); // Nombres de Filtros
			lisFil.put("sFiltros", new Object[] { "", "8" }); // Tamaño de Filtros

			JSONArray anio = new JSONArray();
			while (barre("select cast(ejer as varchar) anio from tejercicios order by ejer desc")) {
				eje = valStrFld("anio");
				anio.put(new Object[] { eje, eje });
			};
			lisFil.put("anio", anio);
			
			JSONArray mes = new JSONArray();
			while (barre("select cast(mes as varchar) mes,lmes from vmeses")) {
				eje = valStrFld("mes");
				mes.put(new Object[] { valStrFld("lmes"), eje });
			};
			
			lisFil.put("mes", mes);
        

            eje = ""
				+ " select \n" + //
				        "substr(a.fnte,1,2) tfnte \n" + //
				        ",max(fnt.lfnte) fuente \n" + //
				        ",substr(a.fnte,3) ramo \n" + //
				        ",(case when  substr(a.fnte,1,1)='1' then 'No' else 'Si' end) etiquetado\n" + //
				        ",sum(cast(b.acum_monto  as decimal(18,2))  * b.acum_tmov * (case when trim(b.acum_cntap) like '811%' then 1 else 0 end) ) estimado\n" + 
				        ",sum(cast(b.acum_monto as decimal(18,2))  * b.acum_tmov * (case when trim(b.acum_cntap) like '813%' then 1 else 0 end) ) ampred  \n" + 
						",sum(cast(b.acum_monto as decimal(18,2))  * b.acum_tmov * (case when ((trim(b.acum_cntap) like '813%') or trim(b.acum_cntap) like '811%') then 1 else 0 end) ) modificado  \n" + 
				        ",sum(cast(b.acum_monto as decimal(18,2))  * b.acum_tmov * (case when trim(b.acum_cntap) in('8140','8150') then 1 else 0 end) * -1 ) devengado\n" + 
				        ",sum(cast(b.acum_monto as decimal(18,2))  * b.acum_tmov * (case when trim(b.acum_cntap) in('8150') then 1 else 0 end) *-1 ) recaudado\n" + 
					//	","+fltejer+"  \"fltejer\"  \n" + //
					//	","+fltper+"  \"fltper\"  \n" + //
				        "from tfondos a \n" + //
				        "left join tacumdregiscog b   on a.fndo=b.acum_fndo and b.acum_anio ="+panio+" and b.acum_mes <="+pmes+" \n" + //
				        "left join tfntes fnt on fnt.fnte=substr(a.fnte,1,2) \n" + //
				        "where 1=1\n" + //
				        "group by (case when  substr(a.fnte,1,1)='1' then 'No' else 'Si' end) ,substr(a.fnte,1,2),substr(a.fnte,3) \n" + //
				        "order by substr(a.fnte,1,2),substr(a.fnte,3)  "
				+ "";

		jsResult = ejeQry(eje);

		icuso.outData.put("repinta", "x");
		lisOffset = 0;

        // Pasamos Jerarquía
		
		
		qry("select lmes from vmeses where mes=" + pmes);
		eje = gStrFld("lmes");

		icuso.outData.put("lcuso", "Saldos a " + eje + " " + panio);
		jsVals.put("anio", panio);
		jsVals.put("mes", pmes);

		

           // super.lis();
        }
    
        public void cap() throws Exception {
            super.cap();
        }

        public void fin() throws Exception {
            super.fin();
        }
    
}
