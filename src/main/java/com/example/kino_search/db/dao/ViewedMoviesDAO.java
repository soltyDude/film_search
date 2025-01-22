package com.example.kino_search.db.dao;

import com.example.kino_search.model.ViewedMovie;
import com.example.kino_search.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewedMoviesDAO {

    private static final Logger logger = Logger.getLogger(ViewedMoviesDAO.class.getName());
    private static volatile ViewedMoviesDAO instance;

    private ViewedMoviesDAO() {}

    public static ViewedMoviesDAO getInstance() {
        if (instance == null) {
            synchronized (ViewedMoviesDAO.class) {
                if (instance == null) {
                    instance = new ViewedMoviesDAO();
                }
            }
        }
        return instance;
    }

    // Добавление фильма в просмотренные
    public boolean addMovieToViewed(int userId, int filmId, Integer reviewId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            ViewedMovie viewedMovie = new ViewedMovie();
            viewedMovie.setUserId(userId);
            viewedMovie.setFilmId(filmId);
            viewedMovie.setReviewId(reviewId);
            viewedMovie.setViewedAt(LocalDateTime.now()); // Установка текущего времени

            session.saveOrUpdate(viewedMovie);

            transaction.commit();
            logger.info("Movie added to viewed_movies: User ID = " + userId + ", Film ID = " + filmId);
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error adding movie to viewed_movies", e);
            return false;
        }
    }

    // Удаление фильма из просмотренных
    public boolean removeMovieFromViewed(int userId, int filmId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Query<?> query = session.createQuery("DELETE FROM ViewedMovie WHERE userId = :userId AND filmId = :filmId");
            query.setParameter("userId", userId);
            query.setParameter("filmId", filmId);

            int rowsAffected = query.executeUpdate();
            transaction.commit();

            if (rowsAffected > 0) {
                logger.info("Movie removed from viewed_movies: User ID = " + userId + ", Film ID = " + filmId);
                return true;
            } else {
                logger.info("No movie found to remove in viewed_movies: User ID = " + userId + ", Film ID = " + filmId);
                return false;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error removing movie from viewed_movies", e);
            return false;
        }
    }

    // Проверка, существует ли фильм в просмотренных
    public boolean isMovieInViewed(int userId, int filmId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(v) FROM ViewedMovie v WHERE v.userId = :userId AND v.filmId = :filmId", Long.class);
            query.setParameter("userId", userId);
            query.setParameter("filmId", filmId);

            long count = query.uniqueResult();
            return count > 0;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error checking if movie exists in viewed_movies", e);
            return false;
        }
    }

    // Получение списка просмотренных фильмов для пользователя
    public List<ViewedMovie> getViewedMoviesByUserId(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ViewedMovie> query = session.createQuery(
                    "FROM ViewedMovie WHERE userId = :userId ORDER BY viewedAt DESC", ViewedMovie.class);
            query.setParameter("userId", userId);
            return query.list();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving viewed movies for user ID " + userId, e);
            return List.of();
        }
    }

    // Обновление записи о просмотренном фильме (например, добавление reviewId)
    public boolean updateViewedMovie(int userId, int filmId, Integer reviewId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Query<ViewedMovie> query = session.createQuery(
                    "FROM ViewedMovie WHERE userId = :userId AND filmId = :filmId", ViewedMovie.class);
            query.setParameter("userId", userId);
            query.setParameter("filmId", filmId);

            Optional<ViewedMovie> viewedMovieOpt = query.uniqueResultOptional();

            if (viewedMovieOpt.isPresent()) {
                ViewedMovie viewedMovie = viewedMovieOpt.get();
                viewedMovie.setReviewId(reviewId);
                session.update(viewedMovie);
                transaction.commit();
                logger.info("Viewed movie updated: User ID = " + userId + ", Film ID = " + filmId + ", Review ID = " + reviewId);
                return true;
            } else {
                logger.info("No viewed movie found to update: User ID = " + userId + ", Film ID = " + filmId);
                return false;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating viewed movie", e);
            return false;
        }
    }
}
