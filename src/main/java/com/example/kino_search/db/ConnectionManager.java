package com.example.kino_search.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String DB_URL = "jdbc:postgresql://db:5432/mydb";
    private static final String DB_USER = "myuser";
    private static final String DB_PASSWORD = "mypassword";

    static {
        try {
            // Явная загрузка драйвера (опционально)
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // Логирование или обработка ошибки
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}

