package pages;

import accounts.StudentAccount;
import database.Database;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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


@WebServlet(name = "StudentUploadHwPage", urlPatterns = {"/StudentUploadHwPage"})
public class StudentUploadHwPage extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet StudentUploadHwPage</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet StudentUploadHwPage at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        StudentAccount account = (StudentAccount) session.getAttribute("Student");
        ArrayList<String> courses = account.getCourses();


        out.println("<form method=\"post\" action =\"" + request.getContextPath() + "/StudentUploadHwPage\">");


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

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String courseName = request.getParameter("courseRegIn");
        out.print("<b><u>" + courseName + "</u> </b>");
        int hwAmount = getNumOfHW(request.getParameter("courseRegIn"));
        

        out.print("<form action=\"UploadServlet\" method=\"post\" enctype=\"multipart/form-data\"> </td>");
        out.print("Reminder: File should include first and last name:");
        if (hwAmount == 0) {
            out.print("<table border=\"1\">");
            out.print("<tr>");
            out.print("<td> Homework # </td>");
            out.print("<td> Homework Description </td>");
            out.print("<td> Upload Homework </td>");
            out.print("</tr>");
            out.print("<tr>");
            out.print("<td> # </td>");
            out.print("<td> No Homework assigned </td>");
            out.print("<td> No Homework assigned </td>");
            out.print("</tr>");
            out.print("</table>");
        } else {
            out.print("<table border=\"1\">");
            out.print("<tr>");
            out.print("<td> Homework # </td>");
            out.print("<td> Homework Description </td>");
            out.print("<td> Upload Homework <input type=\"file\"  name=\"file\"/> </td>");
            out.print("</tr>");

            for (int i = 0; i < hwAmount; i++) {
                out.print("<tr>");
                out.print("<td>HW # " + (i + 1) + "</td>");
                out.print("<td> " + getHwDescription(courseName, (i + 1)) + " </td>");
                out.print("<td> <input type=\"submit\" value=\"Upload File\" /> </td>");
                out.print("</tr>");
            }
            out.print("</table>");
            out.print("</form>");
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

    
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
        }

        return ret;
    }

    
    private String getHwDescription(String courseName, int hwNumber) {
        String path = "C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\NetBeansProjects\\Final Project\\src\\HomeworkAssignment\\" + courseName;
        String ret = "Not Available";

        String files;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                files = listOfFiles[i].getName();
                if (files.endsWith(".txt") || files.endsWith(".TXT")) {
                    if (listOfFiles[i].getName().equals("HW" + hwNumber + ".txt")) {
                        try {

                            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(listOfFiles[i])));
                            String line = "";
                            ret = "";
                            while ((line = reader.readLine()) != null) {
                                ret += line;
                                ret += "\n";
                            }

                        } catch (FileNotFoundException ex) {
                            ret += "filenothere";
                        } catch (IOException ex) {
                            ret += "ioexception";
                        }
                    }
                }
            }
        }

        return ret;
    }
}
