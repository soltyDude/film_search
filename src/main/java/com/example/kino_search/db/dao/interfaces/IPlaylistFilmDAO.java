package com.example.kino_search.db.dao.interfaces;

public interface IPlaylistFilmDAO {
    boolean addFilmToPlaylist(int playlistId, int filmID);
    boolean removeFilmFromPlaylist(int playlistId, int filmId);
}
