package com.example.kino_search.db;

import com.example.kino_search.db.dao.FilmDAO;
import com.example.kino_search.db.dao.GenreDAO;
import com.example.kino_search.db.dao.GenreFilmDAO;
import com.example.kino_search.model.Film;
import com.example.kino_search.util.TMDBApiUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public static String getFilmTitle(int filmId) {
        String sql = "SELECT title FROM film WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, filmId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String title = rs.getString("title");
                    logger.info("Film title retrieved successfully for ID: " + filmId + ", Title: " + title);
                    return title;
                } else {
                    logger.warning("No film found with ID: " + filmId);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving film title for ID: " + filmId, e);
        }
        return null; // Return null if no film is found or an error occurred
    }
}
