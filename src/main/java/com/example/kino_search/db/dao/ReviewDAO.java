package com.example.kino_search.db.dao;

import com.example.kino_search.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReviewDAO {

    private static final Logger logger = Logger.getLogger(ReviewDAO.class.getName());

    public static int addPlaceholderReview(int userId, int filmId) {
        String sql = """
                INSERT INTO reviews (user_id, film_id, rating, review_text)
                VALUES (?, ?, ?, ?)
                RETURNING id
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, filmId);
            stmt.setInt(3, 0); // Default rating (can be updated later)
            stmt.setString(4, ""); // Default review_text (can be updated later)

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int reviewId = rs.getInt("id");
                logger.info("Placeholder review added. Review ID: " + reviewId);
                return reviewId;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding placeholder review", e);
        }
        return -1;
    }
}
