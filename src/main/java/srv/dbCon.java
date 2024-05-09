package srv;

//import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
////
import com.jcraft.jsch.*;
////

//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.Properties;

//import com.mysql.cj.jdbc.Clob;
import java.util.Properties;
import org.json.JSONArray;
import org.json.JSONObject;
//import java.util.Set;

public class dbCon {

	public String excep = "";
	public int wait = 60;
	public String params;
	public int mxRegs = 100; // Registros por fetch (0 es sin limite)
	public Connection conx;
	public JSONObject jsLocTbl;	
	public int nrol;
	public Session ssh;
	public Properties prop = new Properties();

	private boolean iniBar = false;

	public dbCon() throws Exception {
	}

	public void iniConx() throws Exception {
		int conxPort=1234;
		String dbHost = prop.getProperty("bd.host");
		String sshHost = prop.getProperty("bd.sshHost");

		if (sshHost != null) {	
			String sshUsr = prop.getProperty("bd.sshUsr");
			String sshPwd = prop.getProperty("bd.sshPwd");						
			JSch jsch = new JSch();
			ssh = jsch.getSession(sshUsr, sshHost, Integer.parseInt(prop.getProperty("bd.sshPort")));
			ssh.setPassword(sshPwd);

			// //--> out con ssh
			 jsch.addIdentity(prop.getProperty("file.prvkey"),
			 prop.getProperty("file.passphrase"));

			 ssh.setConfig("StrictHostKeyChecking", "no");
			 ssh.connect();
			 ssh.setPortForwardingL(conxPort, dbHost, Integer.parseInt(prop.getProperty("bd.port")));

		} else
			conxPort = Integer.parseInt(prop.getProperty("bd.port"));

		String strConx = "jdbc:" + prop.getProperty("bd.jdbc") + "://" + prop.getProperty("bd.host") + ":" + conxPort + "/" + prop.getProperty("bd.bdatos");
		Class.forName(prop.getProperty("bd.cl"));
		conx = DriverManager.getConnection(strConx, prop.getProperty("bd.usr"), prop.getProperty("bd.pwd"));
		conx.setAutoCommit(false);
	}

	public JSONArray pejeQry(String eje, String[] flds) throws Exception {

		JSONArray tabla = new JSONArray();
		JSONArray data = new JSONArray();
		JSONArray meta = new JSONArray();

		JSONArray mColName = new JSONArray();
		JSONArray mColTipo = new JSONArray();
		JSONArray regis;

		// ArrayList regis;
		PreparedStatement ps = conx.prepareStatement(eje);
		ponParam(ps, flds);
		ps.setQueryTimeout(wait);
		ps.setMaxRows(mxRegs);

		ResultSet rs = ps.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();

		// mHead.put(cols);
		/// Nombres
		// metaRegis = new JSONArray();
		for (int i = 0; i < cols; i++) {
			mColName.put(rsmd.getColumnName(i + 1));
			mColTipo.put(rsmd.getColumnTypeName(i + 1));
		}

		meta.put(mColName);
		meta.put(mColTipo);

		// rsmd.getColumnType(cols);
		while (rs.next()) {
			Object val;
			regis = new JSONArray();

			for (int i = 0; i < cols; i++) {
				val = rs.getObject(i + 1);

				// regis.put(val);
				regis.put((val == null ? "" : val));
			}
			data.put(regis);
		}
		ps.close();
		tabla.put(meta);
		tabla.put(data);
		return tabla;
	}

	// Ejecuta Local
	public void qry(String eje) throws Exception {
		qry(eje, new String[] {});
	}

	public void qry(String eje, String[] flds) throws Exception {
		jsLocTbl = ejeQry(eje, flds);
	}

	// Barre
	public boolean barre() throws Exception {
		return barre(jsLocTbl);
	}

	public boolean barre(String eje) throws Exception {
		if (!iniBar)
			qry(eje);
		iniBar = barre();
		return iniBar;
	}

