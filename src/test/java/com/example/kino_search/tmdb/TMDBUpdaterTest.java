package com.example.kino_search.tmdb;

import com.example.kino_search.db.dao.FilmDAO;
import com.example.kino_search.db.tmdb.TMDBUpdater;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TMDBUpdaterTest {

    @Test
    public void testUpdateFilms() {
        // Предполагается, что TMDBUpdater.updateFilms() обращается к TMDBClient.fetchPopularMovies() и fetchTopRatedMovies()
        // и вызывает FilmDAO.saveOrUpdateFilm()

        // 1. Вызов обновления
        TMDBUpdater.updateFilms();

        // 2. Проверяем, что в базе должны появиться какие-то фильмы.
        // Здесь мы можем просто вызвать FilmDAO.getRandomFilm() и убедиться что не пусто.
        Map<String,Object> randomFilm = FilmDAO.getRandomFilm();

        Assertions.assertFalse(randomFilm.isEmpty(), "After update, we should have at least one film in the database.");
    }
}
