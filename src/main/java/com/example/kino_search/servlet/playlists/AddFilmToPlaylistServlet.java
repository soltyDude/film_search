package com.example.kino_search.servlet.playlists;

import com.example.kino_search.db.ConnectionManager;
import com.example.kino_search.db.FilmService;
import com.example.kino_search.db.dao.PlaylistDAO;
import com.example.kino_search.db.dao.PlaylistFilmDAO;
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

    private static final Logger logger = Logger.getLogger(AddFilmToPlaylistServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String playlistIdParam = request.getParameter("playlistId");
        String apiIdParam = request.getParameter("apiId");

        logger.info("Received playlistId: " + playlistIdParam + ", apiId: " + apiIdParam);

        if (playlistIdParam == null || apiIdParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Playlist ID and API ID are required.");
            return;
        }

        try {
            int playlistId = Integer.parseInt(playlistIdParam);
            int apiId = Integer.parseInt(apiIdParam);

            // Проверяем и сохраняем фильм в базе данных, если его там ещё нет
            FilmService.fetchAndSaveFilm(apiId);

            // Получаем ID фильма из базы данных
            int filmId = FilmService.getFilmIdByApiId(apiId);
            if (filmId == -1) {
                throw new Exception("Film not found in the database for API ID: " + apiId);
            }

            // Добавляем фильм в плейлист
            boolean isAdded = PlaylistFilmDAO.addFilmToPlaylist(playlistId, filmId);

            if (isAdded) {
                // Получаем названия плейлиста и фильма
                String playlistName = PlaylistDAO.getPlaylistNameById(playlistId);
                String filmTitle = FilmService.getFilmTitleByID(filmId);

                request.setAttribute("playlistName", playlistName);
                request.setAttribute("filmTitle", filmTitle);
                request.getRequestDispatcher("/success.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "This film already exists in the playlist.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Invalid playlist ID or API ID format", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid playlist ID or API ID format.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing request", e);
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
