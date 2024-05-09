package cusos.fondos;
import srv.cuso;
import comun.bkComun;

public class area extends bkComun {
    public area(cuso inCuso) throws Exception {
		super(inCuso);
		kComa="'";
	}
    
       

	public void lis() throws Exception {
		super.lis();		
	}
        
        public void cap() throws Exception {
		super.cap(); 
	}

	public void graba() throws Exception {

		String regis = icuso.inData.getString("fndo");
		String fnte = icuso.inData.getString("fnte");
		String monto;
		String fecha;
		String clabe = icuso.inData.getString("clabe");

		qry("select estimado, fecini fecha from tfondos where fndo='"+regis+"'");
        monto = icuso.dbx.JSTblgetvCampo("estimado").toString();
		fecha = icuso.dbx.JSTblgetvCampo("fecha").toString();

		qry("select cogcri cri, lcogcri lcri, case when ( clas is null) then -10000 else clas  end  as cri_clas from tfntes left join tcogcri t on cri=cogcri and cog is false where fnte='"
		+ fnte + "'");
		String clas = icuso.dbx.JSTblgetvCampo("cri_clas").toString();
		String 	kcri = icuso.dbx.JSTblgetvCampo("cri").toString();

		if (clas.equals("-10000")) {
			throw new Exception("No hay cuenta asignada para el CRI "+kcri);
		}

		

		eje = "delete from tdregis where regis=?";
        nregis = ejePs(eje, new String[] { regis, "N" });

		eje = "delete from tregis where regis=?";
        nregis = ejePs(eje, new String[] { regis, "N" });


		if(!clabe.equals("")){

						     // header
							 eje = "insert into tregis (regis ,descrip ,fecha ,monto ,teven ) values(?,?,'"+fecha+"',?,12) ";
							 nregis = ejePs(eje, new String[] { regis, "N", "Presupuesto de Ingresos fondo: "+regis, "C", monto, "N" });
					 
							 eje = ""
							 + " insert into tdregis ("
							 + "   regis ,dregis ,fndo ,tmov ,cnta , cntap ,kcog , monto "
							 + " ) values ( "
							 + "   ? ,? ,? ,1 ,'99','811', ?, ?)  ";
							 nregis = ejePs(eje, new String[] {
									 regis, "N",
									 regis, "N",
									 regis, "N",
									 kcri ,"C",
									 monto, "N"
							 });
					 
							 eje = ""
							 + " insert into tdregis ("
							 + "   regis ,fndo ,tmov ,cnta , cntap ,kcog , monto "
							 + " ) values ( "
							 + "   ? ,? ,-1 , '99', '812', ?, ?)  ";
					 
							 nregis = ejePs(eje, new String[] {
									 regis, "N",
									 regis, "N",
									 kcri ,"C",
									 monto, "N"
							 });

		}


		super.graba();
	}

	public void fin() throws Exception {
		super.fin();
	}

}

