package com.github.ehealthcaremanagement.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionConfig {
    private static final String URL = "jdbc:mysql://localhost:3307/e-healthcare management database";
    private static final String USER = "Abdussalam";       // change per user
    private static final String PASSWORD = "Abdussalam123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}




