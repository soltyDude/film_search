<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Playlist Details</title>
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
    <h1>Playlist: <%= request.getAttribute("playlistName") %></h1>
    <div class="movie-list">
        <%
            List<Map<String, Object>> films = (List<Map<String, Object>>) request.getAttribute("films");
            if (films != null && !films.isEmpty()) {
                for (Map<String, Object> film : films) {
        %>
        <div class="movie-item">
            <a href="movie?id=<%= film.get("apiId") %>">
                <img src="<%= film.get("poster_url") %>" alt="<%= film.get("title") %>">
            </a>
            <h3><%= film.get("title") %></h3>
        </div>
        <%
            }
        } else {
        %>
        <p>No movies in this playlist yet.</p>
        <%
            }
        %>
    </div>
</div>
</body>
</html>
