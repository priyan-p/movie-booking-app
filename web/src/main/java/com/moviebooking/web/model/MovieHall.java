package com.moviebooking.web.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class MovieHall {

    @Id
    private int id;

    private String name;

    private String address;

}
