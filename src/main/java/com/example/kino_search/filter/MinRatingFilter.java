package com.example.kino_search.filter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MinRatingFilter implements MovieFilter {

    private final double minRating;

    public MinRatingFilter(double minRating) {
        this.minRating = minRating;
    }

    @Override
    public List<Map<String, String>> filter(List<Map<String, String>> movies) {
        return movies.stream()
                .filter(movie -> {
                    String apiRating = movie.get("api_rating");
                    if (apiRating == null || apiRating.isEmpty()) {
                        return false; // Исключаем фильмы без рейтинга
                    }
                    return Double.parseDouble(apiRating) >= minRating;
                })
                .collect(Collectors.toList());
    }
}
