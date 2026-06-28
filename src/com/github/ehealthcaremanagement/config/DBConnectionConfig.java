package com.github.ehealthcaremanagement.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionConfig {
    public class DBConnection {
        private static final String URL = "jdbc:mysql://localhost:3306/e-healthcare management database";
        private static final String USER = "root";
        private static final String PASSWORD = "";

        public static Connection connect() {
        try {
            return DriverManager.getConnection(URL,USER,PASSWORD);
        } catch (SQLException e) {
            System.err.println("Check your URL,USER or PASSWORD");
        }return connect();
    }
    }
}
