package com.moviebooking.web.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

import lombok.Data;

@Entity
@Data
public class BookingRequest {

    @Id
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID correlationId;

    private String data;

    @Column(insertable = false, updatable = false)
    private Date requestTime;

    private boolean processed;

}
