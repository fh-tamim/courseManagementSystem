/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pages;

import accounts.StudentAccount;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Peter Sun
 */
@WebServlet(name = "StudentPage", urlPatterns = {"/StudentPage"})
public class StudentLoginPage extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet StudentPage</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet StudentPage at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<form method=\"post\" action =\"" + request.getContextPath() + "/StudentPage\">");

        out.println("Log In Here");
        out.println("<p>User Name:");
        out.print("<input type = \"text\" name = \"username\" size = \"20\">");
        out.println("<p>Password:");
        out.print("<input type = \"text\" name = \"password\" size = \"20\">");

        out.println("<p><input type=\"submit\" value=\"Confirm\" >");
        out.println("</form>");
        out.println("</body></html>");
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username.equals("") || password.equals("")) {
            out.println("Username and password required");
        } else {

            StudentAccount student = new StudentAccount(username, password);
            
            try {
                if (student.isInDatabase()) {
                    
                    session.setAttribute("Student", student);

                    out.println("<table border=\"0\" style=\"background-color:\" width=\"100%\" cellpadding=\"3\" cellspacing=\"3\">");
                    out.println("<tr>");
                    out.println("<td>Welcome " + student.getFirstName() + " " + student.getLastName() + "</td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td>ID: " + student.getId() + "</td>");
                    out.println("</tr>");
                    out.println("<td><a href=\"StudentCoursePage\">Check Courses</a> </td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td> <a href=\"StudentUploadHwPage\">Upload HW</a> </td>");
                    out.println("</tr>");
                    out.println("</table>");
                    out.println("</form>");
                } else {
                    out.println("Sorry We could not find you");
                    out.print("</br>");
                    out.println("Please contact your instructor to ensure you are in the system");
                    out.println("<p><form method=\"get\" action=" + "StudentPage>");
                    out.println("<p><input type=\"submit\" value=\"Go Back\" >");
                    out.println("</form>");
                }
            } catch (Exception ex) {
                out.print(ex.getMessage());
            }
        }

    }
}
