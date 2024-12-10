package com.example.kino_search.db.dao;

import com.example.kino_search.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    /**
     * Получает никнейм пользователя по его ID.
     *
     * @param userId ID пользователя.
     * @return Никнейм пользователя или null, если пользователь не найден.
     */
    public static String getUserNicknameById(int userId) {
        String query = "SELECT nickname FROM users WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("nickname");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Логирование ошибки
            System.err.println("Error fetching user nickname for userId " + userId + ": " + e.getMessage());
        }
        return null; // Если пользователь не найден или произошла ошибка
    }
}
