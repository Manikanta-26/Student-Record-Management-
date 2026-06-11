package com.studentrecords.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing MySQL database connections via JDBC.
 * Uses a singleton-style approach to provide a reusable connection.
 */
public class DBConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/student_records_db?useSSL=false&serverTimezone=UTC";
    private static final String USER     = "root";       // Change as needed
    private static final String PASSWORD = "your_password"; // Change as needed

    private static Connection connection = null;

    // Private constructor — no instantiation
    private DBConnection() {}

    /**
     * Returns an active JDBC connection, creating one if needed.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("[DBConnection] Connected to MySQL successfully.");
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found. Add mysql-connector-java to classpath.", e);
            }
        }
        return connection;
    }

    /**
     * Closes the current connection if open.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DBConnection] Connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("[DBConnection] Error closing connection: " + e.getMessage());
        }
    }
}
