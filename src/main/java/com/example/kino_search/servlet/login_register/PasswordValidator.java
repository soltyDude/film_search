package com.example.kino_search.servlet.login_register;

public class PasswordValidator {

    public static boolean validate(String password) {
        // Проверка длины
        if (password.length() < 8) {
            return false;
        }

        // Проверка наличия цифры
        if (!password.matches(".*\\d.*")) {
            return false;
        }

        // Проверка наличия заглавной буквы
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }

        return true;
    }
}
