package com.example.kino_search.db.dao;

import com.example.kino_search.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewedMoviesDAO {

    private static final Logger logger = Logger.getLogger(ViewedMoviesDAO.class.getName());

    // добавление в просмотренные
    public static boolean addMovieToViewed(int userId, int filmId, Integer reviewId) {
        String sql = """
            INSERT INTO viewed_movies (user_id, film_id) 
            VALUES (?, ?) 
            ON CONFLICT (user_id, film_id) DO NOTHING
            """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, filmId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Movie added to viewed_movies: User ID = " + userId + ", Film ID = " + filmId);
                return true;
            } else {
                logger.info("Movie already exists in viewed_movies: User ID = " + userId + ", Film ID = " + filmId);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding movie to viewed_movies", e);
        }
        return false;
    }


    // Remove a movie from the "viewed_movies" table
    public static boolean removeMovieFromViewed(int userId, int filmId) {
        String sql = "DELETE FROM viewed_movies WHERE user_id = ? AND film_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, filmId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Movie removed from viewed_movies: User ID = " + userId + ", Film ID = " + filmId);
                return true;
            } else {
                logger.info("No movie found to remove in viewed_movies: User ID = " + userId + ", Film ID = " + filmId);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error removing movie from viewed_movies", e);
        }
        return false;
    }

    // Check if a movie exists in the "viewed_movies" table for a user
    public static boolean isMovieInViewed(int userId, int filmId) {
        String sql = "SELECT 1 FROM viewed_movies WHERE user_id = ? AND film_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, filmId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                logger.info("Movie exists in viewed_movies: User ID = " + userId + ", Film ID = " + filmId);
                return true;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error checking if movie exists in viewed_movies", e);
        }
        return false;
    }
}
