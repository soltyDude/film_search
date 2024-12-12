package com.example.kino_search.testeClaces;

import com.example.kino_search.db.ConnectionManager;
import com.example.kino_search.db.FilmService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

public class UpdateRatingTest {

    private static final Logger logger = Logger.getLogger(UpdateRatingTest.class.getName());

    public static void main(String[] args) {
        int testFilmId = 2; // Замените на существующий ID фильма
        int newRating = 8; // Новый рейтинг для тестирования

        // Перед тестом: Вывод текущего состояния
        logger.info("Before update:");
        printFilmDetails(testFilmId);

        // Тестируем обновление рейтинга и количества
        boolean isUpdate = false; // Установите true для проверки обновления
        boolean result = FilmService.updateFilmRatingAndCount(testFilmId, newRating, isUpdate);

        if (result) {
            logger.info("Test passed: Film rating and count updated successfully.");
        } else {
            logger.warning("Test failed: Film rating and count were not updated.");
        }

        // После теста: Вывод текущего состояния
        logger.info("After update:");
        printFilmDetails(testFilmId);
    }

    private static void printFilmDetails(int filmId) {
        String query = "SELECT id, title, rating, count FROM film WHERE id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, filmId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String title = rs.getString("title");
                    float rating = rs.getFloat("rating");
                    int count = rs.getInt("count");

                    logger.info("Film Details: ID=" + id + ", Title='" + title + "', Rating=" + rating + ", Count=" + count);
                } else {
                    logger.warning("No film found with ID: " + filmId);
                }
            }
        } catch (Exception e) {
            logger.severe("Error retrieving film details: " + e.getMessage());
            e.printStackTrace();
        }
    }
}



