package com.example.kino_search.servlet.login_register;

import org.mindrot.jbcrypt.BCrypt;
import com.example.kino_search.db.ConnectionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nickname = request.getParameter("nickname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!password.equals(confirmPassword)) {
            response.getWriter().write("Passwords do not match!");
            return;
        }

        // Хешируем пароль
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = ConnectionManager.getConnection()) {
            // Проверяем, существует ли email в базе
            String checkEmailSql = "SELECT COUNT(*) FROM users WHERE email = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkEmailSql)) {
                checkStmt.setString(1, email);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        response.getWriter().write("Email is already in use!");
                        return;
                    }
                }
            }

            // Если email не найден, добавляем пользователя
            String insertSql = "INSERT INTO users (nickname, email, password, created_at) VALUES (?, ?, ?, NOW())";
            try (PreparedStatement stmt = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, nickname);
                stmt.setString(2, email);
                stmt.setString(3, hashedPassword);
                stmt.executeUpdate();

                // Получаем сгенерированный ID нового пользователя
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);

                        // Создаем плейлист "Want to Watch" для нового пользователя
                        String createPlaylistSql = "INSERT INTO playlist (name, user_id, created_at, updated_at) VALUES (?, ?, NOW(), NOW())";
                        try (PreparedStatement playlistStmt = conn.prepareStatement(createPlaylistSql)) {
                            playlistStmt.setString(1, "Want to Watch");
                            playlistStmt.setInt(2, userId);
                            playlistStmt.executeUpdate();
                        }
                    }
                }
            }

            response.getWriter().write("User registered successfully!");
            response.sendRedirect("dashboard.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}
