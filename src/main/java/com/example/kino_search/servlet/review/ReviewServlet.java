package com.example.kino_search.servlet.review;

import com.example.kino_search.db.FilmService;
import com.example.kino_search.db.dao.ReviewDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ReviewServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            int filmAPIId = Integer.parseInt(request.getParameter("filmAPIId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String reviewText = request.getParameter("reviewText");

            int filmId = FilmService.getFilmIdByApiId(filmAPIId);

            if (ReviewDAO.isReviewExists(userId, filmId)) {
                request.setAttribute("errorMessage", "You have already submitted a review for this movie.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Добавляем отзыв в базу данных
            boolean success = ReviewDAO.addReview(userId, filmAPIId, rating, reviewText);

            if (success) {
                response.sendRedirect("movie?id=" + filmAPIId); // Перенаправление на страницу фильма
            } else {
                request.setAttribute("errorMessage", "Failed to submit the review. Try again.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while processing your review.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
