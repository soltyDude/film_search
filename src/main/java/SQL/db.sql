-- Table: film
CREATE TABLE film (
                      id SERIAL PRIMARY KEY,
                      title VARCHAR(60) NOT NULL,
                      release_date DATE NOT NULL,
                      poster_url TEXT NOT NULL,
                      api_id INT NOT NULL UNIQUE,
                      runtime INT NOT NULL,
                      api_rating NUMERIC(3, 2) NOT NULL,
                      rating NUMERIC(3, 2),
                      api_count INT NOT NULL,
                      count INT DEFAULT 0,
                      overview TEXT
);

-- Table: "user"
CREATE TABLE users (
                        id SERIAL PRIMARY KEY,
                        nickname VARCHAR(225) NOT NULL UNIQUE,
                        email VARCHAR(225) NOT NULL UNIQUE,
                        password VARCHAR(225) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Table: director
CREATE TABLE director (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(40) NOT NULL UNIQUE
);

-- Table: director_film
CREATE TABLE director_film (
                               id SERIAL PRIMARY KEY,
                               film_id INT NOT NULL,
                               director_id INT NOT NULL,
                               CONSTRAINT fk_director_film FOREIGN KEY (film_id) REFERENCES film (id) ON DELETE CASCADE,
                               CONSTRAINT fk_director FOREIGN KEY (director_id) REFERENCES director (id) ON DELETE CASCADE
);

-- Table: genre
CREATE TABLE genre (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(20) NOT NULL UNIQUE
);

-- Table: genre_film
CREATE TABLE genre_film (
                            id SERIAL PRIMARY KEY,
                            genre_id INT NOT NULL,
                            film_id INT NOT NULL,
                            CONSTRAINT fk_genre FOREIGN KEY (genre_id) REFERENCES genre (id) ON DELETE CASCADE,
                            CONSTRAINT fk_genre_film FOREIGN KEY (film_id) REFERENCES film (id) ON DELETE CASCADE
);

-- Table: playlist
CREATE TABLE playlist (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(60) NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          user_id INT NOT NULL,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          CONSTRAINT fk_playlist_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Table: playlist_film
CREATE TABLE playlist_film (
                               id SERIAL PRIMARY KEY,
                               playlist_id INT NOT NULL,
                               film_id INT NOT NULL,
                               CONSTRAINT fk_playlist FOREIGN KEY (playlist_id) REFERENCES playlist (id) ON DELETE CASCADE,
                               CONSTRAINT fk_playlist_film FOREIGN KEY (film_id) REFERENCES film (id) ON DELETE CASCADE
);

-- Table: reviews
CREATE TABLE reviews (
                         id SERIAL PRIMARY KEY,
                         rating INT NOT NULL,
                         review_text TEXT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         user_id INT NOT NULL,
                         film_id INT NOT NULL,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
                         CONSTRAINT fk_review_film FOREIGN KEY (film_id) REFERENCES film (id) ON DELETE CASCADE
);

-- Table: role
CREATE TABLE role (
                      id SERIAL PRIMARY KEY,
                      role VARCHAR(20) NOT NULL UNIQUE
);

-- Table: user_role
CREATE TABLE user_role (
                           id SERIAL PRIMARY KEY,
                           role_id INT NOT NULL,
                           user_id INT NOT NULL,
                           CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE,
                           CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Table: viewed_movies
CREATE TABLE viewed_movies (
                               id SERIAL PRIMARY KEY,
                               viewed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                               reviews_id INT NOT NULL,
                               user_id INT NOT NULL,
                               film_id INT NOT NULL,
                               CONSTRAINT fk_viewed_movie_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
                               CONSTRAINT fk_viewed_movie_film FOREIGN KEY (film_id) REFERENCES film (id) ON DELETE CASCADE,
                               CONSTRAINT fk_viewed_movie_review FOREIGN KEY (reviews_id) REFERENCES reviews (id) ON DELETE CASCADE
);
