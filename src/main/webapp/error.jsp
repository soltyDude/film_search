<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Error</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f8d7da;
      margin: 0;
      padding: 20px;
    }
    .container {
      max-width: 600px;
      margin: 50px auto;
      background: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
      border: 1px solid #f5c6cb;
      padding: 20px;
    }
    .error-header {
      color: #721c24;
      font-size: 24px;
      text-align: center;
      margin-bottom: 10px;
    }
    .error-message {
      color: #721c24;
      font-size: 18px;
      text-align: center;
    }
    .back-link {
      text-align: center;
      margin-top: 20px;
    }
    .back-link a {
      color: #721c24;
      text-decoration: none;
      font-weight: bold;
      padding: 10px 20px;
      border: 2px solid #721c24;
      border-radius: 5px;
    }
    .back-link a:hover {
      background-color: #721c24;
      color: #fff;
    }
  </style>
</head>
<body>
<div class="container">
  <div class="error-header">Oops! Something went wrong.</div>
  <div class="error-message">
    <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "An unexpected error occurred." %>
  </div>
  <div class="back-link">
    <a href="search.jsp">Go back to Search</a>
  </div>
</div>
</body>
</html>
