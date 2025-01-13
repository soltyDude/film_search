package com.example.kino_search.util;

import com.example.kino_search.property.PropertyManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class TMDBApiUtil {
    private static final Logger logger = Logger.getLogger(TMDBApiUtil.class.getName());
    private static final String API_KEY = PropertyManager.getProperty("tmdb.api_key");
    private static final String BASE_URL = PropertyManager.getProperty("tmdb.base_url");
    private static final long CACHE_EXPIRATION_TIME = Long.parseLong(PropertyManager.getProperty("cache.expiration_time"));
    private static final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    public static JsonObject sendRequest(String endpoint) throws Exception {
        logger.info("Received request for endpoint: " + endpoint);

        // Check cache
        String cachedResponse = getFromCache(endpoint);
        if (cachedResponse != null) {
            logger.info("Cache hit for endpoint: " + endpoint);
            return JsonParser.parseString(cachedResponse).getAsJsonObject();
        }

        logger.info("Cache miss for endpoint: " + endpoint + ". Sending API request.");

        String urlString = BASE_URL + endpoint + (endpoint.contains("?") ? "&" : "?") + "api_key=" + API_KEY;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            putInCache(endpoint, response.toString());
            return JsonParser.parseString(response.toString()).getAsJsonObject();
        }
    }

    private static String getFromCache(String endpoint) {
        CacheEntry cacheEntry = cache.get(endpoint);
        if (cacheEntry != null && (System.currentTimeMillis() - cacheEntry.timestamp) <= CACHE_EXPIRATION_TIME) {
            return cacheEntry.response;
        }
        cache.remove(endpoint);
        return null;
    }

    private static void putInCache(String endpoint, String response) {
        cache.put(endpoint, new CacheEntry(response, System.currentTimeMillis()));
    }

    private static class CacheEntry {
        String response;
        long timestamp;

        CacheEntry(String response, long timestamp) {
            this.response = response;
            this.timestamp = timestamp;
        }
    }
}
