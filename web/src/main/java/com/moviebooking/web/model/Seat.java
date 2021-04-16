package com.moviebooking.web.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Seat {

    @Id
    private int id;

    @OneToOne
    @JoinColumn(name = "movie_hall_id")
    private MovieHall movieHall;

    private int row;

    private int number;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MovieHall getMovieHall() {
        return movieHall;
    }

    public void setMovieHall(MovieHall movieHall) {
        this.movieHall = movieHall;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

}
