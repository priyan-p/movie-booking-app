package com.moviebooking.web.repository;

import com.moviebooking.web.model.BookedSeat;

import org.springframework.data.repository.CrudRepository;

public interface BookedSeatRepository extends CrudRepository<BookedSeat, Integer> {

}
