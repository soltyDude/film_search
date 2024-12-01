package com.example.kino_search.servlet.dashboard;

import com.example.kino_search.util.TMDBApiUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchServlet extends HttpServlet {

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");

        if (query == null || query.trim().isEmpty()) {
            request.setAttribute("error", "Please enter a valid search query.");
            request.getRequestDispatcher("search.jsp").forward(request, response);
            return;
        }

        try {
            String endpoint = "/search/movie?query=" + query;
            JsonObject jsonObject = TMDBApiUtil.sendRequest(endpoint);

            // Получаем массив результатов
            JsonArray results = jsonObject.getAsJsonArray("results");

            // Преобразуем JSON в список фильмов
            List<Map<String, String>> movies = new ArrayList<>();
            for (int i = 0; i < results.size(); i++) {
                JsonObject movie = results.get(i).getAsJsonObject();
                Map<String, String> movieData = new HashMap<>();
                movieData.put("id", movie.get("id").getAsString()); // Убедитесь, что ID добавляется
                movieData.put("title", movie.get("title").getAsString());
                movieData.put("overview", movie.get("overview").getAsString());
                movieData.put("release_date", movie.get("release_date").getAsString());

                // Добавляем постер
                if (movie.has("poster_path") && !movie.get("poster_path").isJsonNull()) {
                    movieData.put("poster_url", IMAGE_BASE_URL + movie.get("poster_path").getAsString());
                } else {
                    movieData.put("poster_url", ""); // Если постера нет
                }


                movies.add(movieData);
            }

// Передаем список фильмов на JSP
            request.setAttribute("movies", movies);

        } catch (Exception e) {
            request.setAttribute("error", "Failed to retrieve movies. Please try again later.");
            e.printStackTrace();
        }

        // Переход на search.jsp
        request.getRequestDispatcher("search.jsp").forward(request, response);
    }
}
