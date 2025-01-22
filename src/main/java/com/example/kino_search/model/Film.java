package com.example.kino_search.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "film")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(name = "poster_url", nullable = false)
    private String posterUrl;

    @Column(name = "api_id", nullable = false, unique = true)
    private int apiId;

    @Column(name = "runtime", nullable = false)
    private int runtime;

    @Column(name = "api_rating", nullable = false)
    private float apiRating;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "api_count", nullable = false)
    private int apiCount;

    @Column(name = "count", nullable = false)
    private int count;

    @Column(name = "overview", columnDefinition = "TEXT")
    private String overview;
// Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public int getApiId() {
        return apiId;
    }

    public void setApiId(int apiId) {
        this.apiId = apiId;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public float getApiRating() {
        return apiRating;
    }

    public void setApiRating(float apiRating) {
        this.apiRating = apiRating;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public int getApiCount() {
        return apiCount;
    }

    public void setApiCount(int apiCount) {
        this.apiCount = apiCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
