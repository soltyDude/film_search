package com.example.kino_search.db.dao;

import com.example.kino_search.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenreDAO {

    private static final Logger logger = Logger.getLogger(GenreDAO.class.getName());

    // Сохранение или получение жанра по имени
    public static int saveOrGetGenreId(String genreName) {
        String sqlInsert = "INSERT INTO genre (name) VALUES (?) ON CONFLICT (name) DO NOTHING";
        String sqlSelect = "SELECT id FROM genre WHERE name = ?";

        try (Connection conn = ConnectionManager.getConnection()) {
            // Вставляем жанр, если он не существует
            try (PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
                stmt.setString(1, genreName);
                stmt.executeUpdate();
            }

            // Получаем ID жанра
            try (PreparedStatement stmt = conn.prepareStatement(sqlSelect)) {
                stmt.setString(1, genreName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    logger.severe("Genre not found after insert/update: " + genreName);
                    throw new SQLException("Failed to retrieve Genre ID.");
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error occurred while saving or retrieving genre: " + genreName, e);
        }

        return -1; // Если ничего не найдено
    }

}
