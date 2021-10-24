package accounts;

import database.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class StudentAccount extends Account {

    private ArrayList<String> courses;

    public StudentAccount() {
    }

    public StudentAccount(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getId() {
        return id;
    }

    
    public ArrayList<String> getCourses() {
        String sql = "select coursename from courses";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            courses = new ArrayList<String>();
            while(rs.next()) {
                sql = "select * from " + rs.getString(1) + "course where studentid = " + id;
                pstmt = Database.getConnection().prepareStatement(sql);
                ResultSet studentInClassRS = pstmt.executeQuery();
                if(studentInClassRS.next()) {
                    courses.add(rs.getString(1));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return courses;
    }

    
    @Override
    public boolean isInDatabase() {
        boolean ret = false;

        String sql = "select id, lastname, firstname from StudentAccounts where username = ? and password = ?";
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
}
