package com.example.kino_search.db.dao;

import com.example.kino_search.db.ConnectionManager;
import com.example.kino_search.util.TMDBApiUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAO {

    /**
     * Получает никнейм пользователя по его ID.
     *
     * @param userId ID пользователя.
     * @return Никнейм пользователя или null, если пользователь не найден.
     */
    public static String getUserNicknameById(int userId) {
        String query = "SELECT nickname FROM users WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("nickname");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Логирование ошибки
            System.err.println("Error fetching user nickname for userId " + userId + ": " + e.getMessage());
        }
        return null; // Если пользователь не найден или произошла ошибка
    }
    /**
     * Получает список фильмов, которым пользователь поставил оценку выше 5
     * и просмотрел не позднее полутора лет назад (исключая фильмы с оценкой 9 и 10).
     *
     * @param userId ID пользователя.
     * @return Список массивов, содержащих [title, user_review_rating, date_when_watched].
     */
    public static List<Object[]> getRecommendedBaseMovies(int userId) {
        String query = """
        SELECT f.title, r.rating, vm.viewed_at
        FROM viewed_movies vm
        INNER JOIN reviews r ON vm.reviews_id = r.id
        INNER JOIN film f ON vm.film_id = f.id
        WHERE vm.user_id = ?
          AND r.rating > 5
          AND (vm.viewed_at >= NOW() - INTERVAL '1.5 years' OR r.rating >= 9)
    """;

        List<Object[]> movies = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String title = resultSet.getString("title");
                    int rating = resultSet.getInt("rating");
                    String viewedAt = resultSet.getTimestamp("viewed_at").toString();

                    movies.add(new Object[]{title, rating, viewedAt});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error fetching recommended base movies for userId " + userId + ": " + e.getMessage());
        }

        return movies;
    }

    //
    public static List<Map<String, String>> getRecommendedMovies(int userId) {
        List<Map<String, String>> recommendedMovies = new ArrayList<>();

        // Получаем базовый список фильмов
        List<Object[]> baseMovies = UserDAO.getRecommendedBaseMovies(userId);

        for (Object[] baseMovie : baseMovies) {
            String title = (String) baseMovie[0];
            int rating = (int) baseMovie[1];
            String viewedAtStr = (String) baseMovie[2];

            // Рассчитываем "вес" фильма на основе даты просмотра
            int weight = calculateWeight(viewedAtStr);

            // Учитываем рейтинг, чтобы определить количество похожих фильмов
            int similarCount = Math.max(0, rating + weight);

            // Запрашиваем похожие фильмы из TMDB API
            List<Map<String, String>> similarMovies = fetchSimilarMoviesFromTMDB(title, similarCount);

            // Добавляем уникальные фильмы в итоговый список
            for (Map<String, String> movie : similarMovies) {
                if (!isMovieInList(recommendedMovies, movie)) {
                    recommendedMovies.add(movie);
                }
            }
        }

        return recommendedMovies;
    }

    public static int calculateWeight(String viewedAtStr) {
        LocalDate viewedAt = LocalDate.parse(viewedAtStr.substring(0, 10)); // Парсинг даты
        LocalDate now = LocalDate.now();

        long monthsSinceViewed = ChronoUnit.MONTHS.between(viewedAt, now);

        // Рассчитываем вес: каждое полугодие (6 месяцев) уменьшает вес на 1
        return (int) Math.floor(-monthsSinceViewed / 6.0);

    }

    public static List<Map<String, String>> fetchSimilarMoviesFromTMDB(String title, int count) {
        List<Map<String, String>> similarMovies = new ArrayList<>();

        try {
            String endpoint = "/search/movie?query=" + URLEncoder.encode(title, StandardCharsets.UTF_8);
            com.google.gson.JsonObject jsonResponse = TMDBApiUtil.sendRequest(endpoint);

            JsonArray results = jsonResponse.getAsJsonArray("results");

            for (int i = 0; i < Math.min(count, results.size()); i++) {
                JsonObject movieJson = results.get(i).getAsJsonObject();

                Map<String, String> movie = new HashMap<>();
                movie.put("id", movieJson.get("id").getAsString());
                movie.put("title", movieJson.get("title").getAsString());

                if (movieJson.has("poster_path") && !movieJson.get("poster_path").isJsonNull()) {
                    movie.put("poster_url", "https://image.tmdb.org/t/p/w500" + movieJson.get("poster_path").getAsString());
                    similarMovies.add(movie);
                }
//                else {
//                    movie.put("poster_url", "https://via.placeholder.com/500x750?text=No+Image");
//                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return similarMovies;
    }

    private static boolean isMovieInList(List<Map<String, String>> movieList, Map<String, String> movie) {
        for (Map<String, String> existingMovie : movieList) {
            if (existingMovie.get("id").equals(movie.get("id"))) {
                return true;
            }
        }
        return false;
    }


}