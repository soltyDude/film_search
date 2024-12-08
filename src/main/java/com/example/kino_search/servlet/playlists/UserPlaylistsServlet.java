package com.example.kino_search.servlet.playlists;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.example.kino_search.db.dao.PlaylistDAO;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class UserPlaylistsServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(UserPlaylistsServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        logger.info("Fetching playlists for user ID: " + userId);

        try {
            // Получаем плейлисты пользователя из базы данных
            List<Map<String, Object>> playlists = PlaylistDAO.getPlaylistsByUserId(userId);
            logger.info("Found " + playlists.size() + " playlists");

            // Передаем данные в JSP
            request.setAttribute("playlists", playlists);
            request.getRequestDispatcher("playlists.jsp").forward(request, response);
        } catch (Exception e) {
            logger.severe("Error retrieving playlists for user ID " + userId + ": " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while loading playlists.");
        }
    }
}
