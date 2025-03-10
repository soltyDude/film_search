package com.example.kino_search.servlet.recomendations;

import com.example.kino_search.db.FilmService;
import com.example.kino_search.model.Film;
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
        String pageParam = request.getParameter("page");
        String sizeParam = request.getParameter("size");

        if (apiId == null || apiId.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid movie ID.");
            return;
        }

        // Установка значений для пагинации
        int page = pageParam != null ? Integer.parseInt(pageParam) : 1; // По умолчанию первая страница
        int size = sizeParam != null ? Integer.parseInt(sizeParam) : 10; // По умолчанию 10 записей

        try {
            // Получение фильмов с пагинацией
            List<Film> paginatedFilms = FilmService.getInstance().getPaginatedFilms(page, size);

            // Преобразование фильмов в формат для JSP
            List<Map<String, String>> similarMovies = new ArrayList<>();
            for (Film film : paginatedFilms) {
                Map<String, String> movieData = new HashMap<>();
                movieData.put("id", String.valueOf(film.getApiId()));
                movieData.put("title", film.getTitle());
                movieData.put("poster_url", film.getPosterUrl());
                similarMovies.add(movieData);
            }

            // Передача данных в JSP
            request.setAttribute("similarMovies", similarMovies);
            request.setAttribute("currentPage", page);
            request.setAttribute("pageSize", size);
            request.getRequestDispatcher("similarMovies.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to fetch similar movies.");
        }
    }

}
