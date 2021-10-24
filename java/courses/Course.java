package courses;

import database.Database;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;


@ManagedBean
@RequestScoped
public class Course {

    PreparedStatement pstmt;
    private String name;
    private String id;
    private ArrayList<String> instructorslist;
    private ArrayList<String> studentsList;
    private ArrayList<String> selectInstructors;
    private ArrayList<String> selectStudents;

    
    public Course() {
        getInstructorsInCourseDB();
        getStudentsDB();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getInstructorslist() {
        return instructorslist;
    }

    public void setInstructorslist(ArrayList<String> instructorslist) {
        this.instructorslist = instructorslist;
    }

    public ArrayList<String> getStudentsList() {
        return studentsList;
    }

    public void setStudentsList(ArrayList<String> studentsList) {
        this.studentsList = studentsList;
    }

    public ArrayList<String> getSelectInstructors() {
        return selectInstructors;
    }

    public void setSelectInstructors(ArrayList<String> selectInstructors) {
        this.selectInstructors = selectInstructors;
    }

    public ArrayList<String> getSelectStudents() {
        return selectStudents;
    }

    public void setSelectStudents(ArrayList<String> selectStudents) {
        this.selectStudents = selectStudents;
    }

    
    private void createTable() {
        String sql = "create table " + name + "Course ( id varchar2(4), instructorid varchar2(4), studentId varchar2(4) )";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            pstmt.executeUpdate();

            CreateHwFolder();
            
            insertIntoCourseTable();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    
    private void insertIntoCourseTable() {
        String sql = "insert into courses (id, coursename, numHw) values (?, ?, ?)";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, 0);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    
    private void getInstructorsInCourseDB() {

        String sql = "select id, firstname, lastname from instructoraccounts";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            instructorslist = new ArrayList<String>();
            while (rs.next()) {
                instructorslist.add(rs.getString(1) + " - " + rs.getString(2) + " " + rs.getString(3));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    
    private void getStudentsDB() {

        String sql = "select id, firstname, lastname from studentaccounts";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            studentsList = new ArrayList<String>();
            while (rs.next()) {
                studentsList.add(rs.getString(1) + " - " + rs.getString(2) + " " + rs.getString(3));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    
    public String courseCreationProcess() {
        String ret = "";

        if (name.equals("") || id.equals("") || selectInstructors.size() == 0 || selectInstructors.size() > 2 || selectStudents.size() < 5 || selectStudents.size() > 30) {
            ret = "CourseCreationErrorPage";
        } else if (tableExists()) {
            ret = "CourseCreationErrorPage";
        } else {
            createTable();
            insertIntoDB();
            ret = "CourseCreatedPage";
        }
        return ret;
    }

    
    private boolean tableExists() {
        boolean ret = false;

        String sql = "select * from courses where id = " + id;
        try {
            pstmt = Database.getConnection().prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            studentsList = new ArrayList<String>();
            if (rs.next()) {
                ret = true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    
    private void insertIntoDB() {
        String sql = "insert into " + name + "course (id, instructorid) values (?, ?)";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            for (int i = 0; i < selectInstructors.size(); i++) {
                pstmt.setString(1, id);
                String instructorID = selectInstructors.get(i).substring(0, 4);
                pstmt.setString(2, instructorID);
                pstmt.executeUpdate();
            }

            sql = "insert into " + name + "course (id, studentid) values (?, ?)";
            pstmt = Database.getConnection().prepareStatement(sql);
            for (int i = 0; i < selectStudents.size(); i++) {
                pstmt.setString(1, id);
                String instructorID = selectStudents.get(i).substring(0, 4);
                pstmt.setString(2, instructorID);
                pstmt.executeUpdate();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    
    private void CreateHwFolder() {
        File dir = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\NetBeansProjects\\Final Project\\src\\HomeworkAssignment\\" + name);
        dir.mkdir();
    }

}
