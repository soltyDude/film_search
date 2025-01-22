package com.example.kino_search.model;

import com.example.kino_search.db.dao.FilmDAO;
import com.example.kino_search.db.dao.ReviewDAO;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "viewed_movies")
public class ViewedMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "film_id", nullable = false)
    private int filmId;

    @Column(name = "reviews_id")
    private Integer reviewId;

    @Column(name = "viewed_at", nullable = false)
    private LocalDateTime viewedAt;

    // Ленивая загрузка фильма
    @Transient
    private Film film;

    // Ленивая загрузка отзыва
    @Transient
    private Review review;

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public LocalDateTime getViewedAt() {
        return viewedAt;
    }

    public void setViewedAt(LocalDateTime viewedAt) {
        this.viewedAt = viewedAt;
    }

    // Ленивая загрузка объекта Film
    public Film getFilm() {
        if (film == null) {
            film = FilmDAO.getInstance().getFilmById(filmId);
        }
        return film;
    }

    // Геттер для posterUrl
    public String getPosterUrl() {
        return getFilm() != null ? getFilm().getPosterUrl() : null;
    }

    // Геттер для title
    public String getTitle() {
        return getFilm() != null ? getFilm().getTitle() : null;
    }

    // Геттер для overview
    public String getOverview() {
        return getFilm() != null ? getFilm().getOverview() : null;
    }

    // Ленивая загрузка объекта Review
    public Review getReview() {
        if (review == null && reviewId != null) {
            review = ReviewDAO.getInstance().getReviewByUserAndFilm(userId, reviewId);
        }
        return review;
    }

    // Геттер для rating из Review
    public Integer getRating() {
        return getReview() != null ? getReview().getRating() : null;
    }

    public Integer getApiId() {
        return getFilm() != null ? getFilm().getApiId() : null;
    }

}
