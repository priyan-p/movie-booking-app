package com.moviebooking.web.repository;

import java.util.List;

import com.moviebooking.web.model.BookedSeat;
import com.moviebooking.web.model.ShowSeat;

import org.springframework.data.repository.CrudRepository;

public interface BookedSeatRepository extends CrudRepository<BookedSeat, Integer> {

    int countByUserIdAndShowSeatIn(int userId, List<ShowSeat> showSeats);

}
