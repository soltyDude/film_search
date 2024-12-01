package com.example.kino_search.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TMDBApiUtil {

    private static final String API_KEY = "e28fe83118014486bd75c60ecd32ede4"; // Замените на ваш API ключ
    private static final String BASE_URL = "https://api.themoviedb.org/3";

    // Кэш: хранит результаты запросов и время их добавления
    private static final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    // Время жизни кэша (в миллисекундах)
    private static final long CACHE_EXPIRATION_TIME = 10 * 60 * 1000; // 10 минут

    /**
     * Отправляет GET-запрос к TMDB API и возвращает результат как строку.
     *
     * @param endpoint Эндпоинт API (например, "/movie/popular")
     * @return Результат запроса в формате JSON (строка)
     * @throws Exception Если запрос не удался
     */
    public static String sendRequest(String endpoint) throws Exception {
        // Проверяем кэш
        String cachedResponse = getFromCache(endpoint);
        if (cachedResponse != null) {
            System.out.println("Cache hit for endpoint: " + endpoint);
            return cachedResponse; // Возвращаем данные из кэша
        }

        // Если данных в кэше нет, отправляем запрос
        String urlString = BASE_URL + endpoint + (endpoint.contains("?") ? "&" : "?") + "api_key=" + API_KEY;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("HTTP GET Request Failed with Error Code: " + responseCode);
        }

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

        return responseString;
    }

    /**
     * Получение данных из кэша, если они не устарели.
     *
     * @param endpoint Эндпоинт API
     * @return Данные из кэша или null, если их нет или они устарели
     */
    private static String getFromCache(String endpoint) {
        CacheEntry cacheEntry = cache.get(endpoint);
        if (cacheEntry != null && (System.currentTimeMillis() - cacheEntry.timestamp) <= CACHE_EXPIRATION_TIME) {
            return cacheEntry.response;
        }
        // Удаляем устаревший кэш
        cache.remove(endpoint);
        return null;
    }

    /**
     * Сохраняем данные в кэш.
     *
     * @param endpoint Эндпоинт API
     * @param response Ответ API
     */
    private static void putInCache(String endpoint, String response) {
        cache.put(endpoint, new CacheEntry(response, System.currentTimeMillis()));
        System.out.println("Cached response for endpoint: " + endpoint);
    }

    /**
     * Внутренний класс для хранения записи кэша.
     */
    private static class CacheEntry {
        String response;
        long timestamp;

        CacheEntry(String response, long timestamp) {
            this.response = response;
            this.timestamp = timestamp;
        }
    }
}
