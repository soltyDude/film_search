package com.example.kino_search.db.dao;

import com.example.kino_search.model.Film;
import com.example.kino_search.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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

            // Проверяем существование объекта через JPA Criteria API
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Film> query = builder.createQuery(Film.class);
            Root<Film> root = query.from(Film.class);
            query.select(root).where(builder.equal(root.get("apiId"), film.getApiId()));

            Film existingFilm = session.createQuery(query).uniqueResult();

            if (existingFilm != null) {
                film.setId(existingFilm.getId()); // Устанавливаем ID существующего объекта
                session.merge(film);
            } else {
                session.save(film); // Новый объект
            }

            transaction.commit();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error saving or updating film: " + film.getTitle(), e);
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

    public List<Film> getFilmsPaginated(int page, int size) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Создаем CriteriaBuilder для построения запросов
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Film> query = cb.createQuery(Film.class);
            Root<Film> root = query.from(Film.class);

            // Строим запрос: выбор всех фильмов
            query.select(root);

            // Добавляем пагинацию
            return session.createQuery(query)
                    .setFirstResult((page - 1) * size) // Начало выборки
                    .setMaxResults(size) // Количество записей
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching paginated films", e);
            return List.of(); // Возвращаем пустой список в случае ошибки
        }
    }

}
