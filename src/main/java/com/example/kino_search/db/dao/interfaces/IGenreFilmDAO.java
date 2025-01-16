package com.example.kino_search.db.dao.interfaces;

public interface IGenreFilmDAO {
    boolean saveGenreFilm(int genreId, int filmId);
    // Optionally, you can add a method to remove genre-film relationships
    // boolean removeGenreFilm(int genreId, int filmId);
}
