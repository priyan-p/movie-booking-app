package com.moviebooking.web.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Booking {

    private Set<Integer> showSeatIds;

    @JsonIgnore
    private User user;

}
