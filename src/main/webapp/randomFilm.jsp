<%@ page import="com.example.kino_search.model.Film" %>
<%
  Film film = (Film) request.getAttribute("film");
  if (film == null) {
    out.print("No film found");
    return;
  }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Random Movie</title>
  <style>
    /* Пример: стиль рулетки или анимации можно добавить отдельно */
  </style>
</head>
<body>
<h1>Random Movie</h1>
<p>Title: <%= film.getTitle() %></p>
<img src="<%= film.getPosterUrl() %>" alt="<%= film.getTitle() %>" style="width:200px;">
<!-- Можно добавить ссылку на страницу с деталями фильма -->
<a href="movie?id=<%= film.getApiId() %>">View Details</a>
</body>
</html>
