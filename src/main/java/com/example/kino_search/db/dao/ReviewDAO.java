package com.example.kino_search.db.dao;

import com.example.kino_search.db.ConnectionManager;
import com.example.kino_search.db.FilmService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReviewDAO {

    private static final Logger logger = Logger.getLogger(ReviewDAO.class.getName());
    public static boolean addReview(int userId, int filmAPIId, int rating, String reviewText) {
        String reviewSql = """
            INSERT INTO reviews (user_id, film_id, rating, review_text) 
            VALUES (?, ?, ?, ?)
        """;

        String updateFilmSql = """
            UPDATE film
            SET 
                rating = (rating * count + ?) / (count + 1),
                count = count + 1
            WHERE id = ?
        """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement reviewStmt = conn.prepareStatement(reviewSql);
             PreparedStatement updateFilmStmt = conn.prepareStatement(updateFilmSql)) {

            int filmId = FilmService.getFilmIdByApiId(filmAPIId);

            // Добавляем отзыв
            reviewStmt.setInt(1, userId);
            reviewStmt.setInt(2, filmId);
            reviewStmt.setInt(3, rating);
            reviewStmt.setString(4, reviewText);
            int reviewRowsAffected = reviewStmt.executeUpdate();

            // Обновляем данные фильма
            updateFilmStmt.setInt(1, rating);
            updateFilmStmt.setInt(2, filmId);
            int filmRowsAffected = updateFilmStmt.executeUpdate();

            logger.info("Review added and film rating updated: User ID = " + userId + ", Film ID = " + filmId + ", Rating = " + rating);
            return reviewRowsAffected > 0 && filmRowsAffected > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding review and updating film rating", e);
            return false;
        }
    }

    public static boolean isReviewExists(int userId, int filmId) {
        String sql = """
        SELECT 1 
        FROM reviews 
        WHERE user_id = ? AND film_id = ?
    """;
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, filmId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Если результат есть, отзыв существует
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error checking if review exists", e);
        }
        return false;
    }


    public static List<Map<String, Object>> getReviewsByFilmId(int filmId) {
        String sql = """
        SELECT user_id, rating, review_text, created_at 
        FROM reviews WHERE film_id = ?
    """;
        List<Map<String, Object>> reviews = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, filmId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> review = new HashMap<>();
                    review.put("user_id", rs.getInt("user_id"));
                    review.put("rating", rs.getInt("rating"));
                    review.put("review_text", rs.getString("review_text"));
                    review.put("created_at", rs.getTimestamp("created_at"));
                    reviews.add(review);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

}
