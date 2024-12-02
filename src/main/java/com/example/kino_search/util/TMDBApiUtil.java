package com.example.kino_search.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TMDBApiUtil {

    private static final Logger logger = Logger.getLogger(TMDBApiUtil.class.getName());

    private static final String API_KEY = "e28fe83118014486bd75c60ecd32ede4"; // Ваш API ключ
    private static final String BASE_URL = "https://api.themoviedb.org/3";

    // Кэш
    private static final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    // Время жизни кэша
    private static final long CACHE_EXPIRATION_TIME = 10 * 60 * 1000; // 10 минут

    public static JsonObject sendRequest(String endpoint) throws Exception {
        logger.info("Received request for endpoint: " + endpoint);

        // Проверяем кэш
        String cachedResponse = getFromCache(endpoint);
        if (cachedResponse != null) {
            logger.info("Cache hit for endpoint: " + endpoint);
            return JsonParser.parseString(cachedResponse).getAsJsonObject();
        }

        logger.info("Cache miss for endpoint: " + endpoint + ". Sending API request.");

        // Если данных в кэше нет, отправляем запрос
        String urlString = BASE_URL + endpoint + (endpoint.contains("?") ? "&" : "?") + "api_key=" + API_KEY;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            logger.log(Level.SEVERE, "API request failed for endpoint: " + endpoint + " with response code: " + responseCode);
            throw new RuntimeException("HTTP GET Request Failed with Error Code: " + responseCode);
        }

        logger.info("API request successful for endpoint: " + endpoint);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Сохраняем результат в кэш
        String responseString = response.toString();
        putInCache(endpoint, responseString);

        logger.info("Response cached for endpoint: " + endpoint);
        return JsonParser.parseString(responseString).getAsJsonObject();
    }

    private static String getFromCache(String endpoint) {
        CacheEntry cacheEntry = cache.get(endpoint);
        if (cacheEntry != null && (System.currentTimeMillis() - cacheEntry.timestamp) <= CACHE_EXPIRATION_TIME) {
            logger.info("Cache entry is valid for endpoint: " + endpoint);
            return cacheEntry.response;
        }

        if (cacheEntry != null) {
            logger.info("Cache expired for endpoint: " + endpoint + ". Removing from cache.");
        }
        cache.remove(endpoint);
        return null;
    }

    private static void putInCache(String endpoint, String response) {
        cache.put(endpoint, new CacheEntry(response, System.currentTimeMillis()));
        logger.info("Cached response for endpoint: " + endpoint);
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
