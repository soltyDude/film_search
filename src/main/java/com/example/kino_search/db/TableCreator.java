package com.example.kino_search.db;

import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableCreator {

    private static final Logger logger = Logger.getLogger(TableCreator.class.getName());

    public static void createUsersTable() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                nickname VARCHAR(50) NOT NULL,
                email VARCHAR(100) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;

        try (Connection connection = ConectionManager.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createTableSQL);
            logger.info("Table 'users' created successfully or already exists.");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating table 'users'", e);
        }
    }

    public static void main(String[] args) {
        createUsersTable();
    }
}
