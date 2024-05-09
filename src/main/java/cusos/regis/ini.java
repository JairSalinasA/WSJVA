package cusos.regis;

import srv.cuso;
//import org.json.JSONArray;
//import org.json.JSONObject;

import comun.bkComun;

public class ini extends bkComun {

	public ini(cuso inCuso) throws Exception {
		super(inCuso);
	}

	public void lis() throws Exception {
		String even = icuso.inData.getString("even");
		eje = ""
				+ " select l1,l2,l3,l4,l5,l6,l7,l8,l9 "
				+ " from ("
				+ "       select 0 n,'' l1 ,'' l2 ,'' l3 ,'' l4 ,'' l5 ,'' l6 ,'' l7 ,'' l8 ,'' l9"
				+ " union select 1,'Contable', '','','', '','','','' ,'' "
				+ " union select 2,'Reg','Fondo', 'Cuenta','','Aux','','TMov','Monto' ,''  "
				+ " union select 3, cast(item as varchar) ,cast(fndo as varchar) ,t.cnta ,c.lcnta ,t.auxi,'' ,case tmov when 1 then 'Cargo' else 'Abono' end  ,lpad(cast(cast(monto as money) as text),18,'‗') ,ldregis"
				+ " 	  from tdregis t  "
				+ "         left join tcntas c on trim(c.cnta) =t.cnta   "
				+ "       where tram= " + even
				+ "         and t.cnta is not null           "
				+ " union select 4 ,'' ,'' ,'' ,'' ,'','', '','' ,'' "
				+ " union select 5 ,'Presupuestal' ,'' ,'' ,'' ,'','', '','' ,'' "
				+ " union select 6,'Reg', 'Fondo', 'Mom','','CRI','','TMov','Monto' ,''  "
				+ " union select 7, cast(item as varchar) ,cast(fndo as varchar) ,cntap,c.lcnta  ,kcog, lcogcri ,case tmov when 1 then 'Abono' else 'Cargo' end  ,lpad(cast(cast(monto as money) as text),18,'‗') ,ldregis"
				+ "   	  from tdregis t  "
				+ "         left join tcogcri cc on not cog and cogcri=kcog "
				+ "         left join tcntas c on c.cnta=t.cntap "
				+ "       where tram= " + even
				+ " 	    and cntap is not null "
				+ " ) q "
				+ " where n>0 "
				+ " order by n,l1,l2,l3,l5,l4  "
				+ "";

		jsResult = ejeQry(eje);
	}

	public void fin() throws Exception {
		super.fin();
	}

}