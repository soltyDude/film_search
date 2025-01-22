package com.example.kino_search.servlet.playlists;

import com.example.kino_search.db.dao.PlaylistDAO;
import com.example.kino_search.model.Film;
import com.example.kino_search.model.Playlist;
import com.example.kino_search.model.PlaylistFilm;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ViewPlaylistServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ViewPlaylistServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String playlistIdParam = request.getParameter("id");

        if (playlistIdParam == null || playlistIdParam.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Playlist ID is required.");
            return;
        }

        try {
            int playlistId = Integer.parseInt(playlistIdParam);

            // Получаем детали плейлиста и фильмы
            Playlist playlistDetails = PlaylistDAO.getInstance().getPlaylistDetails(playlistId);

            if (playlistDetails == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Playlist not found.");
                return;
            }

            // Преобразуем список PlaylistFilm в список Film
            List<Film> films = playlistDetails.getPlaylistFilms()
                    .stream()
                    .map(PlaylistFilm::getFilm)
                    .collect(Collectors.toList());

            // Передаём данные плейлиста и фильмы в JSP
            request.setAttribute("playlistId", playlistId);
            request.setAttribute("playlistName", playlistDetails.getName());
            request.setAttribute("films", films);

            request.getRequestDispatcher("viewPlaylist.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            logger.severe("Invalid playlist ID format: " + playlistIdParam);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Playlist ID.");
        } catch (Exception e) {
            logger.severe("Error retrieving playlist details: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while loading the playlist.");
        }
    }
}
