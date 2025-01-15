package com.example.kino_search.db;

import com.example.kino_search.property.PropertyManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager {
    private static final Logger logger = Logger.getLogger(ConnectionManager.class.getName());
    private static volatile ConnectionManager instance;

    // Private constructor to prevent instantiation
    private ConnectionManager() {}

    /**
     * Returns the singleton instance of the GenreFilmDAO class.
     * Uses double-checked locking for thread safety.
     *
     * @return The singleton instance of GenreFilmDAO.
     */
    public static ConnectionManager getInstance() {
        if (instance == null) {
            synchronized (ConnectionManager.class) {
                if (instance == null) {
                    instance = new ConnectionManager();
                }
            }
        }
        return instance;
    }

    static {
        try {
            // Explicitly load the PostgreSQL driver
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "PostgreSQL Driver not found", e);
        }
    }

    public Connection getConnection() throws SQLException {
        // Load database connection properties
        String dbUrl = PropertyManager.getProperty("db.url");
        String dbUser = PropertyManager.getProperty("db.username");
        String dbPassword = PropertyManager.getProperty("db.password");

        logger.info("Connecting to the database: " + dbUrl);
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
}
