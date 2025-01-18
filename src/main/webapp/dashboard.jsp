<%@ page session="true" %>
<%@ page import="com.example.kino_search.util.LanguageManager" %>
<%@ page import="java.util.Calendar" %>
<!DOCTYPE html>
<html lang="<%= session.getAttribute("lang") != null ? session.getAttribute("lang") : "en" %>">
<head>
    <meta charset="UTF-8">
    <title><%= LanguageManager.getMessage("dashboard.title") %></title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f0f2f5;
            color: #333;
        }
        header {
            background-color: #007bff;
            color: #fff;
            padding: 20px;
            text-align: center;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
        }
        header h1 {
            margin: 0;
            font-size: 24px;
        }
        nav {
            background-color: #fff;
            padding: 10px 20px;
            display: flex;
            justify-content: center;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        nav a {
            margin: 0 15px;
            text-decoration: none;
            color: #007bff;
            font-size: 16px;
            transition: color 0.3s ease;
        }
        nav a:hover {
            color: #0056b3;
        }
        .container {
            max-width: 900px;
            margin: 40px auto;
            padding: 20px;
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }
        h2 {
            color: #333;
            font-size: 22px;
            text-align: center;
        }
        p {
            font-size: 16px;
            line-height: 1.6;
            margin: 10px 0;
        }
        ul {
            list-style: none;
            padding: 0;
        }
        ul li {
            margin: 10px 0;
            padding-left: 20px;
            position: relative;
        }
        ul li::before {
            content: "\2022";
            color: #007bff;
            font-weight: bold;
            display: inline-block;
            width: 10px;
            margin-left: -20px;
        }
        button {
            display: block;
            margin: 20px auto;
            padding: 10px 20px;
            font-size: 16px;
            color: #fff;
            background-color: #28a745;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        button:hover {
            background-color: #218838;
        }
        #roulette {
            display: none;
            text-align: center;
            margin: 20px 0;
        }
        #roulette img {
            width: 100px;
        }
        #randomResult {
            display: none;
            text-align: center;
            font-size: 18px;
            margin: 20px auto;
            padding: 20px;
            background-color: #e9ecef;
            border-radius: 10px;
        }
        footer {
            margin-top: 40px;
            text-align: center;
            font-size: 14px;
            color: #888;
        }
    </style>
</head>
<body>
<header>
    <h1><%= LanguageManager.getMessage("dashboard.welcome") %></h1>
</header>

<nav>
    <a href="search.jsp"><%= LanguageManager.getMessage("dashboard.search") %></a>
    <a href="playlists"><%= LanguageManager.getMessage("dashboard.playlists") %></a>
    <a href="whatched"><%= LanguageManager.getMessage("dashboard.watched") %></a>
    <a href="recommendations"><%= LanguageManager.getMessage("dashboard.recommendations") %></a>
    <a href="popular"><%= LanguageManager.getMessage("dashboard.popular") %></a>
    <a href="logout"><%= LanguageManager.getMessage("dashboard.logout") %></a>
</nav>

<div class="container">
    <% String userEmail = (String) session.getAttribute("user"); %>
    <% if (userEmail == null) { %>
    <h2><%= LanguageManager.getMessage("dashboard.not_logged_in") %></h2>
    <p><a href="login.jsp"><%= LanguageManager.getMessage("dashboard.log_in_here") %></a>.</p>
    <% } else { %>
    <h2><%= LanguageManager.getMessage("dashboard.hello").replace("{0}", userEmail) %></h2>
    <p><strong><%= LanguageManager.getMessage("dashboard.explore") %></strong></p>
    <!-- Остальные элементы списка остаются без изменений -->
    <% } %>
</div>

<button id="randomBtn"><%= LanguageManager.getMessage("dashboard.surprise") %></button>

<div id="roulette">
    <img src="resources/images/spinner.gif" alt="Loading...">
</div>

<div id="randomResult"></div>

<div>
    <a href="?lang=en">English</a> | <a href="?lang=ru">Русский</a>
</div>
<footer>
    &copy; <%= Calendar.getInstance().get(Calendar.YEAR) %> MovieFinder. All Rights Reserved.
</footer>

<script>
    document.getElementById('randomBtn').addEventListener('click', function() {
        document.getElementById('roulette').style.display = 'block';
        fetch('<%= request.getContextPath() %>/randomFilm')
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.text();
            })
            .then(html => {
                document.getElementById('roulette').style.display = 'none';
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

<div>
    <a href="?lang=en">English</a> | <a href="?lang=ru">Русский</a>
</div>
</body>
</html>
