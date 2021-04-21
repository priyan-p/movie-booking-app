package com.moviebooking.web.runner;

import com.moviebooking.web.repository.BlockedSeatRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TimeoutBlockedSeatsRunner {

    private static final Logger logger = LoggerFactory.getLogger(TimeoutBlockedSeatsRunner.class);

    @Autowired
    private BlockedSeatRepository blockedSeatRepository;

    @Scheduled(fixedDelay = 500)
    void releaseTimedOutSeats() {
        try {
            blockedSeatRepository.releaseTimedoutBlockedSeats();
        } catch (Exception e) {
            // FIXME: failure possible?
            logger.error("Error releasing seats", e);
        }
    }

}
