package com.example.kino_search.servlet.review;

import com.example.kino_search.db.FilmService;
import com.example.kino_search.db.dao.ReviewDAO;
import com.example.kino_search.db.dao.ViewedMoviesDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReviewServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ReviewServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            int filmAPIId = Integer.parseInt(request.getParameter("filmAPIId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String reviewText = request.getParameter("reviewText");

            logger.info("Received review: userId=" + userId + ", filmAPIId=" + filmAPIId + ", rating=" + rating);

            int filmId = FilmService.getFilmIdByApiId(filmAPIId);

            // Проверяем, существует ли фильм в просмотренных, если нет - добавляем
            if (!ViewedMoviesDAO.isMovieInViewed(userId, filmId)) {
                boolean addedToViewed = ViewedMoviesDAO.addMovieToViewed(userId, filmId, null);
                logger.info("Added film to viewed list: " + addedToViewed);
            }

            boolean reviewExists = ReviewDAO.isReviewExists(userId, filmId);

            boolean success;
            if (reviewExists) {
                success = ReviewDAO.updateReview(userId, filmId, rating, reviewText);
                logger.info("Review updated: " + success);
            } else {
                success = ReviewDAO.addReview(userId, filmAPIId, rating, reviewText);
                logger.info("Review added: " + success);
            }

            if (success) {
                // После успешного добавления/обновления отзыва пересчитываем рейтинг фильма
                boolean updatedRating = FilmService.updateFilmRatingAndCount(filmId);
                logger.info("Updated film rating and count: " + updatedRating);

                response.sendRedirect("movie?id=" + filmAPIId);
            } else {
                logger.warning("Failed to add or update review.");
                request.setAttribute("errorMessage", "Failed to submit the review. Try again.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing review submission", e);
            request.setAttribute("errorMessage", "An error occurred while processing your review.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
