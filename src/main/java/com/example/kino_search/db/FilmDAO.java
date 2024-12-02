package com.example.kino_search.db;

import com.example.kino_search.model.Film;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FilmDAO {

    // Метод для сохранения фильма в таблицу
    public static void saveOrUpdateFilm(Film film) {
        String sql = """
            INSERT INTO film (title, release_date, poster_url, api_id, runtime, api_rating, rating, api_count, count, overvuw)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT (api_id) DO UPDATE SET
                title = EXCLUDED.title,
                release_date = EXCLUDED.release_date,
                poster_url = EXCLUDED.poster_url,
                runtime = EXCLUDED.runtime,
                api_rating = EXCLUDED.api_rating,
                rating = EXCLUDED.rating,
                api_count = EXCLUDED.api_count,
                count = EXCLUDED.count,
                overvuw = EXCLUDED.overvuw;
            """;

        try (Connection conn = ConectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, film.getTitle());
            stmt.setDate(2, film.getReleaseDate());
            stmt.setString(3, film.getPosterUrl());
            stmt.setInt(4, film.getApiId());
            stmt.setInt(5, film.getRuntime());
            stmt.setFloat(6, film.getApiRating());
            stmt.setFloat(7, film.getRating());
            stmt.setInt(8, film.getApiCount());
            stmt.setInt(9, film.getCount());
            stmt.setString(10, film.getOverview());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}