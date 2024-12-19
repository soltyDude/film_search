package com.example.kino_search.db.dao;

import com.example.kino_search.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object (DAO) class for managing the relationship between genres and films.
 * This class provides functionality to save a genre-film association into the database.
 */
public class GenreFilmDAO {

    private static final Logger logger = Logger.getLogger(GenreFilmDAO.class.getName());

    /**
     * Saves the relationship between a genre and a film into the database.
     * If the relationship already exists, it will not create a duplicate due to "ON CONFLICT DO NOTHING".
     *
     * @param genreId The ID of the genre.
     * @param filmId The ID of the film.
     * @return true if the relationship was successfully added, false if it already exists or an error occurred.
     */
    public static boolean saveGenreFilm(int genreId, int filmId) {
        // Validate input parameters
        if (genreId <= 0 || filmId <= 0) {
            logger.severe("Invalid genreId or filmId for saving genre-film relation: Genre ID = " + genreId + ", Film ID = " + filmId);
            throw new IllegalArgumentException("Invalid genreId or filmId.");
        }

        logger.info("Starting to save genre-film relation: Genre ID = " + genreId + ", Film ID = " + filmId);

        // SQL query to insert the genre-film relationship
        String sql = "INSERT INTO genre_film (genre_id, film_id) VALUES (?, ?) ON CONFLICT DO NOTHING";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set query parameters
            stmt.setInt(1, genreId);
            stmt.setInt(2, filmId);

            // Execute the query
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Relationship successfully added
                logger.info("Genre-film relation saved successfully: Genre ID = " + genreId + ", Film ID = " + filmId);
                return true;
            } else {
                // Relationship already exists
                logger.info("Genre-film relation already exists: Genre ID = " + genreId + ", Film ID = " + filmId);
                return false;
            }
        } catch (SQLException e) {
            // Log the exception and return false
            logger.log(Level.SEVERE, "Error occurred while saving genre-film relation: Genre ID = " + genreId + ", Film ID = " + filmId, e);
            return false;
        }
    }

    /**
     * Example of a potential method to remove the genre-film relationship.
     * Uncomment and implement this method if needed.
     *
     * @param genreId The ID of the genre.
     * @param filmId The ID of the film.
     * @return true if the relationship was successfully removed, false otherwise.
     */
//    public static boolean removeGenreFilm(int genreId, int filmId) {
//        String sql = "DELETE FROM genre_film WHERE genre_id = ? AND film_id = ?";
//
//        try (Connection conn = ConnectionManager.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, genreId);
//            stmt.setInt(2, filmId);
//            int rowsAffected = stmt.executeUpdate();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            logger.log(Level.SEVERE, "Error occurred while removing genre-film relation: Genre ID = " + genreId + ", Film ID = " + filmId, e);
//            return false;
//        }
//    }
}
