package com.example.kino_search.db.tmdb;


import com.example.kino_search.db.tmdb.TMDBUpdater;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class StartupListener implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Создаем планировщик
        scheduler = Executors.newSingleThreadScheduledExecutor();
        // Запускаем updateFilms() раз в сутки
        // Можно менять период на свое усмотрениt
        //
        scheduler.scheduleAtFixedRate(() -> {
            TMDBUpdater.updateFilms();
        }, 0, 24, TimeUnit.HOURS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
