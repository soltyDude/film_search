package com.example.kino_search.servlet.dashboard;

import com.example.kino_search.db.dao.PlaylistDAO;
import com.example.kino_search.db.FilmService;
import com.example.kino_search.util.TMDBApiUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;


public class MovieDetailsServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(MovieDetailsServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String movieAPIId = request.getParameter("id");
        logger.info("Received request for movie details. Query parameter 'apiId': " + movieAPIId);



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
        FilmService.fetchAndSaveFilm(Integer.parseInt(movieAPIId));

        if (movieAPIId == null || movieAPIId.trim().isEmpty()) {
            request.setAttribute("error", "Movie ID is required.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        try {
            // Получаем информацию о фильме из базы данных
            int filmId = FilmService.getFilmIdByApiId(Integer.parseInt(movieAPIId));

            if (filmId == -1) {
                throw new Exception("Film not found in the database for API ID: " + movieAPIId);
            }

            // Получаем данные о фильме из базы данных
            Map<String, Object> movieDetails = FilmService.getFilmDetailsById(filmId);

            // Получаем ID плейлиста "Want to Watch" для текущего пользователя
            int playlistId = PlaylistDAO.getWantToWatchPlaylistId(userId);

            if (playlistId == -1) {
                throw new Exception("Want to Watch playlist not found for user ID: " + userId);
            }
            // Передаем данные фильма на JSP
            request.setAttribute("title", movieDetails.get("title"));
            request.setAttribute("overview", movieDetails.get("overview"));
            request.setAttribute("release_date", movieDetails.get("release_date"));
            request.setAttribute("poster_url", movieDetails.get("poster_url"));
            request.setAttribute("apiId", movieAPIId);
            request.setAttribute("playlistId", playlistId); // или другой ID плейлист

            // Рейтинг
            Object rating = movieDetails.get("rating");
            if (rating != null) {
                request.setAttribute("rating", rating.toString());
            } else {
                request.setAttribute("rating", "N/A");
            }

            logger.info("Successfully retrieved movie details from the database for filmId: " + filmId);

        } catch (Exception e) {
            logger.severe("Failed to load movie details from the database. Error: " + e.getMessage());
            request.setAttribute("error", "Failed to load movie details.");
            e.printStackTrace();
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("movie.jsp").forward(request, response);

    }
}