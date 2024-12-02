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
        logger.info("Starting process to save or retrieve Genre ID for genre: " + genreName);

        String selectSql = "SELECT id FROM genre WHERE name = ?";
        String insertSql = "INSERT INTO genre (name) VALUES (?) RETURNING id";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {

            // Проверяем, существует ли жанр
            logger.info("Checking if genre exists in database: " + genreName);
            selectStmt.setString(1, genreName);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                int genreId = rs.getInt("id");
                logger.info("Genre found in database. Genre ID: " + genreId);
                return genreId;
            }

            // Если жанр не найден, вставляем новый
            logger.info("Genre not found in database. Inserting new genre: " + genreName);
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, genreName);
                ResultSet insertRs = insertStmt.executeQuery();
                if (insertRs.next()) {
                    int genreId = insertRs.getInt("id");
                    logger.info("New genre inserted successfully. Genre ID: " + genreId);
                    return genreId;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error occurred while saving or retrieving genre: " + genreName, e);
        }

        logger.severe("Failed to save or retrieve Genre ID for genre: " + genreName);
        return -1;
    }
}
