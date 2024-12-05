package com.example.kino_search.db.dao;

import com.example.kino_search.db.ConnectionManager;
import com.example.kino_search.db.FilmService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlaylistFilmDAO {

    private static final Logger logger = Logger.getLogger(PlaylistFilmDAO.class.getName());

    // Добавление фильма в плейлист
    public static boolean addFilmToPlaylist(int playlistId, int apiId) {
        // Сначала сохраняем или получаем фильм из базы
        FilmService.fetchAndSaveFilm(apiId);

        String sql = """
            INSERT INTO playlist_film (playlist_id, film_id)
            VALUES (?, (SELECT id FROM film WHERE api_id = ?))
            ON CONFLICT DO NOTHING
        """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playlistId);
            stmt.setInt(2, apiId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Film added to playlist: Playlist ID = " + playlistId + ", API ID = " + apiId);
                return true;
            } else {
                logger.info("Film already exists in playlist: Playlist ID = " + playlistId + ", API ID = " + apiId);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding film to playlist", e);
        }
        return false;
    }

    // Удаление фильма из плейлиста
    public static boolean removeFilmFromPlaylist(int playlistId, int filmId) {
        String sql = "DELETE FROM playlist_film WHERE playlist_id = ? AND film_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playlistId);
            stmt.setInt(2, filmId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Film removed from playlist: Playlist ID = " + playlistId + ", Film ID = " + filmId);
                return true;
            } else {
                logger.info("No film found to remove from playlist: Playlist ID = " + playlistId + ", Film ID = " + filmId);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error removing film from playlist", e);
        }
        return false;
    }
}
