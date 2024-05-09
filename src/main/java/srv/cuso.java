package srv;

import org.json.JSONObject;
import java.util.Date;
import java.io.InputStream;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Iterator;

public class cuso {
	public JSONObject inData;
	public JSONObject outData = new JSONObject();
	public String cuso ,lcuso ,ejcuso; //, ncuso
	public dbCon dbx = new dbCon();

	//////
	public String stx, usua = null, rol = "-1", sscuso, ipdom;

	public String pack;
	public String clas;
	public String metd;

	// public int cuso;
	public String lusua;
	public String lrol;

	public String dirLog;
	public Date inicia = new Date();
	public boolean dvlp = false;
	public Properties prop = new Properties();
	public ArrayList  <String> filtroLis= new ArrayList <String>();

	public cuso(String datos) throws Exception {

		// --------------------------------------------------------------------

		inData = new JSONObject(datos);
		cuso = (String) inData.get("cuso");
		ipdom = (String) inData.get("ipdom");

		if (cuso.equals("")) {
			throw new Exception("Falta caso de uso");
		}

		String[] packgMetdo = cuso.split("\\.");
		if (packgMetdo.length != 3) {
			throw new Exception("cuso malformado");
		}

		pack = packgMetdo[0];
		clas = packgMetdo[1];
		metd = packgMetdo[2];

		// prop = new Properties();
		String propArchi = "db.properties";
		InputStream is = getClass().getClassLoader().getResourceAsStream(propArchi);

		if (is != null)
			prop.load(is);
		else
			throw new Exception("No existe archivo properties");
		dbx.prop= this.prop;
		
		dvlp = prop.getProperty("dvlp") != null;
		dirLog = prop.getProperty("dir.log");
		
		dbx.iniConx();
		dbx.ejePs("set lc_monetary to 'C'", new String[] {});

		dbx.qry("select cast(nextval('sqlogcusos') as varchar) sscuso ");
		sscuso = (String) dbx.JSTblgetvCampo("sscuso");

		stx = (String) inData.get("stx");

		if (stx.equals("-1") && inData.has("usua"))
			usua = (String) inData.get("usua");


		// Llenamos usua y rol del registro
		if (!stx.equals("-1")) {
			lcuso = ""
					+ " select "
					+ "   usua, cast(rol as varchar) rol, ccusos0.lcuso "
					+ "  ,coalesce(cuso_alt,'') ejcuso "
					+ " from slogcusos,ccusos0 "
					+ " where id=? "
					+ "   and ccusos0.pack=? "
					+ "   and ccusos0.clas=? "
					+ "   and ccusos0.metd=? "
					+ "";
			dbx.qry(lcuso,
					new String[] {
							stx, "N",
							pack, "C",
							clas, "C",
							metd, "C"
					});

			if (dbx.gNumRows() == 0) {
				throw new Exception("Caso no declarado");
			} else {
				usua = (String) dbx.JSTblgetvCampo("usua");
				rol = (String) dbx.JSTblgetvCampo("rol");
				lcuso = (String) dbx.JSTblgetvCampo("lcuso");
				ejcuso = (String) dbx.JSTblgetvCampo("ejcuso");
			}
			if (rol.equals("-1") && inData.has("rol"))
				rol = (String) inData.get("rol");

			dbx.ejePs("insert into slogcusos (sesion,pack,clas,metd,usua,rol,id,ip,ejcuso) values (?,?,?,?,?,?,?,?,?)",
					new String[] {
							stx, "N",
							pack, "C",
							clas, "C",
							metd, "C",
							usua, "C",
							rol, "N",
							sscuso, "N",
							ipdom, "C",
							ejcuso, "C"
					});
			dbx.conx.commit();
			/// Insertamos tabla temporal con datos
			dbx.ejePs(""
					+ "create temporary table tempses ( "
					+ "  stx int4,"
					+ " usua varchar, "
					+ " rol int4, "
					+ " ip varchar, "
					+ " sscuso int4, "
					+ " ejcuso varchar) on commit drop " , new String[] {});
			String eje;
			eje = "insert into tempses (stx ,usua ,rol ,ip ,sscuso) values ("
					+ stx
					+ ",'" + usua + "'"
					+ ","+rol
					+ ",'" + ipdom + "'"
					+ ","+sscuso
					+ ")";

			dbx.ejePs(eje, new String[] {});

			if (!ejcuso.equals("")) {
				packgMetdo = ejcuso.split("\\.");
				if (packgMetdo.length != 3) {
					throw new Exception("cuso eje malformado");
				}
				pack = packgMetdo[0];
				clas = packgMetdo[1];
				metd = packgMetdo[2];
			}
		}

		/// Llenamos properties del caso
		propArchi = "cusos." + pack + "." + clas + ".properties";
		
		is = null;
		is = getClass().getClassLoader().getResourceAsStream(propArchi);
		if (is != null)
			prop.load(is);
		else
			prop = null;

		// ///// preLlenamos Salida
		outData.put("cuso", cuso); // Nombre del Caso de Uso
		outData.put("ejcuso", ejcuso); // Cuso que se Ejecuta
		outData.put("lcuso", lcuso); // Etiqueta del caso de Uso
		outData.put("stx", stx);
		outData.put("sscuso", sscuso);
		outData.put("lay", inData.get("lay"));
	} 

