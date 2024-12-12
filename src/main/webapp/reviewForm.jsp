<%@ page import="com.example.kino_search.db.FilmService" %>
<%@ page import="com.example.kino_search.db.dao.ReviewDAO" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Integer userId = null;
    Integer apiId = null;

    try {
        userId = Integer.parseInt(request.getParameter("userId"));
        apiId = Integer.parseInt(request.getParameter("apiId"));
    } catch (NumberFormatException e) {
        // если что-то не так с параметрами
    }

    if (userId == null || apiId == null) {
        // Ошибка: недостающие параметры
        request.setAttribute("errorMessage", "User ID and API ID are required.");
        request.getRequestDispatcher("error.jsp").forward(request, response);
        return;
    }

    int filmId = FilmService.getFilmIdByApiId(apiId);
    Map<String, Object> existingReview = ReviewDAO.getReviewByUserAndFilm(userId, filmId);

    Integer existingRating = null;
    String existingReviewText = "";
    if (existingReview != null) {
        existingRating = (Integer) existingReview.get("rating");
        existingReviewText = (String) existingReview.get("review_text");
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><%= (existingReview != null) ? "Edit Review" : "Add Review" %></title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 0;
        }
        .container {
            margin: 20px auto;
            max-width: 500px;
            padding: 20px;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2);
        }
        h1 {
            text-align: center;
        }
        form {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }
        label {
            font-weight: bold;
        }
        input[type="number"], textarea {
            width: 100%;
            padding: 8px;
            font-size: 14px;
        }
        button {
            background-color: #007bff;
            color: #fff;
            border: none;
            padding: 10px;
            border-radius: 5px;
            cursor: pointer;
        }
        button:hover {
            background-color: #0056b3;
        }
        .back-link {
            display: block;
            margin-top: 20px;
            text-align: center;
        }
        .back-link a {
            color: #007bff;
            text-decoration: none;
        }
        .back-link a:hover {
            text-decoration: underline;
        }

        input[type=range] {
            width: 100%;
        }
    </style>
</head>
<body>
<div class="container">
    <h1><%= (existingReview != null) ? "Edit Your Review" : "Add Your Review" %></h1>
    <form action="<%= request.getContextPath() %>/addReview" method="post">
        <input type="hidden" name="userId" value="<%= userId %>">
        <input type="hidden" name="filmAPIId" value="<%= apiId %>">

        <label for="rating">Your Rating (1-10):</label>
        <input type="range" id="rating" name="rating" min="1" max="10"
               value="<%= (existingRating != null) ? existingRating : 5 %>" required
               oninput="document.getElementById('ratingValue').innerText = this.value">
        <span id="ratingValue"><%= (existingRating != null) ? existingRating : 5 %></span>

        <label for="reviewText">Review:</label>
        <textarea name="reviewText" id="reviewText" required><%= existingReviewText %></textarea>

        <button type="submit"><%= (existingReview != null) ? "Update Review" : "Add Review" %></button>
    </form>
    <div class="back-link">
        <a href="whatched">Back to Viewed Movies</a>
    </div>
</div>
</body>
</html>
