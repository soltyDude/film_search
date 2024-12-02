-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2024-12-02 13:51:17.231

-- foreign keys
ALTER TABLE Directir_Film
DROP CONSTRAINT Directir_Film_Director;

ALTER TABLE Directir_Film
DROP CONSTRAINT Directir_Film_Film;

ALTER TABLE genre_film
DROP CONSTRAINT genre_film_film ;

ALTER TABLE genre_film
DROP CONSTRAINT genre_film_genre;

ALTER TABLE playList_film
DROP CONSTRAINT play_list_film_film ;

ALTER TABLE playList_film
DROP CONSTRAINT play_list_film_play_list;

ALTER TABLE play_list_type
DROP CONSTRAINT play_list_type_play_list;

ALTER TABLE play_list_type
DROP CONSTRAINT play_list_type_type;

ALTER TABLE playList
DROP CONSTRAINT play_list_user;

ALTER TABLE reviews
DROP CONSTRAINT reviews_film ;

ALTER TABLE reviews
DROP CONSTRAINT reviews_user;

ALTER TABLE user_role
DROP CONSTRAINT user_role_role;

ALTER TABLE user_role
DROP CONSTRAINT user_role_user;

ALTER TABLE viewed_Movies
DROP CONSTRAINT viewed_Movies_Film;

ALTER TABLE viewed_Movies
DROP CONSTRAINT viewed_Movies_reviews;

ALTER TABLE viewed_Movies
DROP CONSTRAINT viewed_Movies_user;

-- tables
DROP TABLE Directir_Film;

DROP TABLE Director;

DROP TABLE Film;

DROP TABLE genre;

DROP TABLE genre_film;

DROP TABLE playList;

DROP TABLE playList_film;

DROP TABLE play_list_type;

DROP TABLE reviews;

DROP TABLE role;

DROP TABLE type;

DROP TABLE "user";

DROP TABLE user_role;

DROP TABLE viewed_Movies;

-- End of file.

