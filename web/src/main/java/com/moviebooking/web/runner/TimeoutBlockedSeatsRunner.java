package com.moviebooking.web.runner;

import com.moviebooking.web.repository.BlockedSeatRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TimeoutBlockedSeatsRunner {

    @Autowired
    private BlockedSeatRepository blockedSeatRepository;

    @Scheduled(fixedDelay = 500)
    void releaseTimedOutSeats() {
        try {
            blockedSeatRepository.releaseTimedoutBlockedSeats();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
