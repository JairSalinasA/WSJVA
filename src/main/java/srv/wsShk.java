package srv;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.io.StringWriter;
import java.io.PrintWriter;

@ServerEndpoint("/wsShk")
public class wsShk {

	/**
	 *
	 */

	@OnOpen
	public void abre() {
		System.out.println("Abre Conexión ...");
	}

	@OnClose
	public void cierra() {
		System.out.println("Cierra Conexión ...");
	}

	@OnMessage
	public String conecta(Session sesion, String datos) throws Exception {
		String regre = "", strex = "";
		cuso datc = null;

		// Session sesion.getRemoteAddress();

		try {
			datc = new cuso(datos);

			Object vars;
			Constructor<?> ct;
			Method ej;
			Class<?> cVars = Class.forName("cusos." + datc.pack + "." + datc.clas);
			ct = cVars.getConstructor(cuso.class);
			vars = ct.newInstance(datc);
			ej = vars.getClass().getDeclaredMethod(datc.metd);
			ej.invoke(vars);

			// ///// Ejecuta metodo final, si existe
			try {
				ej = vars.getClass().getDeclaredMethod("fin");
				ej.invoke(vars);
			} catch (Exception ex) {
				if (!ex.toString().startsWith("java.lang.NoSuchMethodException"))
					throw ex;
			}

			regre = (String) datc.outData.toString();
			if (datc != null && datc.dbx != null && datc.dbx.conx != null)
				datc.dbx.conx.commit();

		} catch (Exception ex) {
			regre = ex.toString();
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			strex = sw.toString();

			if (regre.contains("reflect.InvocationTargetException")) {
				regre = ex.getCause().toString();
				if (regre.contains("org.postgresql.util.PSQLException:")) {
					int det = regre.indexOf("Detail:");
					if (det == -1)
						det = regre.length();
					regre = regre.substring(41, det);
				}
				// regre=ex.getLocalizedMessage();
			}
			// strex = regre;
			regre = "{\"excp\":\"" + regre.replaceAll("\"", "'").replaceAll("\r", " ").replaceAll("\n", " ") + "\"}";
			if (datc != null && datc.dbx != null && datc.dbx.conx != null)
				datc.dbx.conx.rollback();
		} finally {
			if (datc != null && datc.dbx != null) { // && datc.dbx.conx != null) {
				/// close db
				if (datc.dbx.conx != null) {
					datc.dbx.ejePs("update slogcusos set fin=now(),excep=? where id=?",
							new String[] {
									strex, "C",
									datc.sscuso, "N"
							});
					datc.dbx.conx.commit();
					datc.dbx.conx.close();				
				}
				// close ssh
				if (datc.dbx.ssh != null) {
					datc.dbx.ssh.disconnect();
				}
			}
		}
		return regre;
	}

	@OnError
	public void noPudo(Throwable e) {
		e.printStackTrace();
	}

}
