<%@ page session="true" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Dashboard</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f9f9f9;
        }
        header {
            background-color: #333;
            color: #fff;
            padding: 10px;
            text-align: center;
        }
        nav {
            margin: 20px;
            text-align: center;
        }
        nav a {
            margin: 0 10px;
            text-decoration: none;
            color: #007bff;
        }
        nav a:hover {
            text-decoration: underline;
        }
        .container {
            margin: 20px auto;
            max-width: 800px;
            padding: 20px;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
        }
    </style>
</head>
<body>
<header>
    <h1>Welcome to Your Dashboard</h1>
</header>

<nav>
    <a href="search.jsp">Search Movies</a>
    <a href="playlists">Playlists</a>
    <a href="whatched">Watched</a>
    <a href="recommendations">Recommendations</a> <!-- Новая ссылка -->
    <a href="popular">Popular Movies</a> <!-- Новая ссылка -->
    <a href="logout">Logout</a>
</nav>


<div class="container">
    <%
        String userEmail = (String) session.getAttribute("user");
        if (userEmail == null) {
    %>
    <h2>You are not logged in.</h2>
    <p><a href="login.jsp">Log in here</a>.</p>
    <%
    } else {
    %>
    <h2>Hello, <%= userEmail %>!</h2>
    <p>Explore movies, manage your watchlist, and share your reviews.</p>
    <ul>
        <li><strong>Search Movies:</strong> Use the search feature to find information about your favorite films.</li>
        <li><strong>Watchlist:</strong> Organize the movies you want to watch and track those you've already seen.</li>
        <li><strong>Reviews:</strong> Share your thoughts on movies and see what others think.</li>
    </ul>
    <%
        }
    %>
</div>
<button id="randomBtn">Surprise Me!</button>

<div id="roulette" style="display:none;">
    <!-- Здесь может быть ваша анимация: например, анимированный GIF или CSS-анимация -->
    <img src="resources/images/spinner.gif" alt="spinner">
</div>

<div id="randomResult" style="display:none;"></div>

<script>
    document.getElementById('randomBtn').addEventListener('click', function() {
        // Показываем рулетку (spinner)
        document.getElementById('roulette').style.display = 'block';

        // Пример использования fetch API для AJAX-запроса
        fetch('<%= request.getContextPath() %>/randomFilm')
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.text();
            })
            .then(html => {
                // Прячем рулетку
                document.getElementById('roulette').style.display = 'none';
                // Показываем результат
                document.getElementById('randomResult').style.display = 'block';
                document.getElementById('randomResult').innerHTML = html;
            })
            .catch(error => {
                console.error('Error fetching random film:', error);
                document.getElementById('roulette').style.display = 'none';
                document.getElementById('randomResult').innerHTML = '<p>Sorry, something went wrong.</p>';
                document.getElementById('randomResult').style.display = 'block';
            });
    });
</script>

</body>
</html>
