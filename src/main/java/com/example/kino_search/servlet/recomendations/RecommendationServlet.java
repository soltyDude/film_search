package com.example.kino_search.servlet.recomendations;

import com.example.kino_search.db.dao.UserDAO;
import com.example.kino_search.model.Film;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RecommendationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve userId from the session
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            if (userId == null) {
                // If not logged in, redirect to login page
                response.sendRedirect("login.jsp");
                return;
            }

            // Fetch recommendation list through UserDAO
            List<Map<String, String>> recommendedFilms = UserDAO.getInstance().getRecommendedMovies(userId);

            request.setAttribute("recommendations", recommendedFilms);
            request.getRequestDispatcher("recommendations.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to fetch recommendations.");
        }
    }
}
