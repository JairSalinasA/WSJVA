package cusos.polizas;
import srv.cuso;

import comun.bkComun;

public class ini extends bkComun {

	public ini(cuso inCuso) throws Exception {
		super(inCuso);
		kComa="'";
	}

	public void lis() throws Exception {
		super.lis();
	}

	public void cap() throws Exception {

		if (icuso.cuso.equals("polizas.ini.cap_repo")) {
            icuso.prop.setProperty("lis", ""+
                     "select 'L.C. MARTHA TOMASA ARANA CÁSTULO' nombre1, "+
					" regis ,to_char(fecha,'dd/mm/yyyy') fecha  ,anio ,mes "+
					" ,case when tt.tpol then 'E' when not tt.tpol then 'I' else 'D' end ctpol "+
					",row_number() over (partition by anio,mes,tt.tpol order by regis) num "+
					" ,r.teven ,tt.lteven "+
					"  ,descrip "+
					" ,monto ,rete ,monto-rete neto "+
					" from tregis r "+
					" inner join tteven tt on tt.teven=r.teven and tt.movs "+
					" where 1=1 {regis} {anio} {mes} order by regis"
                    + "");
        }
		//sjsVals.put("ejer", icuso.inData.get("ejer"));
		super.cap(); 
	}

	public void graba() throws Exception {
		super.graba();
	}

	public void fin() throws Exception {
		super.fin();
	}

	public void repo() throws Exception {

        String regis = icuso.inData.getString("regis");
		String concepto = icuso.inData.getString("lteven")+ " "+ icuso.inData.getString("descrip");
		String poliza = icuso.inData.getString("anio")+"-"+icuso.inData.getString("mes")+"-"+icuso.inData.getString("ctpol")+"-"+icuso.inData.getString("num");
        String fecha = icuso.inData.getString("fecha");

		String tipo_pol= icuso.inData.getString("ctpol");
		String tipo="";
		if(tipo_pol.equals("I")){
			tipo="INGRESOS";
		}
		if(tipo_pol.equals("D")){
			tipo="DIARIO";
		}
		if(tipo_pol.equals("E")){
			tipo="EGRESOS";
		}

		//contable
        eje = ""+
                 "select "+
				"dregis, "+
				"  cnta, auxi , lax  "+
				"  ,case when saldo >0 then saldo else 0 end cargo "+
				"  ,case when saldo <0 then saldo*-1 else 0 end abono "+
				" ,(select trim(n.lcnta) from tcntas n where n.cnta=g.cnta) lcnta "+
				" from ( "+
				" select  "+
				" dregis, "+
				"  cnta ,auxi ,ldregis lax "+
				" ,(monto * tmov) saldo "+
				" from tdregis d "+
				" where regis= "+regis +
				"  and signo !=0 "+
				"  and cnta not like '83%' "+
				" ) g where cnta != '99' "+
				" order by dregis "
                + "";
        // tabla 2 hoja 3
        jsResult = ejeQry(eje);

		//presupuestal
		jsResult.put("datos2", ejeQry( ""+
		"select "+
	"dregis, "+
	"  cntap, auxi , lax  "+
	"  ,case when saldo >0 then saldo else 0 end cargo "+
	"  ,case when saldo <0 then saldo*-1 else 0 end abono "+
	" ,(select trim(n.lcnta) from tcntas n where n.cnta=g.cntap) lcnta "+
	" from ( "+
	" select  "+
	" dregis, "+
	"  cntap ,kcog auxi ,ldregis lax "+
	" ,(monto * tmov) saldo "+
	" from tdregis d "+
	" where regis= "+regis +
	"  and signo !=0 "+
	"  and cntap is not null "+
	" ) g where cntap is not null "+
	" order by dregis "
	+ "")) ;


		qry(" SELECT to_char(current_timestamp, 'dd/mm/yyyy HH12:MI:SS') actual");
        String actual = icuso.dbx.JSTblgetvCampo("actual").toString();

		String nombre1 = icuso.inData.getString("nombre1");

        jsResult.put("nombre", "Reporte");
		jsResult.put("concepto", concepto);
		jsResult.put("poliza", poliza);
		jsResult.put("fecha", fecha);
		jsResult.put("actual", actual);
		jsResult.put("firma1", nombre1);
		jsResult.put("regis", regis);
		jsResult.put("tipo", tipo);

       

        

        icuso.outData.put("tabla", jsResult);

    }

}
