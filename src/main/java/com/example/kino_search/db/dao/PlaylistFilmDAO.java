package com.example.kino_search.db.dao;

import com.example.kino_search.db.FilmService;
import com.example.kino_search.model.Film;
import com.example.kino_search.model.Playlist;
import com.example.kino_search.model.PlaylistFilm;
import com.example.kino_search.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PlaylistFilmDAO {

    private static final Logger logger = Logger.getLogger(PlaylistFilmDAO.class.getName());
    private static volatile PlaylistFilmDAO instance;

    private PlaylistFilmDAO() {}

    public static PlaylistFilmDAO getInstance() {
        if (instance == null) {
            synchronized (PlaylistFilmDAO.class) {
                if (instance == null) {
                    instance = new PlaylistFilmDAO();
                }
            }
        }
        return instance;
    }

    public boolean addFilmToPlaylist(int playlistId, int filmId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // Получаем плейлист и фильм за один запрос
            Playlist playlist = session.get(Playlist.class, playlistId);
            if (playlist == null) {
                logger.warning("Playlist with ID " + playlistId + " does not exist.");
                return false;
            }

            Film film = session.get(Film.class, filmId);
            if (film == null) {
                logger.warning("Film with ID " + filmId + " does not exist.");
                return false;
            }

            // Проверяем существование связи
            boolean relationExists = session.createQuery(
                            "SELECT count(pf) FROM PlaylistFilm pf WHERE pf.playlist.id = :playlistId AND pf.film.id = :filmId", Long.class)
                    .setParameter("playlistId", playlistId)
                    .setParameter("filmId", filmId)
                    .uniqueResult() > 0;

            if (relationExists) {
                logger.info("Film already exists in playlist: Playlist ID = " + playlistId + ", Film ID = " + filmId);
                return false;
            }

            // Создаем связь и сохраняем
            PlaylistFilm playlistFilm = new PlaylistFilm();
            playlistFilm.setPlaylist(playlist);
            playlistFilm.setFilm(film);

            session.save(playlistFilm);
            transaction.commit();

            logger.info("Film added to playlist: Playlist ID = " + playlistId + ", Film ID = " + filmId);
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error adding film to playlist", e);
            return false;
        }
    }

    public boolean removeFilmFromPlaylist(int playlistId, int filmId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            int rowsDeleted = session.createQuery(
                            "DELETE FROM PlaylistFilm WHERE playlist.id = :playlistId AND film.id = :filmId")
                    .setParameter("playlistId", playlistId)
                    .setParameter("filmId", filmId)
                    .executeUpdate();

            transaction.commit();

            if (rowsDeleted > 0) {
                logger.info("Film removed from playlist: Playlist ID = " + playlistId + ", Film ID = " + filmId);
                return true;
            } else {
                logger.info("No film found to remove from playlist: Playlist ID = " + playlistId + ", Film ID = " + filmId);
                return false;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error removing film from playlist", e);
            return false;
        }
    }

}
