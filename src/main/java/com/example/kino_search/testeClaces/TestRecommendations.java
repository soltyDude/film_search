package com.example.kino_search.testeClaces;

import com.example.kino_search.db.dao.UserDAO;

import java.util.List;
import java.util.Map;

public class TestRecommendations {
    public static void main(String[] args) {
        System.out.println("Begin Recommendations Test");

        int userId = 8; // Укажите тестовый ID пользователя

        // Получаем базовые рекомендации
        List<Object[]> baseMovies = UserDAO.getRecommendedBaseMovies(userId);

        System.out.println("Base Recommendations and Similar Movie Counts:");
        for (Object[] baseMovie : baseMovies) {
            String title = (String) baseMovie[0];
            int rating = (int) baseMovie[1];
            String viewedAt = (String) baseMovie[2];

            // Рассчитываем вес фильма
            int weight = UserDAO.calculateWeight(viewedAt);
            System.out.println("weight: " + weight);
            System.out.println("rating: " + rating);
            int similarCount = Math.max(0, rating + weight);

            // Запрашиваем похожие фильмы
            List<Map<String, String>> similarMovies = UserDAO.fetchSimilarMoviesFromTMDB(title, similarCount);

            // Выводим название базового фильма и количество похожих
            System.out.println("Base Movie: " + title);
            System.out.println("Similar Movies Count: " + similarCount);
            System.out.println("----------------------------");
            System.out.println();
            System.out.println();

            for (Map<String, String> similarMovie : similarMovies) {
                String movieTitle = (String) similarMovie.get("title");
                System.out.println("Title: " + movieTitle);
                System.out.println("--");
            }
        }

        System.out.println("End Recommendations Test");
    }
}
