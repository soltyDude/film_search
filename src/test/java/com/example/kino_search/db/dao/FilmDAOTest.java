package com.example.kino_search.db.dao;

import com.example.kino_search.model.Film;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Date;

public class FilmDAOTest {

    @Test
    public void testSaveOrUpdateFilm() {
        // Создаем тестовый фильм
        Film film = new Film();
        film.setApiId(999999); // несуществующий API_ID для теста
        film.setTitle("Test Movie");
        film.setReleaseDate(Date.valueOf("2022-01-01"));
        film.setPosterUrl("http://example.com/poster.jpg");
        film.setRuntime(120);
        film.setApiRating(7.5f);
        film.setRating(7.5f);
        film.setApiCount(1000);
        film.setCount(1000);
        film.setOverview("Test Overview");

        // Сохраняем фильм в базу
        FilmDAO.saveOrUpdateFilm(film);

        // Проверяем, что ID присвоен
        Assertions.assertNotNull(film.getId(), "Film ID should be assigned after saving.");

        // Теперь пробуем получить этот фильм по api_id
        Film fetched = FilmDAO.getFilmByApiId(999999);
        Assertions.assertNotNull(fetched, "Fetched film should not be null.");
        Assertions.assertEquals("Test Movie", fetched.getTitle(), "Title should match the saved film.");

        // Можно обновить фильм
        film.setTitle("Updated Test Movie");
        FilmDAO.saveOrUpdateFilm(film);

        Film updated = FilmDAO.getFilmByApiId(999999);
        Assertions.assertEquals("Updated Test Movie", updated.getTitle(), "Title should be updated.");

        // После теста можно почистить базу, если хотите
        // Обычно тестовые данные либо чистятся вручную, либо вы запускаете тесты в отдельной тестовой базе
    }

    @Test
    public void testGetRandomFilm() {
        // Проверим, что метод getRandomFilm() возвращает что-то, при условии, что в БД есть фильмы
        var filmData = FilmDAO.getRandomFilm();
        Assertions.assertFalse(filmData.isEmpty(), "Should return some random film data.");
        Assertions.assertTrue(filmData.containsKey("title"), "Random film should have a title.");
    }
}
