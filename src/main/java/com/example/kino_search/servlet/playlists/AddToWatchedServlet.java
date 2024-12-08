package com.example.kino_search.servlet.playlists;

import com.example.kino_search.db.FilmService;
import com.example.kino_search.db.dao.ViewedMoviesDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

public class AddToWatchedServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(AddToWatchedServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String apiId = request.getParameter("apiId");
        String userId = request.getParameter("userId");

        logger.info("Received API ID: " + apiId + ", User ID: " + userId);

        if (apiId == null || userId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "API ID and User ID are required.");
            return;
        }

        try {
            int parsedApiId = Integer.parseInt(apiId);
            int parsedUserId = Integer.parseInt(userId);

            // Get the film ID from the database
            int filmId = FilmService.getFilmIdByApiId(parsedApiId);

            // Add a placeholder review (rating and review_text can be updated later)
            //int reviewId = ReviewDAO.addPlaceholderReview(parsedUserId, filmId);

            // Add the movie to "viewed_movies"
            boolean addedToWatched = ViewedMoviesDAO.addMovieToViewed(parsedUserId, filmId, null);

            if (addedToWatched) {
                String filmTitle = FilmService.getFilmTitleByID(filmId); // Получить название фильма
                String playlistName = "Watched"; // Название плейлиста или логика его получения

                request.setAttribute("filmTitle", filmTitle);
                request.setAttribute("playlistName", playlistName);

                request.setAttribute("successMessage", "Movie was successfully added to your watched list.");
                request.getRequestDispatcher("/success.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "The movie already exists in your watched list.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } catch (Exception e) {
            logger.severe("Error while adding to watched: " + e.getMessage());
            request.setAttribute("errorMessage", "An error occurred while processing your request.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
