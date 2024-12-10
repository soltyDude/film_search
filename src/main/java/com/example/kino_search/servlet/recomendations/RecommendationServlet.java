package com.example.kino_search.servlet.recomendations;

import com.example.kino_search.db.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RecommendationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Получаем userId из сессии
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            if (userId == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // Получаем список рекомендаций через UserDAO
            List<Map<String, String>> recommendations = UserDAO.getRecommendedMovies(userId);

            // Передаём данные в JSP
            request.setAttribute("recommendations", recommendations);
            request.getRequestDispatcher("recommendations.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to fetch recommendations.");
        }
    }
}
