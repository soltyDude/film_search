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
        // Пример получения данных:
        film.setApiId(filmJson.get("id").getAsInt());
        film.setTitle(filmJson.get("title").getAsString());
        film.setPosterUrl("https://image.tmdb.org/t/p/w500" + filmJson.get("poster_path").getAsString());

        // Release date может быть строкой в формате "yyyy-MM-dd"
        // Нужно проверить на null
        if (!filmJson.get("release_date").isJsonNull()) {
            film.setReleaseDate(Date.valueOf(filmJson.get("release_date").getAsString()));
        } else {
            // Установим текущую дату или null
            film.setReleaseDate(new Date(System.currentTimeMillis()));
        }

        // Так как runtime не приходит в популярном или топ запросе напрямую, его можно получить отдельным запросом к /movie/{id} или установить дефолт
        film.setRuntime(120); // нужно отдельное обращение к TMDB API, если хотите точный runtime

        float apiRating = filmJson.get("vote_average").getAsFloat();
        int apiCount = filmJson.get("vote_count").getAsInt();

        film.setApiRating(apiRating);
        film.setApiCount(apiCount);
        // Внутренний rating и count может быть 0 при первой загрузке
        film.setRating(apiRating);
        film.setCount(apiCount);

        if (!filmJson.get("overview").isJsonNull()) {
            film.setOverview(filmJson.get("overview").getAsString());
        } else {
            film.setOverview("");
        }

        return film;
    }
}
