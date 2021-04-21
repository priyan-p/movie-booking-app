package com.moviebooking.web.repository;

import com.moviebooking.web.model.BookingRequest;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BookingRequestRepository extends CrudRepository<BookingRequest, String> {

    @Query(nativeQuery = true, name = "select movie_booking.validate_concurrent_requests(?1)")
    boolean existsByCorrelationId(String correlationId);

}
