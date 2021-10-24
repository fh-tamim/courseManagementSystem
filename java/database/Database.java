package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Database {

    private final static String className = "oracle.jdbc.driver.OracleDriver";
    private final static String url = "jdbc:oracle:thin:@localhost:1521:xe";
    private final static String user = "system";
    private final static String password = "tiger";
    private static Connection connection;

    
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName(className);
                connection = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return connection;
    }
}
