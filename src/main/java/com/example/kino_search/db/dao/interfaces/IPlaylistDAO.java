package com.example.kino_search.db.dao.interfaces;

import java.util.List;
import java.util.Map;

public interface IPlaylistDAO {
    List<Map<String, Object>> getPlaylistsByUserId(int userId);
    int getWantToWatchPlaylistId(int userId);
    Map<String, Object> getPlaylistDetails(int playlistId);
    boolean createPlaylist(int userId, String playlistName);
    String getPlaylistNameById(int playlistId);
}
