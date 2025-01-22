package com.example.kino_search.db.dao;

import com.example.kino_search.model.Film;
import com.example.kino_search.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FilmDAO {

    private static volatile FilmDAO instance;

    private static final Logger logger = Logger.getLogger(FilmDAO.class.getName());

    private FilmDAO() {}

    public static FilmDAO getInstance() {
        if (instance == null) {
            synchronized (FilmDAO.class) {
                if (instance == null) {
                    instance = new FilmDAO();
                }
            }
        }
        return instance;
    }

    public void saveOrUpdateFilm(Film film) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // Проверяем существование объекта
            Film existingFilm = session.createQuery(
                            "FROM Film WHERE apiId = :apiId", Film.class)
                    .setParameter("apiId", film.getApiId())
                    .uniqueResult();

            if (existingFilm != null) {
                film.setId(existingFilm.getId()); // Устанавливаем ID существующего объекта
                session.merge(film);
            } else {
                session.save(film); // Новый объект
            }

            transaction.commit();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error saving or updating film: " + film.getTitle(), e);
        }
    }




    public Film getFilmByApiId(int apiId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Film where apiId = :apiId", Film.class)
                    .setParameter("apiId", apiId)
                    .uniqueResult();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving film by API ID: " + apiId, e);
            return null;
        }
    }

    public Film getFilmById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Film.class, id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving film by ID: " + id, e);
            return null;
        }
    }

    public Film getRandomFilm() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Film> films = session.createQuery("from Film", Film.class).list();
            if (films != null && !films.isEmpty()) {
                Random random = new Random();
                return films.get(random.nextInt(films.size()));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving random film", e);
        }
        return null;
    }
}
