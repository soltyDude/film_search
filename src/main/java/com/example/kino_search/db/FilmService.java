package com.example.kino_search.db;

import com.example.kino_search.db.dao.FilmDAO;
import com.example.kino_search.db.dao.GenreDAO;
import com.example.kino_search.db.dao.GenreFilmDAO;
import com.example.kino_search.model.Film;
import com.example.kino_search.util.TMDBApiUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FilmService {

    private static final Logger logger = Logger.getLogger(FilmService.class.getName());

    public static void fetchAndSaveFilm(int apiId) {
        logger.info("Starting process to fetch and save film with API ID: " + apiId);

        try {
            // Проверяем, существует ли фильм в базе
            Film existingFilm = FilmDAO.getFilmByApiId(apiId);
            if (existingFilm != null) {
                logger.info("Film already exists in database: " + existingFilm.getTitle());
                return;
            }

            // Запрос данных фильма из TMDB API
            String endpoint = "/movie/" + apiId;
            logger.info("Sending request to TMDB API for endpoint: " + endpoint);
            JsonObject movieDetails = TMDBApiUtil.sendRequest(endpoint);

            // Создание объекта Film
            Film film = new Film();
            film.setApiId(apiId);
            film.setTitle(movieDetails.get("title").getAsString());
            film.setReleaseDate(Date.valueOf(movieDetails.get("release_date").getAsString()));
            film.setPosterUrl("https://image.tmdb.org/t/p/w500" + movieDetails.get("poster_path").getAsString());
            film.setRuntime(movieDetails.get("runtime").getAsInt());
            film.setApiRating(movieDetails.get("vote_average").getAsFloat());
            film.setApiCount(movieDetails.get("vote_count").getAsInt());
            film.setOverview(movieDetails.get("overview").getAsString());

            // Сохраняем фильм
            logger.info("Saving film to database: " + film.getTitle());
            FilmDAO.saveOrUpdateFilm(film);

            if (film.getId() == 0) {
                logger.severe("Film ID is not set. Film might not have been saved properly.");
                throw new IllegalStateException("Film ID is not set. Film might not have been saved properly.");
            }

            // Обработка жанров
            JsonArray genres = movieDetails.getAsJsonArray("genres");
            logger.info("Processing genres for film: " + film.getTitle());
            for (int i = 0; i < genres.size(); i++) {
                JsonObject genreObj = genres.get(i).getAsJsonObject();
                String genreName = genreObj.get("name").getAsString();
                int genreId = GenreDAO.saveOrGetGenreId(genreName);

                logger.info("Saving genre-film relation: Genre ID = " + genreId + ", Film ID = " + film.getId());
                GenreFilmDAO.saveGenreFilm(genreId, film.getId());
            }

            logger.info("Film and genres saved successfully: " + film.getTitle());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurred while processing film with API ID: " + apiId, e);
        }
    }

    public static String getFilmTitleByID(int id) {

        String sql = "SELECT title FROM film WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String title = rs.getString("title");
                    logger.info("Film title retrieved successfully for Film ID: " + id + ", Title: " + title);
                    return title;
                } else {
                    logger.warning("No film found with ID: " + id);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving film title for Film ID: " + id, e);
        }
        return null; // Return null if no film is found or an error occurred
    }


    public static Integer getFilmIdByApiId(int apiId) {
        String sql = "SELECT id FROM film WHERE api_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, apiId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int filmId = rs.getInt("id");
                    logger.info("Film ID retrieved successfully for API ID: " + apiId + ", Film ID: " + filmId);
                    return filmId;
                } else {
                    logger.warning("No film found with API ID: " + apiId);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving film ID for API ID: " + apiId, e);
        }
        return null; // Return null if no film is found or an error occurred
    }


    public static Map<String, Object> getFilmDetailsById(int filmId) {
        String sql = "SELECT title, overview, release_date, poster_url, api_rating, rating FROM film WHERE id = ?";
        Map<String, Object> movieDetails = new HashMap<>();

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, filmId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    movieDetails.put("title", rs.getString("title"));
                    movieDetails.put("overview", rs.getString("overview"));
                    movieDetails.put("release_date", rs.getDate("release_date").toString());
                    movieDetails.put("poster_url", rs.getString("poster_url"));
                    movieDetails.put("api_rating", rs.getObject("api_rating")); // Может быть null
                    movieDetails.put("rating", rs.getObject("rating")); // Может быть null
                    logger.info("Fetched film details: " +  rs.getObject("rating"));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving movie details for film ID: " + filmId, e);
        }
        return movieDetails;
    }

    public static boolean updateFilmRatingAndCount(int filmId, int newRating, boolean isUpdate) {
        String updateQuery;
        if (isUpdate) {
            updateQuery = """
            UPDATE film
            SET rating = (rating * count + ?) / count
            WHERE id = ?
        """;
        } else {
            updateQuery = """
            UPDATE film
            SET rating = (rating * count + ?) / (count + 1),
                count = count + 1
            WHERE id = ?
        """;
        }

        logger.info("Updating film: filmId=" + filmId + ", newRating=" + newRating + ", isUpdate=" + isUpdate);

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setInt(1, newRating);
            stmt.setInt(2, filmId);

            logger.info("Executing query: " + updateQuery);
            logger.info("Query parameters: newRating=" + newRating + ", filmId=" + filmId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                logger.info("Successfully updated film rating and count. Rows updated: " + rowsUpdated);
                return true;
            } else {
                logger.warning("No rows were updated for film ID: " + filmId);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating film rating and count for film ID: " + filmId, e);
        }
        return false;
    }

    public static void testUpdateFilmRatingAndCount() {
        int testFilmId = 1; // Замените на существующий ID фильма
        int newRating = 8;
        boolean isUpdate = false; // Используйте true для обновления рейтинга

        boolean result = updateFilmRatingAndCount(testFilmId, newRating, isUpdate);

        if (result) {
            logger.info("Test passed: Film rating and count updated successfully.");
        } else {
            logger.warning("Test failed: Film rating and count were not updated.");
        }
    }

}
