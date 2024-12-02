package com.example.kino_search.model;

import java.sql.Date;

public class Film {
    private int id;
    private String title;
    private Date releaseDate;
    private String posterUrl;
    private int apiId;
    private int runtime;
    private float apiRating;
    private float rating;
    private int apiCount;
    private int count;
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
    public Date getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(Date releaseDate) {
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
    public float getRating() {
        return rating;
    }
    public void setRating(float rating) {
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
