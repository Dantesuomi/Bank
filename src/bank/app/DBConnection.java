package bank.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() throws SQLException {
        String dbURL = "jdbc:mysql://localhost:3306/bank";
        String username = "root";
        String password = "0865";
        return DriverManager.getConnection(dbURL, username, password);
    }


}
