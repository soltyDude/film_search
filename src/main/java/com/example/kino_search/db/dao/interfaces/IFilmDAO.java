package com.example.kino_search.db.dao.interfaces;

import com.example.kino_search.model.Film;

import java.util.Map;

public interface IFilmDAO {
    void saveOrUpdateFilm(Film film);
    Film getFilmByApiId(int apiId);
    Film getFilmById(int id);
    Map<String, Object> getRandomFilm();
}
