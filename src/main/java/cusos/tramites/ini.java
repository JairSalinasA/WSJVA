package cusos.tramites;

import srv.cuso;

import comun.bkComun;

public class ini extends bkComun {

	public ini(cuso inCuso) throws Exception {
		super(inCuso);
	}

	public void lis() throws Exception {
		super.lis();		
	}

	public void lisFase() throws Exception {
		eje=""
		+ " select " 
		+ " a.tram ,a.teven ,c.rol ,r.lrol ,c.orden ,b.fecha "
		+ " from tautoriz a "
		+ "   left join tcautoriz c on c.teven=a.teven "
		+ "   left join croles0 r on r.rol=c.rol "
		+ "   left join tautoriz b on b.tram =a.tram and b.rol=c.rol  "
		+ " where a.tram="+ icuso.inData.getString("tram")
		+ "   and a.prol is null "
		+ " order by a.tram,c.orden  "
		+ "" ;

		jsResult = ejeQry(eje);
	}

	public void cap() throws Exception {
		super.cap();
	}

	public void graba() throws Exception {
		super.graba();
	}

	public void fin() throws Exception {
		super.fin();
	}

}