package com.example.kino_search.servlet.playlists;

import com.example.kino_search.db.FilmService;
import com.example.kino_search.db.dao.PlaylistFilmDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RemoveFilmFromPlaylistServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(RemoveFilmFromPlaylistServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("begin removing");
        try {
            int playlistId = Integer.parseInt(request.getParameter("playlistId"));
            int apiId = Integer.parseInt(request.getParameter("apiId"));
            int filmId = FilmService.getFilmIdByApiId(apiId);
// Затем удалить filmId из плейлиста

            // Удаляем фильм из плейлиста
            boolean success = PlaylistFilmDAO.removeFilmFromPlaylist(playlistId, filmId);

            if (success) {
                logger.info("Film removed successfully from playlist. Playlist ID: " + playlistId + ", Film ID: " + filmId);
                response.sendRedirect("viewPlaylist?id=" + playlistId);
            } else {
                logger.warning("Failed to remove film. Film not found in playlist. Playlist ID: " + playlistId + ", Film ID: " + filmId);
                request.setAttribute("errorMessage", "Film not found in the playlist.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Invalid playlistId or filmId.", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid playlistId or filmId.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while removing film from playlist.", e);
            request.setAttribute("errorMessage", "An error occurred while processing your request.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
