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
    }
    .movie-details p {
      color: #555;
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
      <p><strong>Rating:</strong> <%= request.getAttribute("rating") %></p> <!-- Отображение рейтинга -->
      <p><%= request.getAttribute("overview") %></p>

    </div>
  </div>
</div>
</body>
</html>
