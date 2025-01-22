package com.example.kino_search.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();

            // Hibernate settings
            configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
            configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/moviefinder");
            configuration.setProperty("hibernate.connection.username", "postgres");
            configuration.setProperty("hibernate.connection.password", "qwerty1234");
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.format_sql", "true");
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");

            // Adding annotated classes
            configuration.addAnnotatedClass(com.example.kino_search.model.Genre.class);
            configuration.addAnnotatedClass(com.example.kino_search.model.Film.class);

            System.out.println("--------------Added entity: " + com.example.kino_search.model.Genre.class.getName());

            // Create SessionFactory
            sessionFactory = configuration.buildSessionFactory();

            // Log annotated entities
            sessionFactory.getMetamodel().getEntities().forEach(entityType -> {
                System.out.println("-------start");
                System.out.println("Annotated entity: " + entityType.getJavaType().getName());
                System.out.println("-------end");
            });

        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("Initial SessionFactory creation failed: " + ex.getMessage());
        }

        System.out.println("--------------connection goooooood");

    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
