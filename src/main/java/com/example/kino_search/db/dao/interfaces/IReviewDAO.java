package com.example.kino_search.db.dao.interfaces;

import java.util.List;
import java.util.Map;

public interface IReviewDAO {
    boolean addReview(int userId, int filmAPIId, int rating, String reviewText);
    boolean isReviewExists(int userId, int filmId);
    List<Map<String, Object>> getReviewsByFilmId(int filmId);
    Map<String, Object> getReviewByUserAndFilm(int userId, int filmId);
    boolean updateReview(int userId, int filmId, int newRating, String newReviewText);
}
