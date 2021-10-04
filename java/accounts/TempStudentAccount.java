
package accounts;

import database.Database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Peter Sun
 */
@ManagedBean(name = "tempStudentAccount")
@SessionScoped
public class TempStudentAccount {

    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String id;
    private PreparedStatement pstmt;
    private String confirmUserName;
    private String confirmPassword;

    /**
     * Creates a new instance of TempStudentAccount
     */
    public TempStudentAccount() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getConfirmUserName() {
        return confirmUserName;
    }

    public void setConfirmUserName(String confirmUserName) {
        this.confirmUserName = confirmUserName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String ConfirmPassword) {
        this.confirmPassword = ConfirmPassword;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public String registrationProcess() {
        String ret = "";

        if ((!userName.equals(confirmUserName)) || (!password.equals(confirmPassword))) {
            ret = "StudentCreationErrorPage";
        } else {
            if (isInDatabase()) {
                ret = "StudentCreationErrorPage";
            } else {
                insertStudentIntoDB();
                ret = "StudentCreatedPage";
            }
        }
        return ret;
    }

    public boolean isInDatabase() {
        boolean ret = false;

        String sql = "select id, lastname, firstname from StudentAccounts where username = ? and password = ?";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            pstmt.setString(1, this.userName);
            pstmt.setString(2, this.password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ret = true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    private void insertStudentIntoDB() {
        String sql = "insert into StudentAccounts(id, lastname, firstname, username, password) values (?, ?, ?, ?, ?)";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            pstmt.setString(1, this.id);
            pstmt.setString(2, this.lastName);
            pstmt.setString(3, this.firstName);
            pstmt.setString(4, this.userName);
            pstmt.setString(5, this.password);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
}
