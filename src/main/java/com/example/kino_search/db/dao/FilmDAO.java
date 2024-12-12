package com.example.kino_search.db.dao;

import com.example.kino_search.db.ConnectionManager;
import com.example.kino_search.model.Film;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FilmDAO {

    private static final Logger logger = Logger.getLogger(FilmDAO.class.getName());

    // Метод для сохранения фильма в таблицу потом убуду использовать для ежедненого обновления
    public static void saveOrUpdateFilm(Film film) {
        String sqlInsert = """
        INSERT INTO film (api_id, title, release_date, poster_url, runtime, api_rating, rating, api_count, count, overview)
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
            overview = EXCLUDED.overview
    """;

        String sqlSelect = "SELECT id FROM film WHERE api_id = ?";

        try (Connection conn = ConnectionManager.getConnection()) {
            // Вставка или обновление
            try (PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
                stmt.setInt(1, film.getApiId());
                stmt.setString(2, film.getTitle());
                stmt.setDate(3, film.getReleaseDate());
                stmt.setString(4, film.getPosterUrl());
                stmt.setInt(5, film.getRuntime());
                stmt.setFloat(6, film.getApiRating());
                stmt.setFloat(7, film.getRating());
                stmt.setInt(8, film.getApiCount());
                stmt.setInt(9, film.getCount());
                stmt.setString(10, film.getOverview());
                stmt.executeUpdate();
            }

            // Получение ID после обновления
            try (PreparedStatement stmt = conn.prepareStatement(sqlSelect)) {
                stmt.setInt(1, film.getApiId());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    film.setId(rs.getInt("id"));
                    logger.info("Film ID retrieved: " + film.getId());
                } else {
                    logger.severe("Failed to retrieve Film ID after insert/update for API ID: " + film.getApiId());
                    throw new SQLException("Failed to retrieve Film ID.");
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error occurred while saving or updating film: " + film.getTitle(), e);
        }
    }


    public static Film getFilmByApiId(int apiId) {
        String sql = "SELECT * FROM film WHERE api_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, apiId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Film film = new Film();
                film.setId(rs.getInt("id"));
                film.setApiId(rs.getInt("api_id"));
                film.setTitle(rs.getString("title"));
                film.setReleaseDate(rs.getDate("release_date"));
                film.setPosterUrl(rs.getString("poster_url"));
                film.setRuntime(rs.getInt("runtime"));
                film.setApiRating(rs.getFloat("api_rating"));
                film.setRating(rs.getFloat("rating"));
                film.setApiCount(rs.getInt("api_count"));
                film.setCount(rs.getInt("count"));
                film.setOverview(rs.getString("overview"));
                return film;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no film is found or in case of an exception
    }


    public static Film getFilmById(int id) {
        String sql = "SELECT * FROM film WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Film film = new Film();
                film.setId(rs.getInt("id"));
                film.setApiId(rs.getInt("api_id"));
                film.setTitle(rs.getString("title"));
                film.setReleaseDate(rs.getDate("release_date"));
                film.setPosterUrl(rs.getString("poster_url"));
                film.setRuntime(rs.getInt("runtime"));
                film.setApiRating(rs.getFloat("api_rating"));
                film.setRating(rs.getFloat("rating"));
                film.setApiCount(rs.getInt("api_count"));
                film.setCount(rs.getInt("count"));
                film.setOverview(rs.getString("overview"));
                return film;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no film is found or in case of an exception
    }

    public static Map<String, Object> getRandomFilm() {
        Map<String, Object> filmData = new HashMap<>();
        String sql = "SELECT id, title, poster_url, api_id FROM film ORDER BY RANDOM() LIMIT 1";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                filmData.put("id", rs.getInt("id"));
                filmData.put("title", rs.getString("title"));
                filmData.put("poster_url", rs.getString("poster_url"));
                filmData.put("apiId", rs.getInt("api_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filmData;
    }

}