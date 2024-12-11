package com.example.kino_search.servlet.playlists;

import com.example.kino_search.db.dao.PlaylistDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class CreatePlaylistServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        String playlistName = request.getParameter("playlistName");

        if (playlistName == null || playlistName.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Playlist name cannot be empty.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        boolean isCreated = PlaylistDAO.createPlaylist(userId, playlistName);

        if (isCreated) {
            response.sendRedirect("playlists"); // Перенаправление на страницу с плейлистами
        } else {
            request.setAttribute("errorMessage", "Failed to create playlist. Try again.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
