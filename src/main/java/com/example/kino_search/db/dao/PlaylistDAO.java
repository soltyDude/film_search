package com.example.kino_search.db.dao;

import com.example.kino_search.model.Playlist;
import com.example.kino_search.model.PlaylistFilm;
import com.example.kino_search.util.HibernateUtil;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlaylistDAO {

    private static final Logger logger = Logger.getLogger(PlaylistDAO.class.getName());
    private static volatile PlaylistDAO instance;

    private PlaylistDAO() {}

    public static PlaylistDAO getInstance() {
        if (instance == null) {
            synchronized (PlaylistDAO.class) {
                if (instance == null) {
                    instance = new PlaylistDAO();
                }
            }
        }
        return instance;
    }

    public List<Playlist> getPlaylistsByUserId(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Playlist> query = session.createQuery("FROM Playlist WHERE userId = :userId", Playlist.class);
            query.setParameter("userId", userId);
            return query.list();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving playlists for user ID: " + userId, e);
            return List.of();
        }
    }

    public Optional<Playlist> getPlaylistById(int playlistId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Playlist.class, playlistId));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving playlist by ID: " + playlistId, e);
            return Optional.empty();
        }
    }

    public boolean createPlaylist(int userId, String playlistName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Playlist playlist = new Playlist();
            playlist.setUserId(userId);
            playlist.setName(playlistName);
            playlist.setCreatedAt(LocalDateTime.now());
            playlist.setUpdatedAt(LocalDateTime.now());

            session.save(playlist);
            transaction.commit();

            logger.info("Playlist created: " + playlistName);
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating playlist: " + playlistName, e);
            return false;
        }
    }

    public String getPlaylistNameById(int playlistId) {
        return getPlaylistById(playlistId).map(Playlist::getName).orElse(null);
    }

    public boolean deletePlaylist(int playlistId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Playlist playlist = session.get(Playlist.class, playlistId);
            if (playlist != null) {
                session.delete(playlist);
                transaction.commit();
                logger.info("Playlist deleted: ID = " + playlistId);
                return true;
            } else {
                logger.warning("Playlist not found for deletion: ID = " + playlistId);
                return false;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error deleting playlist: ID = " + playlistId, e);
            return false;
        }
    }

    public int getWantToWatchPlaylistId(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Integer> query = session.createQuery(
                    "SELECT id FROM Playlist WHERE userId = :userId AND name = 'Want to Watch'", Integer.class);
            query.setParameter("userId", userId);

            Integer playlistId = query.uniqueResult();
            if (playlistId != null) {
                logger.info("Found 'Want to Watch' playlist ID: " + playlistId + " for user ID: " + userId);
                return playlistId;
            } else {
                logger.warning("No 'Want to Watch' playlist found for user ID: " + userId);
                return -1;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving 'Want to Watch' playlist ID for user ID: " + userId, e);
            return -1;
        }
    }

    public Playlist getPlaylistDetails(int playlistId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Playlist> query = cb.createQuery(Playlist.class);

            Root<Playlist> playlistRoot = query.from(Playlist.class);
            Fetch<Playlist, PlaylistFilm> playlistFilmsFetch = playlistRoot.fetch("playlistFilms", JoinType.LEFT);
            playlistFilmsFetch.fetch("film", JoinType.LEFT);

            query.select(playlistRoot)
                    .where(cb.equal(playlistRoot.get("id"), playlistId));

            // Hardcoded sorting by "title" of the associated films
            query.orderBy(cb.asc(playlistRoot.join("playlistFilms").join("film").get("title")));

            return session.createQuery(query).uniqueResult();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving playlist details for ID: " + playlistId, e);
            return null;
        }
    }

    public List<Playlist> getPlaylistsByUserIdPaginated(int userId, int page, int size) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Playlist> query = cb.createQuery(Playlist.class);
            Root<Playlist> root = query.from(Playlist.class);
            query.select(root).where(cb.equal(root.get("userId"), userId));

            return session.createQuery(query)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching paginated playlists", e);
            return List.of();
        }
    }






}
