package com.moviebooking.web.service;

import java.util.List;

import com.moviebooking.web.model.SeatAvailability;
import com.moviebooking.web.repository.SeatAvailabilityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowSeatService {

    @Autowired
    private SeatAvailabilityRepository seatAvailabilityRepository;

    public List<SeatAvailability> getSeatAvailability(int showId) {
        return seatAvailabilityRepository.findAllByShowId(showId);
    }

}
