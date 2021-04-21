package com.moviebooking.consumer.repository;

import java.util.List;

import com.moviebooking.consumer.model.BookedSeat;
import com.moviebooking.consumer.model.ShowSeat;

import org.springframework.data.repository.CrudRepository;

public interface BookedSeatRepository extends CrudRepository<BookedSeat, Integer> {

    int countByUserIdAndShowSeatIn(int userId, List<ShowSeat> showSeats);

}
