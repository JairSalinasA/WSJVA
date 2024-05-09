package cusos.prueba;
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
		super.cap();
	}

	public void graba() throws Exception {
		
		String dml = icuso.inData.getString("dml");

        String id="";

        if (dml.equals("1")) {// inserta

			 id = icuso.inData.getString("id");
			String monto = icuso.inData.getString("monto");
			monto = monto.replace("$", "").replace(",", "");
			//obtenemos el dato de la secudncia
			qry("select nextval('sprueba') conse");
            id = icuso.dbx.JSTblgetvCampo("conse").toString();
            
			        // header
					eje = "insert into tprueba (id, descripcion,  monto) values(?,?,?) ";
					nregis = ejePs(eje, new String[] { id, "N", "prueba", "C", monto, "N" });

					// header
					eje = "insert into tdprueba (id, concepto) values(?,?) ";
					nregis = ejePs(eje, new String[] { id, "N", "concepto1", "C" });

        }

        if (dml.equals("2")) {// editar

			 id = icuso.inData.getString("id");
			 String monto = icuso.inData.getString("monto");
			 monto = monto.replace("$", "").replace(",", "");
			 // header
			 eje = "update  tprueba set descripcion=?,  monto=? where id=? ";
			 nregis = ejePs(eje, new String[] { "prueba cambio", "C", monto, "N", id, "N" });

			 eje = "update  tdprueba set concepto=? where id=? ";
			 nregis = ejePs(eje, new String[] { "concepto cambio", "C", id, "N" });
            
        }

        if (dml.equals("3")) {// eliminar

			 id = icuso.inData.getString("id");

			eje = "delete from tdprueba where id=? ";
			nregis = ejePs(eje, new String[] { id, "N" });

			 // header
			 eje = "delete from tprueba where id=? ";
			 nregis = ejePs(eje, new String[] { id, "N" });

            
        }
		//esto es para mandar los datos a la pantalla
        if (!dml.equals("3") && !dml.equals("o")) {// Si no es borrar regresa nuevo registro
            eje = "select * from (" + icuso.ponInData(prop.getProperty("lis")) + ") lisQ where id =" + id;
            jsResult = ejeQry(eje, new String[] {});
        }
        icuso.outData.put("dml", dml);
	}

	public void fin() throws Exception { 
		super.fin();
	}

public void repo() throws Exception {

		String datos = icuso.inData.getString("datos");

		//consulta 1
		eje = ""+
		"SELECT to_char(current_timestamp, 'dd/mm/yyyy HH12:MI:SS') actual "
	   + "";
		// tabla 2 hoja 3
		jsResult = ejeQry(eje);

		//consulta 2
		qry(" SELECT to_char(current_timestamp, 'dd/mm/yyyy HH12:MI:SS') actual2");
		String actual2 = icuso.dbx.JSTblgetvCampo("actual2").toString();


		jsResult.put("datos", datos);
		jsResult.put("actual2", actual2);

		icuso.outData.put("tabla", jsResult);
		
	}

}