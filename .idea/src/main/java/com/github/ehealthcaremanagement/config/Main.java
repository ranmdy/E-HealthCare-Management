package com.github.ehealthcaremanagement.config;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Testing Database Connection ===");

        try {
            // Call your DBConnectionConfig class
            Connection conn = DBConnectionConfig.getConnection();

            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Connection successful!");
                conn.close(); // Always close when done
            } else {
                System.out.println("❌ Connection failed.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


