package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/car_rental";
    private static final String USER = "root";          // change as needed
    private static final String PASSWORD = "your_pass"; // change as needed

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // ensure driver loaded
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
