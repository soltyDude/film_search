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
        RETURNING id
    """;

        String updateViewedMovieSql = """
        UPDATE viewed_movies
        SET reviews_id = ?
        WHERE user_id = ? AND film_id = ?
    """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement reviewStmt = conn.prepareStatement(reviewSql);
             PreparedStatement updateViewedMovieStmt = conn.prepareStatement(updateViewedMovieSql)) {

            int filmId = FilmService.getFilmIdByApiId(filmAPIId);

            // Добавляем отзыв
            reviewStmt.setInt(1, userId);
            reviewStmt.setInt(2, filmId);
            reviewStmt.setInt(3, rating);
            reviewStmt.setString(4, reviewText);

            int reviewId = -1;
            try (ResultSet rs = reviewStmt.executeQuery()) {
                if (rs.next()) {
                    reviewId = rs.getInt("id");
                }
            }

            if (reviewId == -1) {
                throw new SQLException("Failed to retrieve the generated review ID.");
            }

            // Обновляем поле reviews_id в просмотренных фильмах
            updateViewedMovieStmt.setInt(1, reviewId);
            updateViewedMovieStmt.setInt(2, userId);
            updateViewedMovieStmt.setInt(3, filmId);
            int viewedMovieRowsAffected = updateViewedMovieStmt.executeUpdate();

            logger.info("Review added and movie's reviews_id updated: User ID = " + userId + ", Film ID = " + filmId + ", Review ID = " + reviewId + ", Rating = " + rating);

            // Поскольку обновление рейтинга убрано, проверим только успешность добавления отзыва.
            return viewedMovieRowsAffected > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding review and updating viewed_movie's reviews_id", e);
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
                    int userId = rs.getInt("user_id");
                    String nickname = UserDAO.getUserNicknameById(userId);

                    review.put("user_id", userId);
                    review.put("user_nickname", nickname);
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

    public static Map<String, Object> getReviewByUserAndFilm(int userId, int filmId) {
        String sql = "SELECT rating, review_text FROM reviews WHERE user_id = ? AND film_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, filmId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> review = new HashMap<>();
                    review.put("rating", rs.getInt("rating"));
                    String reviewText = rs.getString("review_text");
                    review.put("review_text", reviewText);
                    return review;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(ReviewDAO.class.getName()).log(Level.SEVERE, "Error fetching review by user and film", e);
        }
        return null;
    }

    public static boolean updateReview(int userId, int filmId, int newRating, String newReviewText) {
        // Обновляем отзыв
        String updateReviewSql = """
        UPDATE reviews
        SET rating = ?, review_text = ?, updated_at = CURRENT_TIMESTAMP
        WHERE user_id = ? AND film_id = ?
    """;

        // После обновления отзыва нужно пересчитать рейтинг фильма.
        // Посчитаем заново средний рейтинг и количество отзывов для этого фильма.
        String avgSql = "SELECT AVG(rating) as avg_rating, COUNT(*) as cnt FROM reviews WHERE film_id = ?";

        String updateFilmSql = """
        UPDATE film
        SET rating = ?, count = ?
        WHERE id = ?
    """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement updateReviewStmt = conn.prepareStatement(updateReviewSql);
             PreparedStatement avgStmt = conn.prepareStatement(avgSql);
             PreparedStatement updateFilmStmt = conn.prepareStatement(updateFilmSql)) {

            updateReviewStmt.setInt(1, newRating);
            updateReviewStmt.setString(2, newReviewText);
            updateReviewStmt.setInt(3, userId);
            updateReviewStmt.setInt(4, filmId);

            int reviewRows = updateReviewStmt.executeUpdate();
            if (reviewRows == 0) {
                // Не найден отзыв для обновления
                return false;
            }

            // Пересчёт среднего рейтинга
            avgStmt.setInt(1, filmId);
            double avgRating = 0;
            int count = 0;
            try (ResultSet rs = avgStmt.executeQuery()) {
                if (rs.next()) {
                    avgRating = rs.getDouble("avg_rating");
                    count = rs.getInt("cnt");
                }
            }

            // Обновляем рейтинг фильма
            updateFilmStmt.setDouble(1, avgRating);
            updateFilmStmt.setInt(2, count);
            updateFilmStmt.setInt(3, filmId);
            updateFilmStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            Logger.getLogger(ReviewDAO.class.getName()).log(Level.SEVERE, "Error updating review and film rating", e);
        }
        return false;
    }


}
