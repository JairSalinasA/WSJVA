package cusos.autoriza;

import srv.cuso;
//import org.json.JSONObject;
import comun.bkComun;

public class ini extends bkComun {
	public ini(cuso inCuso) throws Exception {
		super(inCuso);
	}

	public void lis() throws Exception {
		super.lis();
	}

	public void lisFase() throws Exception {
		String tram = "0";

		if (icuso.inData.has("comp"))
			tram = icuso.inData.getString("comp");

		if (icuso.inData.has("tram"))
			tram = icuso.inData.getString("tram");

		eje = ""
				+ " select "
				+ " a.tram ,a.teven ,c.rol ,r.lrol ,c.orden ,b.fecha "
				+ " from tautoriz a "
				+ "   left join tcautoriz c on c.teven=a.teven "
				+ "   left join croles0 r on r.rol=c.rol "
				+ "   left join tautoriz b on b.tram =a.tram and b.rol=c.rol  "
				+ " where a.tram=" + tram
				+ "   and a.prol is null "
				+ " order by a.tram,c.orden  "
				+ "";

		jsResult = ejeQry(eje);
	}

	public void accion() throws Exception {
		String acc = icuso.inData.getString("acc");
		String tram = icuso.inData.getString("tram");

		if (acc.equals("-1"))
			eje = "delete from tautoriz where prol=(select rol from tempses) and tram=" + tram + "";
		if (acc.equals("1"))
			eje = "" +
					" insert into tautoriz ( " +
					"   tram ,prol ,teven ,rol ,orden) " +
					" select  " +
					"   au.tram ,au.rol ,au.teven ,sig.rol ,sig.orden " +
					" from tautoriz au  " +
					"   left join tcautoriz sig on sig.teven=au.teven " +
					"   left join (" +
					"     select a.tram atram ,max(a.orden) aorden from tautoriz a group by a.tram) a on atram=au.tram "
					+
					" where au.tram =" + tram +
					"   and au.orden =aorden " +
					"   and sig.orden > aorden " +
					" order by sig.orden " +
					" limit 1  " +
					"";

		nregis = ejePs(eje);
		eje = icuso.ponInData(prop.getProperty("lis")) + " where t.tram=" + tram;
		jsResult = ejeQry(eje, new String[] {});

	}

	public void fin() throws Exception {
		super.fin();
	}

}