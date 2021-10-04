/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pages;

import accounts.StudentAccount;
import database.Database;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
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
@WebServlet(name = "StudentCoursePage", urlPatterns = {"/StudentCoursePage"})
public class StudentCoursePage extends HttpServlet {

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
            out.println("<title>Servlet StudentCoursePage</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet StudentCoursePage at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession();
        StudentAccount account = (StudentAccount) session.getAttribute("Student");
        ArrayList<String> courses = account.getCourses();

        out.println("<form method=\"post\" action =\"" + request.getContextPath() + "/StudentCoursePage\">");

        out.println("<p>Courses Currently registered in: ");
        out.print("<select name = \"courseRegIn\" size = \"1\">");
        for (int i = 0; i < courses.size(); i++) {
            out.print("<option value = " + courses.get(i) + ">" + courses.get(i) + "</option>");
        }
        out.print("</select>");

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
        StudentAccount account = (StudentAccount) session.getAttribute("Student");
        String courseName = request.getParameter("courseRegIn");
        out.print(courseName);
        int hwAmount = getNumOfHW(courseName);

        if (hwAmount == 0) {
            out.print("<table border=\"1\">");
            out.print("<tr>");
            out.print("<td> Homework # </td>");
            out.print("<td> Homework Grade </td>");
            out.print("</tr>");
            out.print("<tr>");
            out.print("<td> No Homework assigned </td>");
            out.print("<td> <center> # </center> </td>");
            out.print("</tr>");
            out.print("</table>");
        } else {
            out.print("<table border=\"1\">");
            out.print("<tr>");
            out.print("<td> Homework # </td>");
            out.print("<td> Homework Grade </td>");
            out.print("</tr>");

            for (int i = 0; i < hwAmount; i++) {
                out.print("<tr>");
                out.print("<td>HW # " + (i + 1) + "</td>");
                if(getHwGrade(account, courseName, (i + 1)) == null) {
                    out.print("<td>PENDING</td>");
                } else
                    out.print("<td>" + getHwGrade(account, courseName, (i + 1)) + "</td>");
                out.print("</tr>");
            }
            out.print("</tr>");
            out.print("</table>");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    /**
     * Get total amount of homework assigned for course
     * @param courseName Course to look for
     * @return Total amount of homework assigned for specified class
     */
    private int getNumOfHW(String courseName) {
        int ret = 0;
        PreparedStatement pstmt;
        String sql = "select * from " + courseName + "course";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            ret = rsmd.getColumnCount() - 3;

        } catch (SQLException ex) {
            ex.printStackTrace();
            ret = 100;
        }

        return ret;
    }

    /**
     * Returns homework grade
     * @param account Student Id
     * @param courseName Course to look for
     * @param hwNumber Homework number
     * @return 
     */
    private String getHwGrade(StudentAccount account, String courseName, int hwNumber) {
        String ret = "";
        PreparedStatement pstmt;
        String sql = "select Hw" + hwNumber + " from " + courseName + "course where studentid = \'" + account.getId() + "\'";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ret = rs.getString(1);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return ret;
    }
}
