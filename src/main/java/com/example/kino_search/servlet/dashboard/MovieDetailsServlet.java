package com.example.kino_search.servlet.dashboard;

import com.example.kino_search.db.FilmService;
import com.example.kino_search.util.TMDBApiUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Logger;


public class MovieDetailsServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(MovieDetailsServlet.class.getName());
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String movieId = request.getParameter("id");
        logger.info("Received request for movie details. Query parameter 'apiId': " + movieId);



        HttpSession session = request.getSession(false); // false - чтобы избежать создания новой сессии
        Integer userId = null;
        if (session != null && session.getAttribute("userId") != null) {
            userId = (Integer) session.getAttribute("userId");

        } else {
            logger.info("Received request for movie details. Query parameter 'userId': " + userId);
            response.sendRedirect("login.jsp");
        }

        logger.info("User ID retrieved from session: " + userId);

        //добавляем фильм в датабазу если он когото заинтересовал
        FilmService.fetchAndSaveFilm(Integer.parseInt(movieId));

        if (movieId == null || movieId.trim().isEmpty()) {
            request.setAttribute("error", "Movie ID is required.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        try {
            String endpoint = "/movie/" + movieId;
            JsonObject movieDetails = TMDBApiUtil.sendRequest(endpoint);

            //этобрать из базы

            // Передаем данные фильма на JSP
            request.setAttribute("title", movieDetails.get("title").getAsString());
            request.setAttribute("overview", movieDetails.get("overview").getAsString());
            request.setAttribute("release_date", movieDetails.get("release_date").getAsString());
            request.setAttribute("poster_url", IMAGE_BASE_URL + movieDetails.get("poster_path").getAsString());
            request.setAttribute("apiId", movieId);

            request.setAttribute("playlistId", 1); // или другой ID плейлист


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