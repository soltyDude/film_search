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
            position: relative;
        }
        .movie-item img {
            max-width: 100%;
            border-radius: 8px;
        }
        .movie-item h3 {
            font-size: 16px;
            margin: 10px 0 5px;
        }
        .remove-btn {
            background-color: #ff4d4f;
            color: #fff;
            border: none;
            padding: 5px 10px;
            font-size: 12px;
            border-radius: 5px;
            cursor: pointer;
            position: absolute;
            top: 10px;
            right: 10px;
        }
        .remove-btn:hover {
            background-color: #d9363e;
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
            <form action="<%= request.getContextPath() %>/removeFilmFromPlaylist" method="post">
                <input type="hidden" name="playlistId" value="<%= request.getAttribute("playlistId") %>">
                <input type="hidden" name="apiId" value="<%= film.get("apiId") %>">
                <button type="submit" style="background-color: #dc3545; color: #fff; border: none; padding: 5px 10px; border-radius: 5px; cursor: pointer;">Remove</button>
            </form>
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
