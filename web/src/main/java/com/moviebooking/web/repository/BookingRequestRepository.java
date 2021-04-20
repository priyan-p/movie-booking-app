package com.moviebooking.web.repository;

import java.util.UUID;

import com.moviebooking.web.model.BookingRequest;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BookingRequestRepository extends CrudRepository<BookingRequest, UUID> {

    @Query(nativeQuery = true, name = "select movie_booking.validate_concurrent_requests(:uuid)")
    boolean validateConcurrentRequests(@Param("uuid") String uuid);

}
