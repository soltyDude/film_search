package com.example.kino_search.servlet.playlists;

import com.example.kino_search.db.dao.PlaylistFilmDAO;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class RemoveFilmFromPlaylistServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int playlistId = Integer.parseInt(request.getParameter("playlistId"));
        int filmId = Integer.parseInt(request.getParameter("filmId"));

        // Удаляем фильм из плейлиста
        boolean success = PlaylistFilmDAO.removeFilmFromPlaylist(playlistId, filmId);

        if (success) {
            response.getWriter().write("Film removed from playlist successfully.");
        } else {
            response.getWriter().write("Film not found in playlist.");
        }
    }
}
