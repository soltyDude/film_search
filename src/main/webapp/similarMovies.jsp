<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Similar Movies</title>
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
        .pagination {
            margin-top: 20px;
            display: flex;
            justify-content: center;
            gap: 10px;
        }
        .pagination a {
            text-decoration: none;
            color: #007BFF;
            border: 1px solid #007BFF;
            padding: 5px 10px;
            border-radius: 4px;
            transition: background-color 0.2s;
        }
        .pagination a:hover {
            background-color: #007BFF;
            color: #fff;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Similar Movies</h1>
    <div class="movie-list">
        <%
            // Получение списка фильмов и текущей страницы из request
            List<Map<String, String>> similarMovies = (List<Map<String, String>>) request.getAttribute("similarMovies");
            int currentPage = (int) request.getAttribute("currentPage");
            int pageSize = (int) request.getAttribute("pageSize");

            if (similarMovies != null && !similarMovies.isEmpty()) {
                for (Map<String, String> movie : similarMovies) {
        %>
        <div class="movie-item">
            <a href="movie?id=<%= movie.get("id") %>">
                <img src="<%= movie.get("poster_url") %>" alt="<%= movie.get("title") %>">
            </a>
            <h3><%= movie.get("title") %></h3>
        </div>
        <%
            }
        } else {
        %>
        <p>No similar movies found.</p>
        <%
            }
        %>
    </div>
    <div class="pagination">
        <%
            // Вычисление предыдущей и следующей страницы
            int prevPage = currentPage > 1 ? currentPage - 1 : 1;
            int nextPage = currentPage + 1;

            // Генерация ссылок для пагинации
        %>
        <a href="similarMovies.jsp?page=<%= prevPage %>&size=<%= pageSize %>">Previous</a>
        <a href="similarMovies.jsp?page=<%= nextPage %>&size=<%= pageSize %>">Next</a>
    </div>
</div>
</body>
</html>
