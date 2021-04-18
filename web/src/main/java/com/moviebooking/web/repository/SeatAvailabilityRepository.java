package com.moviebooking.web.repository;

import java.util.List;
import java.util.Set;

import com.moviebooking.web.model.SeatAvailability;

import org.springframework.data.repository.CrudRepository;

public interface SeatAvailabilityRepository extends CrudRepository<SeatAvailability, Integer> {
    
    List<SeatAvailability> findAllByShowId(int showId);

    int countByIdIn(Set<Integer> showSeatIds);

}
