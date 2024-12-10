-- Table: film
CREATE TABLE film (
                      id SERIAL PRIMARY KEY, -- Автоинкрементный идентификатор
                      title VARCHAR(300) NOT NULL,
                      release_date DATE NOT NULL,
                      poster_url TEXT NOT NULL,
                      api_id INT NOT NULL UNIQUE, -- Уникальное ограничение для API ID
                      runtime INT NOT NULL,
                      api_rating NUMERIC(3, 1) NOT NULL,
                      rating NUMERIC(3, 1),
                      api_count INT NOT NULL,
                      count INT DEFAULT 0,
                      overview TEXT
);

-- Table: users
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
                          name VARCHAR(40) NOT NULL UNIQUE -- Уникальное имя режиссера
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
                       name VARCHAR(20) NOT NULL UNIQUE -- Уникальное имя жанра
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
                               CONSTRAINT fk_playlist_film FOREIGN KEY (film_id) REFERENCES film (id) ON DELETE CASCADE,
                               CONSTRAINT unique_playlist_film UNIQUE (playlist_id, film_id) -- Уникальное ограничение
);

-- Table: reviews
CREATE TABLE reviews (
                         id SERIAL PRIMARY KEY, -- Уникальный идентификатор отзыва
                         rating INT CHECK (rating BETWEEN 0 AND 11), -- Оценка от 1 до 10
                         review_text TEXT, -- Текст отзыва
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Дата создания отзыва
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Дата обновления отзыва
                         user_id INT NOT NULL, -- Идентификатор пользователя
                         film_id INT NOT NULL, -- Идентификатор фильма
                         CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE, -- Внешний ключ на таблицу пользователей
                         CONSTRAINT fk_review_film FOREIGN KEY (film_id) REFERENCES film (id) ON DELETE CASCADE, -- Внешний ключ на таблицу фильмов
                         CONSTRAINT unique_user_film_review UNIQUE (user_id, film_id) -- Уникальное ограничение на пару user_id и film_id
);

-- Table: role
CREATE TABLE role (
                      id SERIAL PRIMARY KEY,
                      role VARCHAR(20) NOT NULL UNIQUE -- Уникальное название роли
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
                               viewed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Timestamp of when the movie was marked as viewed
                               reviews_id INT DEFAULT NULL, -- Optional reviews_id
                               user_id INT NOT NULL, -- ID of the user who viewed the movie
                               film_id INT NOT NULL, -- ID of the film that was viewed
                               CONSTRAINT fk_viewed_movie_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE, -- Foreign key to users
                               CONSTRAINT fk_viewed_movie_film FOREIGN KEY (film_id) REFERENCES film (id) ON DELETE CASCADE, -- Foreign key to films
                               CONSTRAINT fk_viewed_movie_review FOREIGN KEY (reviews_id) REFERENCES reviews (id) ON DELETE CASCADE, -- Foreign key to reviews
                               CONSTRAINT unique_user_film UNIQUE (user_id, film_id) -- Unique constraint to prevent duplicate entries for the same user and film
);


-- Table: type
CREATE TABLE type (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(20) NOT NULL UNIQUE -- Уникальное название типа
);

-- Table: play_list_type
CREATE TABLE play_list_type (
                                id SERIAL PRIMARY KEY,
                                type_id INT NOT NULL,
                                playlist_id INT NOT NULL,
                                CONSTRAINT fk_play_list_type_type FOREIGN KEY (type_id) REFERENCES type (id) ON DELETE CASCADE,
                                CONSTRAINT fk_play_list_type_playlist FOREIGN KEY (playlist_id) REFERENCES playlist (id) ON DELETE CASCADE
);

