package com.moviebooking.consumer.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {

    public User(int userId) {
        this.id = userId;
    }

    @Id
    private int id;

    private String firstName;

    private String lastName;

    private String email;

    private boolean activeStatus;

}
