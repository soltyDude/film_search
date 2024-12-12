package com.example.kino_search.servlet.rdandomFilm;

import com.example.kino_search.db.dao.FilmDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public class RandomFilmServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IOException, ServletException {

        Map<String, Object> film = FilmDAO.getRandomFilm();
        if (film == null || film.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No films found");
            return;
        }

        // Можно сразу перенаправить на JSP:
        request.setAttribute("film", film);
        request.getRequestDispatcher("randomFilm.jsp").forward(request, response);
    }
}