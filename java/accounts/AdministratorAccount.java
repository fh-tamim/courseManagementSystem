/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
@ManagedBean(name = "account")
@SessionScoped
public class AdministratorAccount {

    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String id;
    private PreparedStatement pstmt;

    /**
     * Creates a new instance of AdministratorAccount
     */
    public AdministratorAccount() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    /**
     * As long as fields are not blank and information is correct, login
     * administrator
     *
     * @return Page to go to
     */
    public String validateAdministrator() {
        String ret = "";
        if (userName.equals("") || password.equals("")) {
            ret = "AdministratorNotValidatePage";
        } else {
            if (isInDatabase()) {
                ret = "AdministratorValidatePage";
            } else {
                ret = "AdministratorNotFoundPage";
            }
        }
        return ret;
    }

    /**
     * Check if administrator is in database
     *
     * @return True if administrator is in database
     */
    public boolean isInDatabase() {
        boolean ret = false;

        String sql = "select id, lastname, firstname from AdministratorAccounts where username = ? and password = ?";
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

    /**
     * Setup all fields of administrator account
     *
     * @param rs Result set from query
     */
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
