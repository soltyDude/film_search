package com.example.kino_search.db;

import com.example.kino_search.property.PropertyManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager {
    private static final Logger logger = Logger.getLogger(ConnectionManager.class.getName());
    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Получаем параметры подключения из PropertyManager
                String url = PropertyManager.getProperty("db.url");
                String user = PropertyManager.getProperty("db.username");
                String password = PropertyManager.getProperty("db.password");

                // Создаем новое соединение
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(url, user, password);
                logger.info("Connection to the database established successfully");
            } else {
                logger.info("Reusing existing database connection");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to connect to the database", e);
            throw new RuntimeException("Error connecting to the database", e);
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "PostgreSQL Driver not found", e);
            throw new RuntimeException("PostgreSQL Driver not found", e);        }
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