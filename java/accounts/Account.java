package accounts;

import java.sql.PreparedStatement;

/**
 *
 * @author Peter Sun
 */
public abstract class Account {

    String firstName;
    String lastName;
    String userName;
    String password;
    String id;
    PreparedStatement pstmt;

    abstract boolean isInDatabase();

}
//accounts need to 
// have a first and last name
// have a username and password