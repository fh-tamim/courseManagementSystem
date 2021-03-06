package accounts;

import database.Database;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class InstructorAccount extends Account {

    private ArrayList<String> courses;
    private ArrayList<String> students;
    private int hwNumber;

    public InstructorAccount() {
    }

    public void setLogInInfo(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

 
    public ArrayList<String> getCourses() {
        String sql = "select coursename from courses";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            courses = new ArrayList<String>();
            while (rs.next()) {
                sql = "select * from " + rs.getString(1) + "course where instructorid = " + id;
                pstmt = Database.getConnection().prepareStatement(sql);
                ResultSet instructorInClassRS = pstmt.executeQuery();
                if (instructorInClassRS.next()) {
                    courses.add(rs.getString(1));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return courses;
    }

   
    public ArrayList<String> getStudents(String course) {
        String sql = "select studentid from " + course + "course";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            students = new ArrayList<String>();
            while (rs.next()) {
                sql = "select id, firstname, lastname from studentaccounts where id = " + rs.getString(1) ;
                pstmt = Database.getConnection().prepareStatement(sql);
                ResultSet studentInClass = pstmt.executeQuery();
                if (studentInClass.next()) {
                    students.add(studentInClass.getString(1) + " - " + studentInClass.getString(2) + " " + studentInClass.getString(3));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return students;
    }

    
    @Override
    public boolean isInDatabase() {
        boolean ret = false;

        String sql = "select id, lastname, firstname from InstructorAccounts where username = ? and password = ?";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            pstmt.setString(1, this.userName);
            pstmt.setString(2, this.password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                initializeAccount(rs);
                ret = true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    
    private void initializeAccount(ResultSet rs) {

        try {
            this.id = rs.getString(1);
            this.lastName = rs.getString(2);
            this.firstName = rs.getString(3);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    
    public void assignHw(String course, String homeworkAssignment) {
        hwNumber = getHwNumberFromDb(course);
        setHwNumberInDB(course, ++hwNumber);
        appendHwToCourseTable(course);

        Writer output = null;
        File file = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\NetBeansProjects\\Final Project\\src\\HomeworkAssignment\\" + course + "\\HW" + hwNumber + ".txt");
        try {
            output = new BufferedWriter(new FileWriter(file));
            output.write(homeworkAssignment);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    
    private void appendHwToCourseTable(String course) {
        String sql = "alter table " + course + "course add Hw" + hwNumber + " int";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    
    public int getHwNumberFromDb(String course) {
        int ret = 0;
        String sql = "select numHw from courses where coursename = \'" + course + "\'";
        //String sql = "select numHw from courses where coursename = 'Bio'";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ret = rs.getInt("numHw");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    
    private void setHwNumberInDB(String course, int numberOfHw) {
        String sql = "update courses set numHw = " + Integer.toString(numberOfHw) + " where coursename = \'" + course + "\'";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
    public void setGradeInDB(String student, String course, String hw, String grade) {
        String sql = "update " + course + "course set " + hw + " = \'" + grade + "\' where studentid = \'" + student + "\'";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
