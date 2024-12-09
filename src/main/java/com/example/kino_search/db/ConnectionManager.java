package com.example.kino_search.db;

import com.example.kino_search.property.PropertyManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {
    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(PropertyManager.getProperty("db.url"));
        config.setUsername(PropertyManager.getProperty("db.username"));
        config.setPassword(PropertyManager.getProperty("db.password"));
        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException, SQLException {
        return dataSource.getConnection();
    }
}
