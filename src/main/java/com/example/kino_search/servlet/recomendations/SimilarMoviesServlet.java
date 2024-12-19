package com.example.kino_search.servlet.recomendations;

import com.example.kino_search.util.TMDBApiUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimilarMoviesServlet extends HttpServlet {

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String apiId = request.getParameter("apiId");

        if (apiId == null || apiId.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid movie ID.");
            return;
        }

        try {
            // Fetch similar movies from TMDB API
            String endpoint = "/movie/" + apiId + "/similar";
            JsonObject jsonResponse = TMDBApiUtil.sendRequest(endpoint);

            List<Map<String, String>> similarMovies = new ArrayList<>();
            JsonArray results = jsonResponse.getAsJsonArray("results");

            for (int i = 0; i < results.size(); i++) {
                JsonObject movieJson = results.get(i).getAsJsonObject();

                Map<String, String> movie = new HashMap<>();
                movie.put("id", movieJson.get("id").getAsString());
                movie.put("title", movieJson.get("title").getAsString());

                if (movieJson.has("poster_path") && !movieJson.get("poster_path").isJsonNull()) {
                    movie.put("poster_url", IMAGE_BASE_URL + movieJson.get("poster_path").getAsString());
                    similarMovies.add(movie);
                }
            }

            // Forward the data to JSP
            request.setAttribute("similarMovies", similarMovies);
            request.getRequestDispatcher("similarMovies.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to fetch similar movies.");
        }
    }
}
