package com.example.kino_search.db.tmdb;

import com.example.kino_search.db.dao.FilmDAO;
import com.example.kino_search.model.Film;

import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

public class TMDBUpdater {

    private static final Logger logger = Logger.getLogger(TMDBUpdater.class.getName());

    public static void updateFilms() {
        try {
            // Получаем популярные фильмы
            JsonObject popularResponse = TMDBClient.fetchPopularMovies();
            updateFromJsonResponse(popularResponse);

            // Получаем топовые фильмы
            JsonObject topRatedResponse = TMDBClient.fetchTopRatedMovies();
            updateFromJsonResponse(topRatedResponse);

            logger.info("TMDB update completed successfully.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating films from TMDB", e);
        }
    }

    private static void updateFromJsonResponse(JsonObject response) {
        JsonArray results = response.getAsJsonArray("results");
        if (results == null) {
            logger.warning("No results found in TMDB response.");
            return;
        }

        for (JsonElement elem : results) {
            JsonObject filmJson = elem.getAsJsonObject();
            Film film = parseFilmFromJson(filmJson);
            FilmDAO.saveOrUpdateFilm(film);
        }
    }

    private static Film parseFilmFromJson(JsonObject filmJson) {
        Film film = new Film();
        film.setApiId(filmJson.get("id").getAsInt());
        film.setTitle(filmJson.get("title").getAsString());
        film.setPosterUrl("https://image.tmdb.org/t/p/w500" + filmJson.get("poster_path").getAsString());

        if (!filmJson.get("release_date").isJsonNull()) {
            film.setReleaseDate(Date.valueOf(filmJson.get("release_date").getAsString()));
        } else {
            film.setReleaseDate(new Date(System.currentTimeMillis()));
        }

        float apiRating = filmJson.get("vote_average").getAsFloat();
        int apiCount = filmJson.get("vote_count").getAsInt();

        film.setApiRating(apiRating);
        film.setApiCount(apiCount);

        // Устанавливаем значения rating и count только для новых фильмов
        film.setRating(apiRating);
        film.setCount(0); // Устанавливаем начальное значение 0 для новых фильмов

        if (!filmJson.get("overview").isJsonNull()) {
            film.setOverview(filmJson.get("overview").getAsString());
        } else {
            film.setOverview("");
        }

        return film;
    }
}
