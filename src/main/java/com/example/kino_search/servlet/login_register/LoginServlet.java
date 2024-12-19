package com.example.kino_search.servlet.login_register;

import jakarta.servlet.http.HttpSession;
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


public class LoginServlet extends HttpServlet {

    //хранб в сесии userId user(ник)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT id, password, nickname FROM users WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                ResultSet sqlOutput = stmt.executeQuery();



                if (sqlOutput.next()) {
                    String storedPassword = sqlOutput.getString("password");
                    String nickname = sqlOutput.getString("nickname");

                    if (BCrypt.checkpw(password, storedPassword)) {

                        HttpSession oldSession = request.getSession(false);
                        if (oldSession != null) {
                            oldSession.invalidate(); // уничтожаем старую сессию
                        }
                        HttpSession session = request.getSession(true); // создаём новую сессию
                        session.setAttribute("user", nickname);
                        session.setAttribute("userId", sqlOutput.getInt("id"));

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