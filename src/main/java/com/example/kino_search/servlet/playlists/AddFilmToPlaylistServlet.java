package com.example.kino_search.servlet.playlists;

import com.example.kino_search.db.FilmService;
import com.example.kino_search.db.dao.PlaylistFilmDAO;

import com.example.kino_search.servlet.dashboard.MovieDetailsServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

public class AddFilmToPlaylistServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(MovieDetailsServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String playlistId = request.getParameter("playlistId");
        String apiId = request.getParameter("apiId");


        logger.info("Received playlistId: " + playlistId + ", apiId: " + apiId);


        if (playlistId == null || apiId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Playlist ID and API ID are required.");
            return;
        }

        try {
            // Логика добавления фильма в плейлист
            int parsedPlaylistId = Integer.parseInt(playlistId);
            int parsedApiId = Integer.parseInt(apiId);

            // убедимся что есть в базе
            FilmService.fetchAndSaveFilm(parsedApiId);

            // Добавляем связь между плейлистом и фильмом
            PlaylistFilmDAO.addFilmToPlaylist(parsedPlaylistId, parsedApiId);

            response.sendRedirect("/success.jsp"); // Перенаправляем на страницу успеха
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request.");
        }
    }
}
