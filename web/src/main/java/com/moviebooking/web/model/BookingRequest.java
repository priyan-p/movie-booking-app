package com.moviebooking.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class BookingRequest {

    @Id
    private String correlationId;

    private String data;

    @Column(insertable = false, updatable = false)
    private long requestTimeMillisec;

    private boolean processed;

}
