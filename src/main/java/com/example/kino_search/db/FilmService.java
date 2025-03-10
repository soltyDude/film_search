package com.example.kino_search.db;

import com.example.kino_search.db.dao.FilmDAO;
import com.example.kino_search.db.dao.GenreDAO;
import com.example.kino_search.db.dao.GenreFilmDAO;
import com.example.kino_search.model.Film;
import com.example.kino_search.model.FilmDTO;
import com.example.kino_search.model.FilmMapper;
import com.example.kino_search.util.HibernateUtil;
import com.example.kino_search.util.TMDBApiUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FilmService {

    private static final Logger logger = Logger.getLogger(FilmService.class.getName());
    private static volatile FilmService instance;

    private FilmService() {}

    public static FilmService getInstance() {
        if (instance == null) {
            synchronized (FilmService.class) {
                if (instance == null) {
                    instance = new FilmService();
                }
            }
        }
        return instance;
    }

    public Film fetchAndSaveFilm(int apiId) {
        logger.info("Fetching and saving film with API ID: " + apiId);

        Film film = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // Проверяем, существует ли фильм в базе
            film = session.createQuery("from Film where apiId = :apiId", Film.class)
                    .setParameter("apiId", apiId)
                    .uniqueResult();

            // Запрашиваем данные из TMDB API
            if (film == null) {
                film = new Film();
            }

            JsonObject movieDetails = TMDBApiUtil.sendRequest("/movie/" + apiId);
            film.setApiId(apiId);
            film.setTitle(movieDetails.get("title").getAsString());
            film.setReleaseDate(LocalDate.parse(movieDetails.get("release_date").getAsString()));
            film.setPosterUrl("https://image.tmdb.org/t/p/w500" + movieDetails.get("poster_path").getAsString());
            film.setRuntime(movieDetails.get("runtime").getAsInt());
            film.setApiRating(movieDetails.get("vote_average").getAsFloat());
            film.setApiCount(movieDetails.get("vote_count").getAsInt());
            film.setOverview(movieDetails.get("overview").getAsString());

            session.saveOrUpdate(film);


            JsonArray genres = movieDetails.getAsJsonArray("genres");
            for (int i = 0; i < genres.size(); i++) {
                JsonObject genreObj = genres.get(i).getAsJsonObject();
                String genreName = genreObj.get("name").getAsString();
                int genreId = GenreDAO.getInstance().saveOrGetGenreId(genreName);
                GenreFilmDAO.getInstance().saveGenreFilm(genreId, film.getId());
            }

            transaction.commit();
            logger.info("Film and genres saved successfully: " + film.getTitle());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while saving film with API ID: " + apiId, e);
        }
        return film;
    }

    public String getFilmTitleByID(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Film film = session.get(Film.class, id);
            return film != null ? film.getTitle() : null;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching film title by ID: " + id, e);
            return null;
        }
    }

    public Integer getFilmIdByApiId(int apiId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Film film = session.createQuery("from Film where apiId = :apiId", Film.class)
                    .setParameter("apiId", apiId)
                    .uniqueResult();
            return film != null ? film.getId() : null;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching film ID by API ID: " + apiId, e);
            return null;
        }
    }

    public Map<String, Object> getFilmDetailsById(int filmId) {
        Map<String, Object> movieDetails = new HashMap<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Film film = session.get(Film.class, filmId);
            if (film != null) {
                movieDetails.put("title", film.getTitle());
                movieDetails.put("overview", film.getOverview());
                movieDetails.put("release_date", film.getReleaseDate());
                movieDetails.put("poster_url", film.getPosterUrl());
                movieDetails.put("api_rating", film.getApiRating());
                movieDetails.put("rating", film.getRating());
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching film details by ID: " + filmId, e);
        }
        return movieDetails;
    }

    public boolean updateFilmRatingAndCount(int filmId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // Получаем все отзывы для фильма
            List<Integer> ratings = session.createQuery(
                            "select r.rating from Review r where r.filmId = :filmId", Integer.class)
                    .setParameter("filmId", filmId)
                    .getResultList();

            // Рассчитываем новый рейтинг и количество отзывов
            double avgRating = 0.0;
            int count = ratings.size();

            if (count > 0) {
                avgRating = ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
            }

            // Округляем рейтинг до одного знака после запятой
            avgRating = Math.round(avgRating * 10) / 10.0;

            // Обновляем фильм
            Film film = session.get(Film.class, filmId);
            if (film == null) {
                logger.warning("Film not found for ID: " + filmId);
                return false;
            }

            film.setRating(count > 0 ? avgRating : null); // Если отзывов нет, рейтинг остаётся null
            film.setCount(count);

            session.update(film);
            transaction.commit();

            logger.info("Updated film rating and count: filmId=" + filmId + ", newRating=" + avgRating + ", count=" + count);
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating film rating and count for film ID: " + filmId, e);
            return false;
        }
    }


    public FilmDTO getFilmDTOById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Film film = session.get(Film.class, id);
            if (film == null) {
                throw new IllegalArgumentException("Film not found with ID: " + id);
            }
            return FilmMapper.toDTO(film);
        }
    }

    public List<Film> getPaginatedFilms(int page, int size) {
        return FilmDAO.getInstance().getFilmsPaginated(page, size);
    }

}
