<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Recommended Movies</title>
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
    </style>
</head>
<body>
<div class="container">
    <h1>Recommended Movies</h1>
    <div class="movie-list">
        <%
            List<Map<String, String>> recommendations = (List<Map<String, String>>) request.getAttribute("recommendations");
            if (recommendations != null && !recommendations.isEmpty()) {
                for (Map<String, String> movie : recommendations) {
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
        <p>No recommendations available at the moment.</p>
        <%
            }
        %>
    </div>
</div>
</body>
</html>
