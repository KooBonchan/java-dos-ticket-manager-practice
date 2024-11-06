USE kbc;

DROP VIEW IF EXISTS reservation_list;

DROP TABLE IF EXISTS reservation;

DROP TABLE IF EXISTS movie;

CREATE TABLE movie (
  id integer NOT NULL,
    title varchar(20) NOT NULL,
    genre char(10) NOT NULL,
    start_date date NULL,
    end_date date NULL,
    CONSTRAINT pk_movie PRIMARY KEY (id)
);

CREATE TABLE reservation (
  id bigint NOT NULL,
    movie_id integer NOT NULL,
    seat_row integer NOT NULL,
    seat_col integer NOT NULL,
    reserve_timestamp Timestamp NOT NULL DEFAULT current_timestamp,
    CONSTRAINT pk_reservation PRIMARY KEY (id),
    CONSTRAINT fk_reservation_movie
    FOREIGN KEY (movie_id)
        REFERENCES movie(id)
);

CREATE INDEX idx_movie_title ON
movie(title);

CREATE INDEX idx_movie_end_date ON
movie(end_date);

CREATE INDEX idx_rsv_timestamp ON
reservation(reserve_timestamp);

CREATE VIEW reservation_list
AS
SELECT
  r.id,
  m.title,
  r.seat_row,
  r.seat_col,
  r.reserve_timestamp
FROM
  reservation r
INNER JOIN
movie AS m
ON
  m.id = r.movie_id;


-- -----------
INSERT INTO movie
(id, title, genre)
VALUES
(1626123456, 'Avengers', 'Fantasy'),
(1627234567, 'Conjuring', 'Horror'),
(1626654321, 'Love Actually', 'Romance'),
(1627175707, 'Classical Thump', 'Bass'),
(1627175746, '7T\'s', 'Bass'),
(1627175983, 'Stomping Ground', 'Bass');

INSERT INTO reservation
(id, movie_id, seat_row, seat_col, reserve_timestamp)
VALUES
(33011317445647, 1627175707, 4, 7, current_timestamp),
(33011319245647, 1627175983, 2, 5, current_date);

SELECT * FROM movie;

SELECT * FROM reservation_list;