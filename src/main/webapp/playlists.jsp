<%@ page session="true" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Your Playlists</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f9f9f9;
        }
        .container {
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
        }
        h1 {
            text-align: center;
            color: #333;
        }
        .playlist {
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 8px;
            margin-bottom: 15px;
            background-color: #fefefe;
        }
        .playlist h2 {
            margin: 0;
            font-size: 20px;
        }
        .playlist p {
            color: #555;
            margin: 5px 0;
        }
        .view-btn {
            display: inline-block;
            padding: 10px 20px;
            margin-top: 10px;
            color: #fff;
            background-color: #007BFF;
            text-decoration: none;
            border-radius: 5px;
        }
        .view-btn:hover {
            background-color: #0056b3;
        }
        .create-link {
            display: block;
            text-align: center;
            margin: 20px 0;
            color: #007BFF;
            text-decoration: none;
            font-size: 18px;
        }
        .create-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Your Playlists</h1>

    <%
        List<Map<String, Object>> playlists = (List<Map<String, Object>>) request.getAttribute("playlists");
        if (playlists == null || playlists.isEmpty()) {
    %>
    <p>You don't have any playlists yet. <a href="createPlaylist.jsp" class="create-link">Create one!</a></p>
    <%
    } else {
        for (Map<String, Object> playlist : playlists) {
    %>
    <div class="playlist">
        <h2><%= playlist.get("name") %></h2>
        <p><strong>Created:</strong> <%= playlist.get("created_at") %></p>
        <p><strong>Updated:</strong> <%= playlist.get("updated_at") %></p>
        <a href="viewPlaylist?id=<%= playlist.get("id") %>" class="view-btn">View Playlist</a>
    </div>
    <%
            }
        }
    %>
</div>
</body>
</html>
