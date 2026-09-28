package com.example.managecafe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/managecafe_db";
    private static final String USER = "root"; // replace with your MySQL username
    private static final String PASSWORD = "7099"; // replace with your MySQL password

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
