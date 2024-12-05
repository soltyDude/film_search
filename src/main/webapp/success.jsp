<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Success</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #d4edda;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 600px;
            margin: 50px auto;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
            border: 1px solid #c3e6cb;
            padding: 20px;
        }
        .success-header {
            color: #155724;
            font-size: 24px;
            text-align: center;
            margin-bottom: 10px;
        }
        .success-message {
            color: #155724;
            font-size: 18px;
            text-align: center;
        }
        .back-link {
            text-align: center;
            margin-top: 20px;
        }
        .back-link a {
            color: #155724;
            text-decoration: none;
            font-weight: bold;
            padding: 10px 20px;
            border: 2px solid #155724;
            border-radius: 5px;
        }
        .back-link a:hover {
            background-color: #155724;
            color: #fff;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="success-header">Action Successful!</div>
    <div class="success-message">
        <p>Film "<%= request.getAttribute("filmTitle") %>" was successfully added to the playlist "<%= request.getAttribute("playlistName") %>".</p>
    </div>
    <div class="back-link">
        <a href="search.jsp">Go back to Search</a>
    </div>
</div>
</body>
</html>
