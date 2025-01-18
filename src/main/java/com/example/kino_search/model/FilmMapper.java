package com.example.kino_search.model;

public class FilmMapper {
    public static FilmDTO toDTO(Film film) {
        FilmDTO dto = new FilmDTO();
        dto.setId(film.getId());
        dto.setTitle(film.getTitle());
        dto.setReleaseDate(film.getReleaseDate().toString());
        dto.setPosterUrl(film.getPosterUrl());
        return dto;
    }
}
