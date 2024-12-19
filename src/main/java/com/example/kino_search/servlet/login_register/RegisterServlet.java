package com.example.kino_search.servlet.login_register;

import org.mindrot.jbcrypt.BCrypt;
import com.example.kino_search.db.ConnectionManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            response.getWriter().write("Passwords do not match!");
            return;
        }

        // Validate the password strength
        if (!PasswordValidator.validate(password)) {
            response.getWriter().write("Password does not meet security requirements!");
            return;
        }

        // Hash the password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = ConnectionManager.getConnection()) {
            // Check if the email is already in use
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

            // Insert the new user record
            String insertSql = "INSERT INTO users (nickname, email, password, created_at) VALUES (?, ?, ?, NOW())";
            int userId;
            try (PreparedStatement stmt = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, nickname);
                stmt.setString(2, email);
                stmt.setString(3, hashedPassword);
                stmt.executeUpdate();

                // Retrieve the generated user ID
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        userId = generatedKeys.getInt(1);
                    } else {
                        response.getWriter().write("Failed to register user.");
                        return;
                    }
                }
            }

            // Create the "Want to Watch" playlist for the new user
            String createPlaylistSql = "INSERT INTO playlist (name, user_id, created_at, updated_at) VALUES (?, ?, NOW(), NOW())";
            try (PreparedStatement playlistStmt = conn.prepareStatement(createPlaylistSql)) {
                playlistStmt.setString(1, "Want to Watch");
                playlistStmt.setInt(2, userId);
                playlistStmt.executeUpdate();
            }

            // After successful registration, automatically log the user in:
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            HttpSession session = request.getSession(true);
            session.setAttribute("user", nickname);
            session.setAttribute("userId", userId);

            // Redirect the user to the dashboard
            response.sendRedirect("dashboard.jsp");

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}
