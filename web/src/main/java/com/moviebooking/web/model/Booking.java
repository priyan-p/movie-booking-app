package com.moviebooking.web.model;

import java.util.Set;

import lombok.Data;

@Data
public class Booking {

    private Set<Integer> showSeatIds;

}
