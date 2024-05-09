package servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import java.util.Properties;

//@WebServlet("/UploadDownloadFileServlet")
@WebServlet("/UpLdFile")
public class UploadDownloadFileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    // private ServletFileUpload uploader = null;
    private String host;
    private String ruta;
    private String docBase;
    // ServletContext ctx;

    @Override
    public void init() throws ServletException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new ServletException("Content type is not multipart/form-data");
        }

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/html");

        // ---------------------------------------//
        // ----- Establecemos parametros --------//
        // ---------------------------------------//

        Properties prop = new Properties();
        String propFileName = "db.properties";
        InputStream is = getClass().getClassLoader().getResourceAsStream(propFileName);
        prop.load(is);

        host = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        ruta = prop.getProperty("archis.ruta");
        docBase = prop.getProperty("archis.docBase");

        // ------------------------------------- //
        // ------------------------------------- //
        // ------------------------------------- //

        DiskFileItemFactory fileFactory = new DiskFileItemFactory();
        ServletFileUpload uploader = new ServletFileUpload(fileFactory);
        File filesDir = new File(docBase);
        fileFactory.setRepository(filesDir);

        PrintWriter out = response.getWriter();
        JSONObject resp = new JSONObject();

        try {
            List<FileItem> fileItemsList = uploader.parseRequest(request);
            Iterator<FileItem> fileItemsIterator = fileItemsList.iterator();
            String nomArchi = "";

            while (fileItemsIterator.hasNext()) {
                FileItem fileItem = fileItemsIterator.next();
                nomArchi = fileItem.getName();
                String extension = FilenameUtils.getExtension(nomArchi);

                // JSONObject seq = getSequenceFile(extension);
                int seq = getSequenceFile(extension);
                nomArchi = Integer.toString(seq) + "." + extension;

                // nomArchi = seq.getString("Filename").toString();
                // int sequence = seq.getInt("Sequence");

                File file = new File(docBase + File.separator + nomArchi);
                fileItem.write(file);

                // resp.put("code", 1);
                resp.put("id", seq);
                // resp.put("docBase", docBase);
                // resp.put("ruta", ruta);
                resp.put("url", host + "/" + ruta + "/" + nomArchi);

                out.write(resp.toString());
            }
        } catch (Exception e) {
            // resp.put("code", -1);
            resp.put("excep", e.getMessage());
            out.write(resp.toString());
        }
    }

    private int getSequenceFile(String extension) throws SQLException, Exception {
        int Sequence = 0;
        String Filename = "";
        comun.ConnectionDataBase db = new comun.ConnectionDataBase();
        db.OpenConnection();
        Exception falla=null;
        try {

            ResultSet res;
            String resQ;
            res = db.getQuery("SELECT host,ruta from thosts where host='" + host + "' and ruta='" + ruta + "'");
            if (!res.next()) {
                resQ = db.executeQuery("INSERT INTO thosts (host, ruta ,docbase) VALUES ('" + host + "' ,'" + ruta
                        + "','" + docBase + "')");
                if (!resQ.equals("1"))
                    throw new Exception(resQ);

            }

            res = db.getQuery("SELECT nextval('tfiles_seq')");
            res.next();
            Sequence = res.getInt(1);

            Filename = Sequence + "." + extension;

            resQ = "INSERT INTO tfiles (archi, FILENAME ,HOST ,RUTA) VALUES (" + Sequence + ",'"
                    + Filename + "', '" + host + "' ,'" + ruta + "')";
            resQ = db.executeQuery(resQ);
            if (!resQ.equals("1"))
                throw new Exception(resQ);

            db.Commit();
        } catch (Exception e) {
            db.Rollback();
            falla=e;
//            Sequence= -1;
        } finally {
            db.CloseConnection();
        }

        if (falla!=null) throw falla;
        // JSONObject data = new JSONObject();
        // data.put("Sequence", Sequence);
        // data.put("Filename", Filename);

        return Sequence;
    }

}
