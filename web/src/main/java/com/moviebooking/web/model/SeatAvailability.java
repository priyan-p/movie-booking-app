package com.moviebooking.web.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class SeatAvailability {

    @Id
    private int id;

    @OneToOne
    @JoinColumn(name = "show_id")
    private Show show;

    @OneToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    private double price;

    private boolean available;

}
