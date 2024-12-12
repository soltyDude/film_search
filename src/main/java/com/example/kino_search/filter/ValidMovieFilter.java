package com.example.kino_search.filter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ValidMovieFilter implements MovieFilter {

    @Override
    public List<Map<String, String>> filter(List<Map<String, String>> movies) {
        return movies.stream()
                .filter(movie -> {
                    String title = movie.get("title");
                    String posterUrl = movie.get("poster_url");

                    // Проверяем, чтобы у фильма были название и постер
                    return title != null && !title.isEmpty() &&
                            posterUrl != null && !posterUrl.isEmpty();
                })
                .collect(Collectors.toList());
    }
}
