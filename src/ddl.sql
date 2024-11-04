use kbc;
drop view if exists reservation_list;
drop table if exists reservation;
drop table if exists movie;


create table movie (
	id integer not null,
    title varchar(20) not null,
    genre char(10) not null,
    start_date date null,
    end_date date null,
    constraint pk_movie primary key (id)
);

create table reservation (
	id bigint not null,
    movie_id integer not null,
    seat_row integer not null,
    seat_col integer not null,
    reserve_date Date not null,
    constraint pk_reservation primary key (id),
    constraint fk_reservation_movie
		foreign key (movie_id)
        references movie(id)
);

create index idx_movie_title on movie(title);
create index idx_movie_end_date on movie(end_date);
create index idx_rsv_date on reservation(reserve_date);

create view reservation_list
as
select r.id, m.title, r.seat_row, r.seat_col, r.reserve_date
from reservation r
inner join
movie as m
on m.id = r.movie_id;


--  ---------
insert into movie
(id, title, genre)
values
(1626123456, 'Avengers', 'Fantasy'),
(1627234567, 'Conjuring', 'Horror'),
(1626654321, 'Love Actually', 'Romance'),
(1627175707, 'Classical Thump', 'Bass'),
(1627175746, '7T\'s', 'Bass'),
(1627175983, 'Stomping Ground', 'Bass');

insert into reservation
values
(33011317445647, 1627175707, 4, 7, current_date),
(33011319245647, 1627175983, 2, 5, current_date);

select * from movie;
select * from reservation_list;