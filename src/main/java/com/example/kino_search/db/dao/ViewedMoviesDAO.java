package com.example.kino_search.db.dao;

import com.example.kino_search.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewedMoviesDAO {

    private static final Logger logger = Logger.getLogger(ViewedMoviesDAO.class.getName());

    // Добавление фильма в просмотренные
    public static boolean addMovieToViewed(int userId, int filmId, Integer reviewId) {
        String sql = """
            INSERT INTO viewed_movies (user_id, film_id, reviews_id) 
            VALUES (?, ?, ?) 
            ON CONFLICT (user_id, film_id) DO NOTHING
            """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, filmId);
            if (reviewId != null) {
                stmt.setInt(3, reviewId);
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }

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

    // Удаление фильма из просмотренных
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

    // Проверка, существует ли фильм в просмотренных
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

    // Получение списка просмотренных фильмов для пользователя
    public static List<Map<String, Object>> getViewedMoviesByUserId(int userId) {
        String sql = """
        SELECT v.viewed_at, f.api_id, f.title, f.poster_url, r.rating
        FROM viewed_movies v
        JOIN film f ON v.film_id = f.id
        LEFT JOIN reviews r ON r.user_id = v.user_id AND r.film_id = v.film_id
        WHERE v.user_id = ?
        ORDER BY v.viewed_at DESC
    """;

        List<Map<String, Object>> viewedMovies = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> movie = new HashMap<>();
                    movie.put("viewed_at", rs.getTimestamp("viewed_at"));
                    movie.put("apiId", rs.getInt("api_id"));
                    movie.put("title", rs.getString("title"));
                    movie.put("poster_url", rs.getString("poster_url"));

                    int rating = rs.getInt("rating");
                    if (rs.wasNull()) {
                        movie.put("rating", null); // или можно просто не класть ключ, если нет рейтинга
                    } else {
                        movie.put("rating", rating);
                    }

                    viewedMovies.add(movie);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving viewed movies for user ID " + userId, e);
        }

        return viewedMovies;
    }


    // Обновление записи о просмотренном фильме (например, добавление review_id)
    public static boolean updateViewedMovie(int userId, int filmId, Integer reviewId) {
        String sql = """
            UPDATE viewed_movies
            SET reviews_id = ?
            WHERE user_id = ? AND film_id = ?
        """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (reviewId != null) {
                stmt.setInt(1, reviewId);
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }
            stmt.setInt(2, userId);
            stmt.setInt(3, filmId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Viewed movie updated: User ID = " + userId + ", Film ID = " + filmId + ", Review ID = " + reviewId);
                return true;
            } else {
                logger.info("No viewed movie found to update: User ID = " + userId + ", Film ID = " + filmId);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating viewed movie", e);
        }
        return false;
    }
}
