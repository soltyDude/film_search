<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register</title>
    <script>
        function validatePassword() {
            const password = document.getElementById("password").value;
            const confirmPassword = document.getElementById("confirmPassword").value;
            const errorElement = document.getElementById("passwordError");

            // Проверка длины пароля
            if (password.length < 8) {
                errorElement.textContent = "Password must be at least 8 characters.";
                return false;
            }

            // Проверка наличия цифр
            if (!/\d/.test(password)) {
                errorElement.textContent = "Password must include at least one number.";
                return false;
            }

            // Проверка наличия заглавных букв
            if (!/[A-Z]/.test(password)) {
                errorElement.textContent = "Password must include at least one uppercase letter.";
                return false;
            }

            // Проверка совпадения паролей
            if (password !== confirmPassword) {
                errorElement.textContent = "Passwords do not match.";
                return false;
            }

            errorElement.textContent = "";
            return true;
        }
    </script>
</head>
<body>
<h2>User Registration</h2>
<form action="register" method="POST" onsubmit="return validatePassword();">
    <label for="nickname">Nickname:</label>
    <input type="text" id="nickname" name="nickname" required><br>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required><br>

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required><br>

    <label for="confirmPassword">Confirm Password:</label>
    <input type="password" id="confirmPassword" name="confirmPassword" required><br>

    <span id="passwordError" style="color: red;"></span><br>

    <!-- Добавляем текст с требованиями к паролю -->
    <div>
        <p><strong>Password Requirements:</strong></p>
        <ul>
            <li>At least 8 characters</li>
            <li>At least one number</li>
            <li>At least one uppercase letter</li>
        </ul>
    </div>

    <button type="submit">Register</button>
</form>
</body>
</html>
