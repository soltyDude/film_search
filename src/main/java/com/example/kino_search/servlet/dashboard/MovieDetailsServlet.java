package com.example.kino_search.servlet.dashboard;

import com.example.kino_search.db.dao.PlaylistDAO;
import com.example.kino_search.db.FilmService;
import com.example.kino_search.db.dao.ReviewDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class MovieDetailsServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(MovieDetailsServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String movieAPIId = request.getParameter("id");
        logger.info("Received request for movie details. Query parameter 'apiId': " + movieAPIId);

        HttpSession session = request.getSession(false);
        Integer userId = null;
        if (session != null && session.getAttribute("userId") != null) {
            userId = (Integer) session.getAttribute("userId");
        } else {
            response.sendRedirect("login.jsp");
            return;
        }

        if (movieAPIId == null || movieAPIId.trim().isEmpty()) {
            request.setAttribute("error", "Movie ID is required.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        try {
            // Добавляем фильм в базу данных, если его ещё нет
            FilmService.fetchAndSaveFilm(Integer.parseInt(movieAPIId));

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

            // Получаем список отзывов
            List<Map<String, Object>> reviews = ReviewDAO.getReviewsByFilmId(filmId);

            // Передаем данные фильма на JSP
            request.setAttribute("title", movieDetails.get("title"));
            request.setAttribute("overview", movieDetails.get("overview"));
            request.setAttribute("release_date", movieDetails.get("release_date"));
            request.setAttribute("poster_url", movieDetails.get("poster_url"));
            request.setAttribute("apiId", movieAPIId);
            request.setAttribute("playlistId", playlistId);
            request.setAttribute("reviews", reviews);

            // Передаём рейтинг из API и внутренний рейтинг
            request.setAttribute("api_rating", movieDetails.get("api_rating"));
            request.setAttribute("rating", movieDetails.get("rating"));
            request.setAttribute("internalRating", movieDetails.get("rating") != null ? movieDetails.get("rating") : "N/A");

            System.out.println(movieDetails.get("api_rating").toString());
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
