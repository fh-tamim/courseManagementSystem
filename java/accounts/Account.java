package accounts;

import java.sql.PreparedStatement;


public abstract class Account {

    String firstName;
    String lastName;
    String userName;
    String password;
    String id;
    PreparedStatement pstmt;

    abstract boolean isInDatabase();

}