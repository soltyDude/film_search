<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Viewed Movies</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f9f9f9;
      margin: 0;
      padding: 0;
    }
    .container {
      margin: 20px auto;
      max-width: 800px;
      padding: 20px;
      background: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
    }
    h1 {
      text-align: center;
    }
    .movie-list {
      display: flex;
      flex-wrap: wrap;
      gap: 20px;
    }
    .movie-item {
      width: 200px;
      text-align: center;
    }
    .movie-item img {
      max-width: 100%;
      border-radius: 8px;
    }
    .movie-item h3 {
      font-size: 16px;
      margin: 10px 0 5px;
    }
    .viewed-at {
      color: #888;
      font-size: 12px;
    }
    .review-button {
      margin-top: 10px;
    }
    .review-button button {
      background-color: #007bff;
      color: #fff;
      border: none;
      padding: 5px 10px;
      border-radius: 5px;
      cursor: pointer;
    }
    .review-button button:hover {
      background-color: #0056b3;
    }
  </style>
</head>
<body>
<div class="container">
  <h1>Your Watched Movies</h1>
  <div class="movie-list">
    <%
      List<Map<String, Object>> viewedMovies = (List<Map<String, Object>>) request.getAttribute("viewedMovies");
      Integer userId = (Integer) session.getAttribute("userId"); // Предполагается, что userId хранится в сессии
      if (viewedMovies != null && !viewedMovies.isEmpty()) {
        for (Map<String, Object> movie : viewedMovies) {
          Object ratingObj = movie.get("rating");
    %>
    <div class="movie-item">
      <a href="movie?id=<%= movie.get("apiId") %>">
        <img src="<%= movie.get("poster_url") %>" alt="<%= movie.get("title") %>">
      </a>
      <h3><%= movie.get("title") %></h3>
      <p class="viewed-at">Viewed at: <%= movie.get("viewed_at") %></p>
      <%
        if (ratingObj != null) {
      %>
      <p>Your rating: <%= ratingObj %></p>
      <div class="review-button">
        <!-- Кнопка для редактирования отзыва -->
        <form action="<%= request.getContextPath() %>/reviewForm.jsp" method="get">
          <input type="hidden" name="apiId" value="<%= movie.get("apiId") %>">
          <input type="hidden" name="userId" value="<%= userId %>">
          <button type="submit">Edit Review</button>
        </form>
      </div>
      <%
      } else {
      %>
      <div class="review-button">
        <!-- Кнопка для добавления отзыва -->
        <form action="<%= request.getContextPath() %>/reviewForm.jsp" method="get">
          <input type="hidden" name="apiId" value="<%= movie.get("apiId") %>">
          <input type="hidden" name="userId" value="<%= userId %>">
          <button type="submit">Add Review</button>
        </form>
      </div>
      <%
        }
      %>
    </div>
    <%
      }
    } else {
    %>
    <p>You have not watched any movies yet.</p>
    <%
      }
    %>
  </div>
</div>
</body>
</html>
