package com.example.kino_search.db.tmdb;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import com.example.kino_search.db.dao.ViewedMoviesDAO;
import com.example.kino_search.property.PropertyManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TMDBClient {

    private static final Logger logger = Logger.getLogger(TMDBClient.class.getName());
    private static final String API_KEY = PropertyManager.getProperty("tmdb.api_key");
    private static final String BASE_URL = PropertyManager.getProperty("tmdb.base_url");

    private static volatile TMDBClient instance;

    // Private constructor to prevent instantiation
    private TMDBClient() {}

    /**
     * Returns the singleton instance of the GenreFilmDAO class.
     * Uses double-checked locking for thread safety.
     *
     * @return The singleton instance of GenreFilmDAO.
     */
    public static TMDBClient getInstance() {
        if (instance == null) {
            synchronized (TMDBClient.class) {
                if (instance == null) {
                    instance = new TMDBClient();
                }
            }
        }
        return instance;
    }

    public JsonObject fetchPopularMovies() throws Exception {
        String urlStr = BASE_URL + "/movie/popular?api_key=" + API_KEY + "&language=en-US&page=1";
        return getInstance().fetchJson(urlStr);
    }

    public JsonObject fetchTopRatedMovies() throws Exception {
        String urlStr = BASE_URL + "/movie/top_rated?api_key=" + API_KEY + "&language=en-US&page=1";
        return getInstance().fetchJson(urlStr);
    }

    private JsonObject fetchJson(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        try (InputStreamReader reader = new InputStreamReader(conn.getInputStream())) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }
}
