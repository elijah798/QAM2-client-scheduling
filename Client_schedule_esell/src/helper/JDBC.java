package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcURL = protocol + vendor + location + databaseName + "?serverTimezone=UTC-3"; //local
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "sqlUser";
    private static String password = "Passw0rd!";
    public static Connection connection;


    public static void openConnection() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(jdbcURL, userName, password);




        } catch (ClassNotFoundException e) {
            System.out.println("Class not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed");
        } catch (Exception e) {
            System.out.println("SQL Exception");
        }
    }



}
