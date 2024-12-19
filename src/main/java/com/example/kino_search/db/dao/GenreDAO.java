package com.example.kino_search.db.dao;

import com.example.kino_search.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object (DAO) class for managing genres in the database.
 * This class provides functionality to save a genre or retrieve its ID by name.
 */
public class GenreDAO {

    private static final Logger logger = Logger.getLogger(GenreDAO.class.getName());

    /**
     * Saves a genre to the database or retrieves its ID if it already exists.
     * This method uses the SQL "ON CONFLICT" clause to handle duplicate entries.
     *
     * @param genreName The name of the genre to save or retrieve.
     * @return The ID of the genre if successfully found or inserted, otherwise -1.
     */
    public static int saveOrGetGenreId(String genreName) {
        // SQL query to insert a genre (ignoring duplicates)
        String sqlInsert = "INSERT INTO genre (name) VALUES (?) ON CONFLICT (name) DO NOTHING";

        // SQL query to retrieve the genre ID by name
        String sqlSelect = "SELECT id FROM genre WHERE name = ?";

        try (Connection conn = ConnectionManager.getConnection()) {
            // Insert the genre into the database if it doesn't already exist
            try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsert)) {
                insertStmt.setString(1, genreName);
                insertStmt.executeUpdate(); // Insert operation with no result expected
                logger.info("Insert operation completed for genre: " + genreName);
            }

            // Retrieve the ID of the genre
            try (PreparedStatement selectStmt = conn.prepareStatement(sqlSelect)) {
                selectStmt.setString(1, genreName);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        int genreId = rs.getInt("id");
                        logger.info("Genre ID retrieved successfully: " + genreId);
                        return genreId;
                    } else {
                        logger.severe("Genre not found after insert/update: " + genreName);
                        throw new SQLException("Failed to retrieve Genre ID.");
                    }
                }
            }

        } catch (SQLException e) {
            // Log the error and return -1 to indicate failure
            logger.log(Level.SEVERE, "Error occurred while saving or retrieving genre: " + genreName, e);
        }

        return -1; // Return -1 if an error occurred
    }

}
