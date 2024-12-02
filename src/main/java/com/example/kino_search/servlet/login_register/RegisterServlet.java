package com.example.kino_search.servlet.login_register;

import org.mindrot.jbcrypt.BCrypt;
import com.example.kino_search.db.ConectionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

        try (Connection conn = ConectionManager.getConnection()) {
            // Проверяем, существует ли email в базе
            String checkEmailSql = "SELECT COUNT(*) FROM users WHERE email = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkEmailSql)) {
                checkStmt.setString(1, email);
                try (var rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        response.getWriter().write("Email is already in use!");
                        return;
                    }
                }
            }

            // Если email не найден, добавляем пользователя
            String insertSql = "INSERT INTO users (nickname, email, password, created_at) VALUES (?, ?, ?, NOW())";
            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                stmt.setString(1, nickname);
                stmt.setString(2, email);
                stmt.setString(3, hashedPassword);
                stmt.executeUpdate();
                response.getWriter().write("User registered successfully!");
                response.sendRedirect("dashboard.jsp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}
