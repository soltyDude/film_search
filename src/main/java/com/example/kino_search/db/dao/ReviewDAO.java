package com.example.kino_search.db.dao;

import com.example.kino_search.db.ConnectionManager;

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
    public static boolean addReview(int userId, int filmId, int rating, String reviewText) {
        String sql = """
        INSERT INTO reviews (user_id, film_id, rating, review_text) 
        VALUES (?, ?, ?, ?)
        """;
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, filmId);
            stmt.setInt(3, rating);
            stmt.setString(4, reviewText);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
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
