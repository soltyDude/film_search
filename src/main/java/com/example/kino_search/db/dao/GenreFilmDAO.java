package com.example.kino_search.db.dao;

import com.example.kino_search.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenreFilmDAO {

    private static final Logger logger = Logger.getLogger(GenreFilmDAO.class.getName());

    // Сохранение связи между фильмом и жанром
    public static void saveGenreFilm(int genreId, int filmId) {
        logger.info("Starting to save genre-film relation: Genre ID = " + genreId + ", Film ID = " + filmId);

        String sql = "INSERT INTO genre_film (genre_id, film_id) VALUES (?, ?) ON CONFLICT DO NOTHING";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Установка параметров запроса
            stmt.setInt(1, genreId);
            stmt.setInt(2, filmId);

            // Выполнение запроса
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Genre-film relation saved successfully: Genre ID = " + genreId + ", Film ID = " + filmId);
            } else {
                logger.info("Genre-film relation already exists: Genre ID = " + genreId + ", Film ID = " + filmId);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error occurred while saving genre-film relation: Genre ID = "
                    + genreId + ", Film ID = " + filmId, e);
        }
    }
}
