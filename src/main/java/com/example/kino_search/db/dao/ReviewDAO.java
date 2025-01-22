package com.example.kino_search.db.dao;

import com.example.kino_search.model.Review;
import com.example.kino_search.model.ViewedMovie;
import com.example.kino_search.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReviewDAO {

    private static final Logger logger = Logger.getLogger(ReviewDAO.class.getName());
    private static volatile ReviewDAO instance;

    private ReviewDAO() {}

    public static ReviewDAO getInstance() {
        if (instance == null) {
            synchronized (ReviewDAO.class) {
                if (instance == null) {
                    instance = new ReviewDAO();
                }
            }
        }
        return instance;
    }

    public boolean addReview(int userId, int filmId, int rating, String reviewText) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // Создаем новый отзыв
            Review review = new Review();
            review.setUserId(userId);
            review.setFilmId(filmId);
            review.setRating(rating);
            review.setReviewText(reviewText);
            review.setCreatedAt(LocalDateTime.now());
            review.setUpdatedAt(LocalDateTime.now());

            session.save(review);

            // Обновляем таблицу `viewed_movies`
            ViewedMovie viewedMovie = session.createQuery(
                            "from ViewedMovie where userId = :userId and filmId = :filmId", ViewedMovie.class)
                    .setParameter("userId", userId)
                    .setParameter("filmId", filmId)
                    .uniqueResult();

            if (viewedMovie != null) {
                viewedMovie.setReviewId(review.getId());
                session.update(viewedMovie);
            }

            transaction.commit();
            logger.info("Review added successfully: User ID = " + userId + ", Film ID = " + filmId);
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error adding review", e);
            return false;
        }
    }

    public boolean isReviewExists(int userId, int filmId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                            "select count(r) from Review r where r.userId = :userId and r.filmId = :filmId", Long.class)
                    .setParameter("userId", userId)
                    .setParameter("filmId", filmId)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error checking if review exists", e);
            return false;
        }
    }

    public List<Review> getReviewsByFilmId(int filmId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Review where filmId = :filmId", Review.class)
                    .setParameter("filmId", filmId)
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching reviews by film ID", e);
            return null;
        }
    }

    public Review getReviewByUserAndFilm(int userId, int filmId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from Review where userId = :userId and filmId = :filmId", Review.class)
                    .setParameter("userId", userId)
                    .setParameter("filmId", filmId)
                    .uniqueResult();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching review by user and film", e);
            return null;
        }
    }

    public boolean updateReview(int userId, int filmId, int newRating, String newReviewText) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Review review = session.createQuery(
                            "from Review where userId = :userId and filmId = :filmId", Review.class)
                    .setParameter("userId", userId)
                    .setParameter("filmId", filmId)
                    .uniqueResult();

            if (review == null) {
                logger.warning("No review found for update: User ID = " + userId + ", Film ID = " + filmId);
                return false;
            }

            review.setRating(newRating);
            review.setReviewText(newReviewText);
            review.setUpdatedAt(LocalDateTime.now());
            session.update(review);

            transaction.commit();
            logger.info("Review updated successfully: User ID = " + userId + ", Film ID = " + filmId);
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating review", e);
            return false;
        }
    }
}
