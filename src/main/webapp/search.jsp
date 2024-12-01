<%@ page import="java.util.Map" %>
<%@ page import="java.util.List"%>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Search Movies</title>
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
        form {
            text-align: center;
            margin-bottom: 20px;
        }
        input[type="text"] {
            padding: 10px;
            width: 70%;
            border: 1px solid #ccc;
            border-radius: 4px;
            margin-right: 10px;
        }
        button {
            padding: 10px 20px;
            border: none;
            background-color: #007bff;
            color: white;
            border-radius: 4px;
            cursor: pointer;
        }
        button:hover {
            background-color: #0056b3;
        }
        .movie-list {
            margin-top: 20px;
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
        .movie-item p {
            font-size: 14px;
            color: #555;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Search Movies</h1>
    <form action="search" method="get">
        <input type="text" name="query" placeholder="Enter movie keywords" required>
        <button type="submit">Search</button>
    </form>
    <div class="movie-list">
        <%
            List<Map<String, String>> movies = (List<Map<String, String>>) request.getAttribute("movies");
            if (movies != null) {
                for (Map<String, String> movie : movies) {
        %>
        <div class="movie-item">
            <% if (!movie.get("poster_url").isEmpty()) { %>
            <img src="<%= movie.get("poster_url") %>" alt="<%= movie.get("title") %>">
            <% } %>
            <h3><%= movie.get("title") %></h3>
            <p><%= movie.get("release_date") %></p>
        </div>
        <%
            }
        } else if (request.getAttribute("error") != null) {
        %>
        <p><%= request.getAttribute("error") %></p>
        <%
            }
        %>
    </div>
</div>
</body>
</html>