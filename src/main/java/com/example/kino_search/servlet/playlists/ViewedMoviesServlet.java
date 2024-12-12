package com.example.kino_search.servlet.playlists;

import com.example.kino_search.db.dao.ViewedMoviesDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ViewedMoviesServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ViewedMoviesServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        logger.info("Fetching viewed movies for user ID: " + userId);

        try {
            // Получаем список просмотренных фильмов из базы данных
            List<Map<String, Object>> viewedMovies = ViewedMoviesDAO.getViewedMoviesByUserId(userId);

            // Передаем данные в JSP
            request.setAttribute("viewedMovies", viewedMovies);
            request.getRequestDispatcher("whatched.jsp").forward(request, response);
        } catch (Exception e) {
            logger.severe("Error retrieving viewed movies for user ID " + userId + ": " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while loading viewed movies.");
        }
    }
}
