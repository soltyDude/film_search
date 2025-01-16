package com.example.kino_search.db.dao.interfaces;

import java.util.List;
import java.util.Map;

public interface IUserDAO {
    String getUserNicknameById(int userId);
    List<Object[]> getRecommendedBaseMovies(int userId);
    List<Map<String, String>> getRecommendedMovies(int userId);
    int calculateWeight(String viewedAtStr);
    List<Map<String, String>> fetchSimilarMoviesFromTMDB(int apiId, int count);
}
