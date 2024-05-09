package servlet ;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class hola extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
      
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Hola pucos</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Hola putoooosxx!</h1>");
        out.println("</body>");
        out.println("</html>");
    }
}