package com.example.kino_search.db.dao.interfaces;

import java.util.List;
import java.util.Map;

public interface IViewedMoviesDAO {
    boolean addMovieToViewed(int userId, int filmId, Integer reviewId);
    boolean removeMovieFromViewed(int userId, int filmId);
    boolean isMovieInViewed(int userId, int filmId);
    List<Map<String, Object>> getViewedMoviesByUserId(int userId);
    boolean updateViewedMovie(int userId, int filmId, Integer reviewId);
}
