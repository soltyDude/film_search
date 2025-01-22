package com.example.kino_search.testeClaces;

import com.example.kino_search.model.Genre;
import com.example.kino_search.util.HibernateUtil;
import org.hibernate.Session;

public class HibernateTest {
    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // Create a new genre
            Genre genre = new Genre();
            genre.setName("Actionpp");

            // Save the genre
            session.save(genre);

            session.getTransaction().commit();
            System.out.println("Hibernate connected and genre saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
