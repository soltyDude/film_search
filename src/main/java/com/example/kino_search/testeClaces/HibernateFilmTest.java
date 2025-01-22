package com.example.kino_search.testeClaces;

import com.example.kino_search.model.Film;
import com.example.kino_search.util.HibernateUtil;
import org.hibernate.Session;

import java.time.LocalDate;

public class HibernateFilmTest {
    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // Проверка на существование фильма с таким API ID
            int apiId = 12345; // Пример ID
            Film existingFilm = session.createQuery("from Film where apiId = :apiId", Film.class)
                    .setParameter("apiId", apiId)
                    .uniqueResult();

            if (existingFilm == null) {
                // Создание нового фильма
                Film film = new Film();
                film.setTitle("Inception");
                film.setReleaseDate(LocalDate.of(2010, 7, 16));
                film.setPosterUrl("https://example.com/inception.jpg");
                film.setApiId(apiId);
                film.setRuntime(148);
                film.setApiRating(8.8f);
                film.setRating(9.0f);
                film.setApiCount(1000000);
                film.setCount(500000);
                film.setOverview("A thief who steals corporate secrets through the use of dream-sharing technology.");

                session.save(film);
                System.out.println("Фильм добавлен: " + film.getTitle());
            } else {
                System.out.println("Фильм с таким API ID уже существует: " + existingFilm.getTitle());
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
