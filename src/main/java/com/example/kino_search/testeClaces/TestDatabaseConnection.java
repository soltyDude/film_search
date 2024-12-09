package com.example.kino_search.testeClaces;

import com.example.kino_search.db.ConnectionManager;

import java.sql.Connection;
import java.util.logging.Logger;

public class TestDatabaseConnection {

    private static final Logger logger = Logger.getLogger(TestDatabaseConnection.class.getName());

    public static void main(String[] args) {
        try {
            logger.info("Starting database connection test...");
            Connection connection = ConnectionManager.getConnection();

            if (connection != null) {
                logger.info("Database connection test successful!");
                //ConnectionManager.closeConnection();
            }
        } catch (Exception e) {
            logger.severe("Database connection test failed: " + e.getMessage());
        }
    }
}