	public boolean barre(JSONObject tabla) throws Exception {
		int numRows = gNumRows(tabla);
		boolean regre = tabla != null && numRows > 0;
		int recNum = (Integer) tabla.get("recNum");

		if (regre && numRows == recNum + 1) { // Esta en EOF, reinicia puntero
			tabla.put("recNum", -1);
			regre = false;
		}
		if (regre) {
			recNum++;
			tabla.put("recNum", recNum);
		}
		return regre;
	}

	// Ejecuta Cualquiera
	public JSONObject ejeQry(String eje) throws Exception {
		return ejeQry(eje, new String[] {});
	}

	public JSONObject ejeQry(String eje, String[] flds) throws Exception {
		PreparedStatement ps = conx.prepareStatement(eje);
		ponParam(ps, flds);
		ps.setQueryTimeout(wait);

		ResultSet rs = ps.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		// Clob clob = rs.getClob("nomb_column"); //getBlob("As");//
		// .getClob("DETAILED_DESCRIPTION")

		int cols = rsmd.getColumnCount();
		JSONObject JSTable = new JSONObject();

		String[] aCols = new String[cols]; // Nombre
		String[] cCols = new String[cols]; // Clase (tipo) de columna
		for (int i = 0; i < cols; i++) {
			aCols[i] = rsmd.getColumnName(i + 1);
			cCols[i] = rsmd.getColumnClassName(i + 1);
		}

		JSTable.put("cols", aCols); // Nombre de columna
		JSTable.put("ccols", cCols); // Tipos de Columna
		JSONArray aDatos = new JSONArray();
		JSTable.put("datos", aDatos); // los Datos
		JSTable.put("recNum", -1);

		while (rs.next()) {
			Object[] aRegis = new Object[cols];
			for (int i = 0; i < cols; i++) {
				aRegis[i] = rs.getObject(i + 1);
			}
			JSTable.append("datos", aRegis);
		}

		return JSTable;
	}

	public int ejePs(String eje, String[] flds) throws Exception {
		return ejePs(eje, flds, conx);
	}

	public int ejePs(String eje, String[] flds, Connection tconx)
			throws Exception {
		PreparedStatement ps = tconx.prepareStatement(eje);
		ponParam(ps, flds);

		int regre = ps.executeUpdate();
		ps.close();
		return regre;
	}

	public void ponParam(PreparedStatement ps, String[] flds) throws Exception {
		int ix;
		String Fld, Tipo;
		for (int i = 0; i < flds.length; i++) {
			Fld = flds[i];
			i++;
			Tipo = flds[i];
			ix = (i + 1) / 2;
			if (Tipo.equals("I")) {
				if (Fld.equals("")) {
					ps.setNull(ix, java.sql.Types.INTEGER);
				} else {
					ps.setInt(ix, Integer.parseInt(Fld));
				}
			}
			if (Tipo.equals("C")) {
				if (Fld.equals("")) {
					ps.setNull(ix, java.sql.Types.VARCHAR);
				} else {
					// Fld = Fld.replace("CONEXION", "CONEXIÓN")
					// .replace("conexion", "conexión").replace("Conexion",
					// "Conexión");
					ps.setString(ix, Fld);
				}
			}
			if (Tipo.equals("N")) {
				if (Fld.equals("")) {
					ps.setNull(ix, java.sql.Types.DOUBLE);
				} else {
					ps.setDouble(ix, Double.parseDouble(Fld.replace("$", "")
							.replace(",", "").replace("%", "")));
				}
			}
			if (Tipo.equals("F")) {
				if (Fld.equals("")) {
					ps.setNull(ix, java.sql.Types.DATE);
				} else {
					ps.setDate(ix, java.sql.Date
							.valueOf(Fld.substring(6, 10) + "-"
									+ Fld.substring(3, 5) + "-"
									+ Fld.substring(0, 2)));
				}
			}
		}

	}

	////////////////// ***************/////////////////////////

