<%@ page import="java.util.Map" %>
<%
  Map<String,Object> film = (Map<String,Object>) request.getAttribute("film");
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
<p>Title: <%= film.get("title") %></p>
<img src="<%= film.get("poster_url") %>" alt="<%= film.get("title") %>" style="width:200px;">
<!-- Можно добавить ссылку на страницу с деталями фильма -->
<a href="movie?id=<%= film.get("apiId") %>">View Details</a>
</body>
</html>
