package com.example.kino_search.servlet.dashboard;

import com.example.kino_search.util.TMDBApiUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;

import java.io.IOException;

public class MovieDetailsServlet extends HttpServlet {

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String movieId = request.getParameter("id");

        if (movieId == null || movieId.trim().isEmpty()) {
            request.setAttribute("error", "Movie ID is required.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        try {
            String endpoint = "/movie/" + movieId;
            JsonObject movieDetails = TMDBApiUtil.sendRequest(endpoint);

            // Передаем данные фильма на JSP
            request.setAttribute("title", movieDetails.get("title").getAsString());
            request.setAttribute("overview", movieDetails.get("overview").getAsString());
            request.setAttribute("release_date", movieDetails.get("release_date").getAsString());
            request.setAttribute("poster_url", IMAGE_BASE_URL + movieDetails.get("poster_path").getAsString());

            // Рейтинг
            if (movieDetails.has("vote_average") && !movieDetails.get("vote_average").isJsonNull()) {
                request.setAttribute("rating", movieDetails.get("vote_average").getAsString());
            } else {
                request.setAttribute("rating", "N/A");
            }

            // доп сюда

        } catch (Exception e) {
            request.setAttribute("error", "Failed to load movie details.");
            e.printStackTrace();
        }

        request.getRequestDispatcher("movie.jsp").forward(request, response);
    }
}