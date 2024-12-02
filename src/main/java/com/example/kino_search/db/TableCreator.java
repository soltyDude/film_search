package com.example.kino_search.db;

import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableCreator {

    private static final Logger logger = Logger.getLogger(TableCreator.class.getName());

    public static void createFilmTable() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS film (
                id SERIAL PRIMARY KEY,
                title VARCHAR(60) NOT NULL,
                release_date DATE,
                poster_url TEXT,
                api_id INT UNIQUE NOT NULL,
                runtime INT,
                api_rating FLOAT,
                rating FLOAT,
                api_count INT,
                count INT,
                overvuw TEXT
            )
        """;

        try (Connection connection = ConectionManager.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createTableSQL);
            logger.info("Table 'film' created successfully or already exists.");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating table 'film'", e);
        }
    }

    public static void main(String[] args) {
        FilmService.fetchAndSaveFilm(550);
    }
}
