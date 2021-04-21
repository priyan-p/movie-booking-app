package com.moviebooking.web.repository;

import java.util.List;

import com.moviebooking.web.model.ShowSeat;

import org.springframework.data.repository.CrudRepository;

public interface ShowSeatRepository extends CrudRepository<ShowSeat, Integer> {

    List<ShowSeat> findAllByShowId(int showId);

}
