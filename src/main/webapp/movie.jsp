<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title><%= request.getAttribute("title") %></title>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 0;
      padding: 0;
      background-color: #f9f9f9;
    }
    .container {
      margin: 20px auto;
      max-width: 800px;
      padding: 20px;
      background: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
    }
    .movie-header {
      display: flex;
      gap: 20px;
    }
    .movie-header img {
      max-width: 300px;
      border-radius: 8px;
    }
    .movie-details {
      flex: 1;
    }
    .movie-details h1 {
      margin: 0;
      color: #333;
    }
    .movie-details p {
      color: #555;
      margin: 8px 0;
    }
    .actions {
      display: flex;
      gap: 10px;
      margin-top: 20px;
    }
    .actions button {
      background-color: #007bff;
      color: #fff;
      border: none;
      padding: 10px 20px;
      font-size: 16px;
      border-radius: 5px;
      cursor: pointer;
    }
    .actions button:hover {
      background-color: #0056b3;
    }
    .reviews {
      margin-top: 40px;
    }
    .reviews h2 {
      margin-bottom: 20px;
      color: #333;
    }
    .review {
      padding: 10px 15px;
      border: 1px solid #ddd;
      border-radius: 5px;
      margin-bottom: 10px;
      background: #f9f9f9;
    }
    .review p {
      margin: 5px 0;
    }
    .review em {
      color: #888;
    }
  </style>
</head>
<body>
<div class="container">
  <div class="movie-header">
    <img src="<%= request.getAttribute("poster_url") %>" alt="<%= request.getAttribute("title") %>">
    <div class="movie-details">
      <h1><%= request.getAttribute("title") %></h1>
      <p><strong>Release Date:</strong> <%= request.getAttribute("release_date") %></p>
      <p><strong>TMDB Rating:</strong> <%= request.getAttribute("api_rating") %> / 10</p>
      <p><strong>Internal Rating:</strong> <%= request.getAttribute("rating") %> / 10</p>
      <p><%= request.getAttribute("overview") %></p>
    </div>
  </div>

  <div class="actions">
    <form action="<%= request.getContextPath() %>/addFilmToPlaylist" method="post">
      <input type="hidden" name="playlistId" value="<%= request.getAttribute("playlistId") %>">
      <input type="hidden" name="apiId" value="<%= request.getAttribute("apiId") %>">
      <button type="submit">Want to Watch</button>
    </form>

    <form action="<%= request.getContextPath() %>/addToWatched" method="post">
      <input type="hidden" name="apiId" value="<%= request.getAttribute("apiId") %>">
      <input type="hidden" name="userId" value="<%= session.getAttribute("userId") %>">
      <button type="submit">Add to Watched</button>
    </form>
  </div>

  <div class="reviews">
    <h2>Reviews:</h2>
    <form action="addReview" method="post" style="margin-bottom: 20px;">
      <label for="rating">Your Rating:</label>
      <input type="range" id="rating" name="rating" min="1" max="10" value="5" required>
      <label for="reviewText">Your Review:</label>
      <textarea id="reviewText" name="reviewText" placeholder="Write your thoughts..." required></textarea>
      <input type="hidden" name="filmAPIId" value="<%= request.getAttribute("apiId") %>">
      <input type="hidden" name="userId" value="<%= session.getAttribute("userId") %>">
      <button type="submit">Submit Review</button>
    </form>

    <%
      List<Map<String, Object>> reviews = (List<Map<String, Object>>) request.getAttribute("reviews");
      if (reviews != null && !reviews.isEmpty()) {
        for (Map<String, Object> review : reviews) {
    %>
    <div class="review">
      <p><strong>Rating:</strong> <%= review.get("rating") %> stars</p>
      <p><%= review.get("review_text") %></p>
      <p><em>Reviewed by User <%= review.get("user_nickname") %></em></p>
    </div>
    <%
      }
    } else {
    %>
    <p>No reviews yet. Be the first to write one!</p>
    <%
      }
    %>
  </div>
</div>
</body>
</html>
