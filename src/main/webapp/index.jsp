<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Welcome to MovieFinder</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      height: 100vh;
      margin: 0;
      background-color: #f3f3f3;
    }
    .container {
      text-align: center;
      background: #fff;
      padding: 20px;
      border-radius: 8px;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
      width: 300px;
    }
    h1 {
      margin-bottom: 20px;
    }
    .buttons {
      margin-top: 20px;
    }
    .buttons button {
      margin: 10px;
      padding: 10px 20px;
      border: none;
      background: #007bff;
      color: white;
      border-radius: 4px;
      cursor: pointer;
    }
    .buttons button:hover {
      background: #0056b3;
    }
    .buttons a {
      color: white;
      text-decoration: none;
    }
  </style>
</head>
<body>
<div class="container">
  <h1>Welcome to MovieFinder!</h1>
  <p>Discover movies, create watchlists, and share reviews.</p>
  <div class="buttons">
    <button><a href="login.jsp">Login</a></button>
    <button><a href="register.jsp">Register</a></button>
  </div>
</div>
</body>
</html>
