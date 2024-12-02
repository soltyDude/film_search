package com.example.kino_search.db;

import com.example.kino_search.model.Film;
import com.example.kino_search.util.TMDBApiUtil;
import com.google.gson.JsonObject;

import java.sql.Date;

public class FilmService {

    public static void fetchAndSaveFilm(int apiId) {
        try {
            // Получаем данные фильма из TMDB API
            String endpoint = "/movie/" + apiId;
            JsonObject movieDetails = TMDBApiUtil.sendRequest(endpoint);

            // Заполняем объект Film
            Film film = new Film();
            film.setId(movieDetails.get("id").getAsInt());
            film.setTitle(movieDetails.get("title").getAsString());
            film.setReleaseDate(Date.valueOf(movieDetails.get("release_date").getAsString()));
            film.setPosterUrl("https://image.tmdb.org/t/p/w500" + movieDetails.get("poster_path").getAsString());
            film.setApiId(apiId);
            film.setRuntime(movieDetails.get("runtime").getAsInt());
            film.setApiRating(movieDetails.get("vote_average").getAsFloat());
            film.setApiCount(movieDetails.get("vote_count").getAsInt());
            film.setOverview(movieDetails.get("overview").getAsString());

            // Сохраняем фильм в базу данных
            FilmDAO.saveOrUpdateFilm(film);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}