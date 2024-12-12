package com.example.kino_search.db.tmdb;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TMDBClient {

    private static final String API_KEY = "ВАШ_КЛЮЧ_ТМДБ";
    private static final String BASE_URL = "https://api.themoviedb.org/3";

    public static JsonObject fetchPopularMovies() throws Exception {
        String urlStr = BASE_URL + "/movie/popular?api_key=" + API_KEY + "&language=en-US&page=1";
        return fetchJson(urlStr);
    }

    public static JsonObject fetchTopRatedMovies() throws Exception {
        String urlStr = BASE_URL + "/movie/top_rated?api_key=" + API_KEY + "&language=en-US&page=1";
        return fetchJson(urlStr);
    }

    private static JsonObject fetchJson(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        try (InputStreamReader reader = new InputStreamReader(conn.getInputStream())) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }
}
