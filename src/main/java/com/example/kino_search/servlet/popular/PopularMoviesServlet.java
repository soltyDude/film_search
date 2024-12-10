package com.example.kino_search.servlet.popular;

import com.example.kino_search.util.TMDBApiUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopularMoviesServlet extends HttpServlet {

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String endpoint = "/movie/popular";
            JsonObject jsonResponse = TMDBApiUtil.sendRequest(endpoint);

            List<Map<String, String>> popularMovies = new ArrayList<>();
            JsonArray results = jsonResponse.getAsJsonArray("results");

            for (JsonElement element : results) {
                JsonObject movieJson = element.getAsJsonObject();

                Map<String, String> movie = new HashMap<>();
                movie.put("id", movieJson.get("id").getAsString());
                movie.put("title", movieJson.get("title").getAsString());
                if (movieJson.has("poster_path") && !movieJson.get("poster_path").isJsonNull()) {
                    movie.put("poster_url", IMAGE_BASE_URL + movieJson.get("poster_path").getAsString());
                } else {
                    movie.put("poster_url", "https://via.placeholder.com/500x750?text=No+Image");
                }
                popularMovies.add(movie);
            }

            // Форматируем текущую дату для отображения
            String currentDate = new SimpleDateFormat("MMMM dd, yyyy").format(new Date());
            request.setAttribute("currentDate", currentDate);

            // Передаём данные на JSP
            request.setAttribute("popularMovies", popularMovies);
            request.getRequestDispatcher("popular.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to fetch popular movies.");
        }
    }
}