	// public Object JSTblgetvCampo(JSONObject tabla, int regNum, String nCampo)
	// throws Exception {
	// return JSTblgetvCampo(tabla, regNum, JSTblgetNCampo(tabla, nCampo));
	// }

	public Object gFlds(String fld) {

		return false;
	};

	public Object JSTblgetReg(int regNum)
			throws Exception {
		return ((JSONArray) jsLocTbl.get("datos")).get(regNum);
	}

	public Object JSTblgetReg(JSONObject tabla, int regNum)
			throws Exception {
		return ((JSONArray) tabla.get("datos")).get(regNum);
	}

	public int JSTblgetNCampo(JSONObject tabla, String nCampo)
			throws Exception {
		int reg = -1;
		String[] nCampos = (String[]) tabla.get("cols");
		// Arrays.asList(nCampos).forEach();

		for (reg = 0; reg < nCampos.length; reg++) {
			if (nCampos[reg].equals(nCampo)) {
				break;
			}
		}
		return (reg == nCampos.length) ? -1 : reg;
	}

	///// --------------------------------------------------------------------------------->////
	public Object JSTblgetvCampo(JSONObject tabla, int regNum, int nCampo)
			throws Exception {
		return ((Object[]) ((JSONArray) tabla.get("datos")).get(regNum))[nCampo];
	}
	////////////////////////////////////////////////////////////////////////////////////////

	public Object JSTblgetvCampo(JSONObject tabla, int regNum, String nCampo) // String nCampo
			throws Exception {
		return JSTblgetvCampo(tabla, regNum, JSTblgetNCampo(tabla, nCampo));
	}

	public Object JSTblgetvCampo(JSONObject tabla, String nCampo) // registro 0
			throws Exception {
		return JSTblgetvCampo(tabla, 0, nCampo);
	}

	public Object JSTblgetvCampo(String nCampo) // local registro 0 String nCampo
			throws Exception {
		return JSTblgetvCampo(jsLocTbl, 0, nCampo);
	}

	public Object JSTblgetvCampo(int regNum, int nCampo) // Local
			throws Exception {
		return JSTblgetvCampo(jsLocTbl, regNum, nCampo);
	}

	public Object JSTblgetvCampo(int reg, String nCampo) // Local
			throws Exception {
		return JSTblgetvCampo(jsLocTbl, reg, nCampo);
	}

	///// <---------------------------------------------------------------------------------////

	///// --------------------------------------------------------------------------------->////

	public Object valFld(JSONObject tabla, int numCampo) // String nCampo
			throws Exception {
		int recNum = (Integer) tabla.get("recNum");
		if (recNum == -1 && gNumRows(tabla) > 0) {
			recNum = 0;
			tabla.put("recNum", 0);
		}
		;
		return JSTblgetvCampo(tabla, recNum, numCampo);
	}

	public Object valFld(JSONObject tabla, String nCampo) // String nCampo
			throws Exception {
		return valFld(tabla, JSTblgetNCampo(tabla, nCampo));
	}

	////////////////////////////////////////////////////////////////////////////////////////
	public Object valFld(int numCampo) // String nCampo
			throws Exception {
		return valFld(jsLocTbl, numCampo);
	}

	public Object valFld(String nCampo) // String nCampo
			throws Exception {
		return valFld(jsLocTbl, nCampo);
	}

	public String valStrFld(JSONObject tabla, String nCampo) // String nCampo
			throws Exception {
		return (String) valFld(tabla, nCampo);
	}

	public String valStrFld(String nCampo) // String nCampo
			throws Exception {
		return (String) valFld(jsLocTbl, nCampo);
	}

	///// <---------------------------------------------------------------------------------////

	///// --------------------------------------------------------------------------------->////
	public int gNumRows(JSONObject tabla) throws Exception {
		return ((JSONArray) tabla.get("datos")).length();
	}

	////////////////////////////////////////////////////////////////////////////////////////
	public int gNumRows() throws Exception {
		return gNumRows(jsLocTbl);
	}
	///// <---------------------------------------------------------------------------------////

}
