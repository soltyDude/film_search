package com.example.kino_search.db.dao;

import com.example.kino_search.db.ConnectionManager;
import com.example.kino_search.util.TMDBApiUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {

    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());

    /**
     * Retrieves the user's nickname by their ID.
     *
     * @param userId User ID.
     * @return The user's nickname or null if not found.
     */
    public static String getUserNicknameById(int userId) {
        logger.log(Level.INFO, "Fetching nickname for userId: {0}", userId);
        String query = "SELECT nickname FROM users WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String nickname = resultSet.getString("nickname");
                    logger.log(Level.INFO, "Found nickname for userId {0}: {1}", new Object[]{userId, nickname});
                    return nickname;
                } else {
                    logger.log(Level.WARNING, "No nickname found for userId {0}", userId);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching user nickname for userId " + userId, e);
        }
        return null;
    }

    /**
     * Retrieves a list of movies that the user rated above 5 and watched not later than 1.5 years ago,
     * or movies with a rating of 9 or 10 regardless of the time. This serves as a base for recommendations.
     * Now also retrieves f.api_id to use TMDB ID directly for similar movies.
     *
     * @param userId User ID.
     * @return A list of Object[] where each array contains [title, api_id, rating, viewed_at].
     */
    public static List<Object[]> getRecommendedBaseMovies(int userId) {
        logger.log(Level.INFO, "Fetching recommended base movies for userId: {0}", userId);

        String query = """
            SELECT f.title, f.api_id, r.rating, vm.viewed_at
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
                    int apiId = resultSet.getInt("api_id");
                    int rating = resultSet.getInt("rating");
                    String viewedAt = resultSet.getTimestamp("viewed_at").toString();
                    movies.add(new Object[]{title, apiId, rating, viewedAt});
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching recommended base movies for userId " + userId, e);
        }

        logger.log(Level.INFO, "Found {0} base movies for userId {1}", new Object[]{movies.size(), userId});
        return movies;
    }

    /**
     * Retrieves a list of recommended movies for a user.
     *
     * Logic:
     * - Start with base movies from getRecommendedBaseMovies().
     * - For each base movie, calculate a weight based on how fresh the rating is.
     *   Fresher rating -> higher positive weight -> more similar movies.
     * - Number of similar movies = (rating + weight)*3, at least 5.
     * - Fetch similar movies using TMDB's /movie/{api_id}/similar endpoint.
     * - Add unique results to the final recommendation list.
     */
    public static List<Map<String, String>> getRecommendedMovies(int userId) {
        logger.log(Level.INFO, "Calculating recommended movies for userId: {0}", userId);
        List<Map<String, String>> recommendedMovies = new ArrayList<>();

        // Get the base list of user-rated movies (with api_id)
        List<Object[]> baseMovies = UserDAO.getRecommendedBaseMovies(userId);

        for (Object[] baseMovie : baseMovies) {
            String title = (String) baseMovie[0];
            int apiId = (int) baseMovie[1];
            int rating = (int) baseMovie[2];
            String viewedAtStr = (String) baseMovie[3];

            int weight = calculateWeight(viewedAtStr);
            int similarCount = (rating + weight) * 3;
            similarCount = Math.max(similarCount, 5);

            logger.log(Level.INFO, "Base movie: {0} (apiId={1}, rating={2}, viewedAt={3}) -> weight={4}, similarCount={5}",
                    new Object[]{title, apiId, rating, viewedAtStr, weight, similarCount});

            // Fetch similar movies from TMDB using movie id
            List<Map<String, String>> similarMovies = fetchSimilarMoviesFromTMDB(apiId, similarCount);

            // Add unique movies
            for (Map<String, String> movie : similarMovies) {
                if (!isMovieInList(recommendedMovies, movie)) {
                    recommendedMovies.add(movie);
                }
            }
        }

        logger.log(Level.INFO, "Total recommended movies for userId {0}: {1}", new Object[]{userId, recommendedMovies.size()});
        return recommendedMovies;
    }

    /**
     * Calculates the weight based on how recent the rating is.
     * More recent rating (fewer months since viewed) -> higher positive weight.
     *
     * 0–5 months: +3
     * 6–11 months: +2
     * 12–17 months: +1
     * 18+ months: +0
     */
    public static int calculateWeight(String viewedAtStr) {
        LocalDate viewedAt = LocalDate.parse(viewedAtStr.substring(0, 10));
        LocalDate now = LocalDate.now();
        long monthsSinceViewed = ChronoUnit.MONTHS.between(viewedAt, now);

        int weight;
        if (monthsSinceViewed < 6) {
            weight = 3;
        } else if (monthsSinceViewed < 12) {
            weight = 2;
        } else if (monthsSinceViewed < 18) {
            weight = 1;
        } else {
            weight = 0;
        }

        logger.log(Level.FINE, "Calculated weight={0} for viewedAt={1} (monthsSinceViewed={2})",
                new Object[]{weight, viewedAtStr, monthsSinceViewed});

        return weight;
    }

    /**
     * Fetches similar movies from TMDB using /movie/{apiId}/similar endpoint.
     * 'count' determines how many results to take from the fetched list.
     */
    public static List<Map<String, String>> fetchSimilarMoviesFromTMDB(int apiId, int count) {
        logger.log(Level.INFO, "Fetching similar movies for apiId={0}, requested count={1}", new Object[]{apiId, count});
        List<Map<String, String>> similarMovies = new ArrayList<>();

        try {
            String endpoint = "/movie/" + apiId + "/similar";
            JsonObject jsonResponse = TMDBApiUtil.sendRequest(endpoint);
            JsonArray results = jsonResponse.getAsJsonArray("results");

            int actualCount = Math.min(count, results.size());
            logger.log(Level.INFO, "TMDB returned {0} results for movieId {1}, using {2}",
                    new Object[]{results.size(), apiId, actualCount});

            for (int i = 0; i < actualCount; i++) {
                JsonObject movieJson = results.get(i).getAsJsonObject();
                Map<String, String> movie = new HashMap<>();
                movie.put("id", movieJson.get("id").getAsString());
                movie.put("title", movieJson.get("title").getAsString());

                if (movieJson.has("poster_path") && !movieJson.get("poster_path").isJsonNull()) {
                    movie.put("poster_url", "https://image.tmdb.org/t/p/w500" + movieJson.get("poster_path").getAsString());
                    similarMovies.add(movie);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching similar movies from TMDB for apiId=" + apiId, e);
        }

        logger.log(Level.INFO, "Fetched {0} similar movies for apiId={1}", new Object[]{similarMovies.size(), apiId});
        return similarMovies;
    }

    /**
     * Checks if a movie is already in the list.
     */
    private static boolean isMovieInList(List<Map<String, String>> movieList, Map<String, String> movie) {
        String movieId = movie.get("id");
        for (Map<String, String> existingMovie : movieList) {
            if (existingMovie.get("id").equals(movieId)) {
                return true;
            }
        }
        return false;
    }

}