	public String ponInData(String sent) throws Exception {

		/// Parametros desde variables
		Iterator<String> ll = inData.keys();
		String llave, pllave = "", vllave, eje;
		ArrayList<String> condis = new ArrayList<String>();

		while (ll.hasNext()) {
			llave = ll.next();
			vllave = inData.getString(llave);
			pllave = llave;

			if ("".equals(pllave)) {
				continue;
			}

			if (sent.contains("@" + pllave)) {
				sent = sent.replace("@" + pllave, "'" + vllave.replace("'", "''") + "'");
			}
			if (sent.contains("#" + pllave)) {
				if ("".equals(vllave))
					sent = sent.replace("#" + pllave, "null ");
				else
					sent = sent.replace("#" + pllave, vllave.replace("$", "").replace(",", "")) ;
			}
		}

		sent = sent.replace("#Cuso.usua", usua);
		sent = sent.replace("#Cuso.rol", rol);

		// String qry= sent;
		String parm, vparm, oparm, condi, coma;
		int hasta = 0;

		// for (int desde = sent.indexOf("~"); desde != -1; desde = sent.indexOf("~")) {
		for (int desde = sent.indexOf("{"); desde != -1; desde = sent.indexOf("{")) {
			// hasta = sent.indexOf(".", desde);
			hasta = sent.indexOf("}", desde);
			if (hasta == -1) {
				throw new Exception("Parametro sin terminación");
			}
			parm = sent.substring(desde + 1, hasta);
			oparm = parm;
			coma = "";// like="";
			if (parm.contains("'"))
				coma = "'";
			// if (parm.contains("'%")) like="%";
			parm = parm.replace("'", "");
			// parm= parm.replace("%","");
			filtroLis.add("\""+parm+"\"");

			vparm = "";

			///// Desde Filtros
			if (inData.has("flt" + parm) && // Tiene el filtro
					!inData.get("flt" + parm).equals("") // el filtro tiene valor
			) {

				condi = (String) inData.get("flt" + parm);
				// coma = "";

				String[] aDato = condi.split("\\|");
				if (aDato.length > 2) {
					throw new Exception("Solo acepta dos rangos");
				}

				if (aDato.length > 0 && condi.contains("|") && coma.equals("")) { /////////// Rango Numeros o Fechas
					if (!aDato[0].equals("")) { // Mayor o igual que
						vparm = parm + " >= " + aDato[0];
					}
					if (aDato.length > 1) {
						vparm = vparm + (vparm.equals("") ? "" : " and ") + parm + " <= " + aDato[1];
					}
				} else if (aDato.length > 0 && coma.equals("'") && condi.startsWith("=")) { /// Cadena busqueda exacta
					vparm = parm + " = '" + condi.substring(1) + "'";
				} else if (aDato.length > 0 && coma.equals("'") && !condi.contains("%")) { /// Cadena, busca con Likes
					vparm = "UPPER(" + parm + ") like UPPER('%" + condi + "%')";
				} else if (condi.contains(",")) {///////////////////////////////////////// Lista
					vparm = parm + " in (" + condi + ")";
				} else if (condi.contains("%")) {///////////////////////////////////////// Igual con Like (cadena)
					vparm = parm + " like '" + condi + "'";
				} else { //////////////////////////////////////////////////////////// Igual
					vparm = parm + " = " + coma + condi + coma;

				}

				// vparm = "and " + vparm;
			}
			// sent = sent.replace("~" + oparm + ".", "");
			sent = sent.replace("{" + oparm + "}", "");
			// if (lisFiltroFld != null)
			condis.add(vparm);
		}

		// Aplicamos condis de filtro en lista

		int x = 0;
		eje = " where ";
		for (int i = 0; i < condis.size(); i++) {
			if (!condis.get(i).equals("")) {
				if (x > 0)
					eje = " and ";
				else {
					sent = " select * from ( " + sent + ") q ";
					x++;
				}
				sent += eje + " (" + condis.get(i) + ") ";
			}
		}

		return sent;
	}

}