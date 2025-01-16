package com.example.kino_search.db.dao;

import com.example.kino_search.db.ConnectionManager;
import com.example.kino_search.db.FilmService;
import com.example.kino_search.db.dao.interfaces.IPlaylistDAO;
import com.example.kino_search.db.dao.interfaces.IPlaylistFilmDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlaylistFilmDAO implements IPlaylistFilmDAO {

    private static final Logger logger = Logger.getLogger(PlaylistFilmDAO.class.getName());
    private static volatile PlaylistFilmDAO instance;

    // Private constructor to prevent instantiation
    private PlaylistFilmDAO() {}

    /**
     * Returns the singleton instance of the GenreFilmDAO class.
     * Uses double-checked locking for thread safety.
     *
     * @return The singleton instance of GenreFilmDAO.
     */
    public static PlaylistFilmDAO getInstance() {
        if (instance == null) {
            synchronized (PlaylistFilmDAO.class) {
                if (instance == null) {
                    instance = new PlaylistFilmDAO();
                }
            }
        }
        return instance;
    }
    // Добавление фильма в плейлист
    public boolean addFilmToPlaylist(int playlistId, int filmID) {
        // Сначала сохраняем или получаем фильм из базы
        FilmService.getInstance().fetchAndSaveFilm(filmID);

        String sql = """
            INSERT INTO playlist_film (playlist_id, film_id)
            VALUES (?, ?)
            ON CONFLICT DO NOTHING
        """;

        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playlistId);
            stmt.setInt(2, filmID);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Film added to playlist: Playlist ID = " + playlistId + ",  ID = " + filmID);
                return true;
            } else {
                logger.info("Film already exists in playlist: Playlist ID = " + playlistId + ",  ID = " + filmID);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding film to playlist", e);
        }
        return false;
    }

    // Удаление фильма из плейлиста
    public boolean removeFilmFromPlaylist(int playlistId, int filmId) {
        String sql = "DELETE FROM playlist_film WHERE playlist_id = ? AND film_id = ?";

        try (Connection conn = ConnectionManager.getInstance().getConnection();
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
