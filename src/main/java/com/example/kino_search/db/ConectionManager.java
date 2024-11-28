package com.example.kino_search.db;

import com.example.kino_search.property.PropertyManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConectionManager {
    private static final Logger logger = Logger.getLogger(ConectionManager.class.getName());
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Получаем параметры подключения из PropertyManager
                String url = PropertyManager.getProperty("db.url");
                String user = PropertyManager.getProperty("db.username");
                String password = PropertyManager.getProperty("db.password");

                // Устанавливаем соединение
                connection = DriverManager.getConnection(url, user, password);
                logger.info("Connection to the database established successfully");

            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Failed to connect to the database", e);
                throw new RuntimeException("Error connecting to the database", e);
            }
        } else {
            logger.info("Reusing existing database connection");
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Database connection closed successfully");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to close the database connection", e);
        }
    }
}