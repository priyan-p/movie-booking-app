package com.moviebooking.web.repository;

import com.moviebooking.web.model.BlockedSeat;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BlockedSeatRepository extends CrudRepository<BlockedSeat, Integer> {

    @Query(nativeQuery = true, value = "select movie_booking.remove_timedout_records()")
    void releaseTimedoutBlockedSeats();

}
