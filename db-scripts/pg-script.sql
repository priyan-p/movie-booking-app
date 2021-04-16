CREATE SCHEMA movie_booking AUTHORIZATION postgres;

-- movie_booking.movie definition

-- Drop table

-- DROP TABLE movie_booking.movie;

CREATE TABLE movie_booking.movie (
	id int8 NOT NULL,
	"name" varchar NOT NULL,
	running_time_hour int2 NOT NULL,
	"language" varchar NOT NULL,
	CONSTRAINT movie_pk PRIMARY KEY (id),
	CONSTRAINT movie_un UNIQUE (name, language)
);


-- movie_booking.movie_hall definition

-- Drop table

-- DROP TABLE movie_booking.movie_hall;

CREATE TABLE movie_booking.movie_hall (
	id int8 NOT NULL,
	"name" varchar NOT NULL,
	address varchar NOT NULL,
	CONSTRAINT movie_hall_pk PRIMARY KEY (id),
	CONSTRAINT movie_hall_un UNIQUE (name)
);


-- movie_booking."user" definition

-- Drop table

-- DROP TABLE movie_booking."user";

CREATE TABLE movie_booking."user" (
	id int8 NOT NULL,
	first_name varchar NOT NULL,
	last_name varchar NOT NULL,
	email varchar NOT NULL,
	active_status bool NOT NULL,
	CONSTRAINT user_pk PRIMARY KEY (id),
	CONSTRAINT user_un UNIQUE (email)
);


-- movie_booking.movie_show definition

-- Drop table

-- DROP TABLE movie_booking.movie_show;

CREATE TABLE movie_booking.movie_show (
	id int8 NOT NULL,
	movie_id int8 NOT NULL,
	movie_hall_id int8 NOT NULL,
	start_time timestamp(0) NOT NULL,
	end_time timestamp(0) NOT NULL,
	CONSTRAINT movie_show_pk PRIMARY KEY (id),
	CONSTRAINT movie_show_un UNIQUE (movie_hall_id, movie_id, start_time, end_time),
	CONSTRAINT movie_show_fk FOREIGN KEY (movie_hall_id) REFERENCES movie_booking.movie_hall(id),
	CONSTRAINT movie_show_fk_1 FOREIGN KEY (movie_id) REFERENCES movie_booking.movie(id)
);


-- movie_booking.seat definition

-- Drop table

-- DROP TABLE movie_booking.seat;

CREATE TABLE movie_booking.seat (
	id int8 NOT NULL,
	movie_hall_id int8 NOT NULL,
	"row" int4 NOT NULL,
	"number" int4 NOT NULL,
	CONSTRAINT seat_pk PRIMARY KEY (id),
	CONSTRAINT seat_un UNIQUE (movie_hall_id, number),
	CONSTRAINT seat_fk FOREIGN KEY (movie_hall_id) REFERENCES movie_booking.movie_hall(id)
);


-- movie_booking.booking definition

-- Drop table

-- DROP TABLE movie_booking.booking;

CREATE TABLE movie_booking.booking (
	id int8 NOT NULL,
	user_id int8 NOT NULL,
	show_id int8 NOT NULL,
	seat_id int8 NOT NULL,
	booking_time timestamp(0) NOT NULL,
	CONSTRAINT booking_pk PRIMARY KEY (id),
	CONSTRAINT booking_un UNIQUE (seat_id, show_id),
	CONSTRAINT booking_seat_fk FOREIGN KEY (user_id) REFERENCES movie_booking.seat(id),
	CONSTRAINT booking_show_fk FOREIGN KEY (user_id) REFERENCES movie_booking.movie_show(id),
	CONSTRAINT booking_user_fk FOREIGN KEY (user_id) REFERENCES movie_booking."user"(id)
);


-- movie_booking.reservation definition

-- Drop table

-- DROP TABLE movie_booking.reservation;

CREATE TABLE movie_booking.reservation (
	id int8 NOT NULL,
	user_id int8 NOT NULL,
	show_id int8 NOT NULL,
	seat_id int8 NOT NULL,
	reserved_time timestamp(0) NOT NULL,
	CONSTRAINT reservation_pk PRIMARY KEY (id),
	CONSTRAINT reservation_un UNIQUE (seat_id, show_id),
	CONSTRAINT reservation_seat_fk FOREIGN KEY (user_id) REFERENCES movie_booking.seat(id),
	CONSTRAINT reservation_show_fk FOREIGN KEY (user_id) REFERENCES movie_booking.movie_show(id),
	CONSTRAINT reservation_user_fk FOREIGN KEY (user_id) REFERENCES movie_booking."user"(id)
);
