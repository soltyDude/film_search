package com.example.kino_search.servlet.login_register;

import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import com.example.kino_search.db.ConectionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try (Connection conn = ConectionManager.getConnection()) {
            String sql = "SELECT password, nickname FROM users WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String storedPassword = ((ResultSet) rs).getString("password");
                    String nickname = rs.getString("nickname");

                    if (BCrypt.checkpw(password, storedPassword)) {
                        HttpSession session = request.getSession();
                        session.setAttribute("user", nickname);
                        response.getWriter().write("Login successful!");

                        response.sendRedirect("dashboard.jsp");
                    } else {
                        response.getWriter().write("Invalid credentials!");
                    }
                } else {
                    response.getWriter().write("User not found!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}