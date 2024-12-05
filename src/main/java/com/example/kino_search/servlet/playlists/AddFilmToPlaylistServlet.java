package com.example.kino_search.servlet.playlists;

import com.example.kino_search.db.ConnectionManager;
import com.example.kino_search.db.FilmService;
import com.example.kino_search.db.dao.PlaylistFilmDAO;

import com.example.kino_search.servlet.dashboard.MovieDetailsServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
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
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        try {
            // Логика добавления фильма в плейлист
            int parsedPlaylistId = Integer.parseInt(playlistId);
            int parsedApiId = Integer.parseInt(apiId);

            // убедимся что есть в базе
            FilmService.fetchAndSaveFilm(parsedApiId);

            // Получаем ID фильма из базы
            int filmId = getFilmIdByApiId(parsedApiId);

            // Добавляем связь между плейлистом и фильмом
            boolean isAded = PlaylistFilmDAO.addFilmToPlaylist(parsedPlaylistId, filmId);


            if (isAded) {
                // Получите названия плейлиста и фильма для отображения
                String playlistName = "whant to whatch"/*PlaylistDAO.getPlaylistName(parsedPlaylistId)*/;
                String filmTitle = FilmService.getFilmTitle(filmId);

                request.setAttribute("playlistName", playlistName);
                request.setAttribute("filmTitle", filmTitle);
                request.getRequestDispatcher("/success.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "This film already exists in the playlist.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } catch (Exception e) {
            logger.severe("Error processing request: " + e.getMessage());
            request.setAttribute("errorMessage", "An error occurred while processing your request.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private int getFilmIdByApiId(int apiId) {
        String sql = "SELECT id FROM film WHERE api_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, apiId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving film ID by API ID: " + apiId, e);
        }
        return -1; // Если фильм не найден
    }
}
