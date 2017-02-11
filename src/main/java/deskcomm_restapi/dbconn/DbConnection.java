package deskcomm_restapi.dbconn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 08-01-2017.
 */
public class DbConnection {
    private DbConnection() {

    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/deskcomm", "root", "");
    }
}
