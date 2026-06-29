package com.github.ehealthcaremanagement.config;
/*
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
}*/

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectionConfig {

    private static String url;
    private static String user;
    private static String password;

    static {
        try (InputStream input = DBConnectionConfig.class
                .getClassLoader()
                .getResourceAsStream("dbconfig.properties")) {

            if (input == null) {
                throw new RuntimeException("Unable to find dbconfig.properties");
            }

            Properties prop = new Properties();
            prop.load(input);

            url = prop.getProperty("db.url");
            user = prop.getProperty("db.user");
            password = prop.getProperty("db.password");

        } catch (IOException ex) {
            throw new RuntimeException("Error loading database configuration", ex);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}





