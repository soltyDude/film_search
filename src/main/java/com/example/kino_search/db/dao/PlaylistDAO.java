package com.example.kino_search.db.dao;

import com.example.kino_search.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class PlaylistDAO {

    private static final Logger logger = Logger.getLogger(PlaylistDAO.class.getName());

    public static List<Map<String, Object>> getPlaylistsByUserId(int userId) {
        String sql = "SELECT id, name, created_at, updated_at FROM playlist WHERE user_id = ?";
        List<Map<String, Object>> playlists = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> playlist = new HashMap<>();
                    playlist.put("id", rs.getInt("id"));
                    playlist.put("name", rs.getString("name"));
                    playlist.put("created_at", rs.getTimestamp("created_at"));
                    playlist.put("updated_at", rs.getTimestamp("updated_at"));
                    playlists.add(playlist);
                }
            }
        } catch (Exception e) {
            logger.severe("Error retrieving playlists for user ID " + userId + ": " + e.getMessage());
        }

        return playlists;
    }

    /**
     * Получить ID плейлиста "Want to Watch" для пользователя.
     *
     * @param userId ID пользователя
     * @return ID плейлиста или -1, если плейлист не найден
     */
    public static int getWantToWatchPlaylistId(int userId) {
        String sql = "SELECT id FROM playlist WHERE user_id = ? AND name = 'Want to Watch'";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int playlistId = rs.getInt("id");
                    logger.info("Found 'Want to Watch' playlist ID: " + playlistId + " for user ID: " + userId);
                    return playlistId;
                }
            }
        } catch (SQLException e) {
            logger.severe("Error retrieving 'Want to Watch' playlist ID for user ID: " + userId + ": " + e.getMessage());
        }

        logger.warning("No 'Want to Watch' playlist found for user ID: " + userId);
        return -1; // Плейлист не найден
    }

    public static Map<String, Object> getPlaylistDetails(int playlistId) {
        String sql = """
        SELECT p.id AS playlist_id, p.name AS playlist_name, p.created_at, p.updated_at,
               f.api_id AS film_api_id, f.title AS film_title, f.poster_url
        FROM playlist p
        LEFT JOIN playlist_film pf ON p.id = pf.playlist_id
        LEFT JOIN film f ON pf.film_id = f.id
        WHERE p.id = ?
    """;
        Map<String, Object> playlistDetails = new HashMap<>();
        List<Map<String, Object>> films = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playlistId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    if (playlistDetails.isEmpty()) {
                        playlistDetails.put("id", rs.getInt("playlist_id"));
                        playlistDetails.put("name", rs.getString("playlist_name"));
                        playlistDetails.put("created_at", rs.getTimestamp("created_at"));
                        playlistDetails.put("updated_at", rs.getTimestamp("updated_at"));
                    }

                    if (rs.getInt("film_api_id") != 0) {
                        Map<String, Object> film = new HashMap<>();
                        film.put("apiId", rs.getInt("film_api_id"));
                        film.put("title", rs.getString("film_title"));
                        film.put("poster_url", rs.getString("poster_url"));
                        films.add(film);
                    }
                }
            }
            playlistDetails.put("films", films);

        } catch (SQLException e) {
            logger.severe("Error retrieving playlist details for playlist ID " + playlistId + ": " + e.getMessage());
        }

        return playlistDetails;
    }

    public static boolean createPlaylist(int userId, String playlistName) {
        String query = """
        INSERT INTO playlist (name, user_id, created_at, updated_at)
        VALUES (?, ?, NOW(), NOW())
    """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, playlistName);
            preparedStatement.setInt(2, userId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}

