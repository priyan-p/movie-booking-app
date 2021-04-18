package com.moviebooking.web.service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.moviebooking.web.model.BlockedSeat;
import com.moviebooking.web.model.BookedSeat;
import com.moviebooking.web.model.ShowSeat;
import com.moviebooking.web.model.User;
import com.moviebooking.web.repository.BlockedSeatRepository;
import com.moviebooking.web.repository.BookedSeatRepository;
import com.moviebooking.web.repository.SeatAvailabilityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    @Value("${app.booking.maxSeatsAllowed}")
    private int maxSeatsPerUser;

    @Autowired
    private SeatAvailabilityRepository seatAvailabilityRepository;

    @Autowired
    private BlockedSeatRepository blockedSeatRepository;

    @Autowired
    private BookedSeatRepository bookedSeatRepository;

    @Autowired
    private PaymentService paymentService;

    @Transactional(propagation = Propagation.REQUIRED)
    public void bookSeats(Set<Integer> showSeatIds, int userId) {
        validate(showSeatIds, userId);
        List<BlockedSeat> blockedSeats = showSeatIds.stream().map(id -> {
            BlockedSeat blockedSeat = new BlockedSeat(new User(userId), new ShowSeat(id));
            return blockedSeat;
        }).collect(Collectors.toList());
        blockedSeatRepository.saveAll(blockedSeats);
        handlePayment(blockedSeats);
    }

    private void handlePayment(List<BlockedSeat> blockedSeats) {
        double amount = blockedSeats.stream().mapToDouble(s -> s.getShowSeat().getPrice()).sum();
        try {
            boolean paid = paymentService.pay(amount);
            if (paid) {
                bookedSeatRepository.saveAll(blockedSeats.stream().map(b -> {
                    return new BookedSeat(b.getUser(), b.getShowSeat());
                }).collect(Collectors.toList()));
            }
        } finally {
            blockedSeatRepository.deleteAll(blockedSeats);
        }
    }

    private void validate(Set<Integer> showSeatIds, int userId) {
        if (showSeatIds.size() > maxSeatsPerUser) {
            throw new RuntimeException("Maximum seats allowed is " + maxSeatsPerUser);
        }

        int count = seatAvailabilityRepository.countByIdIn(showSeatIds);
        if (count != showSeatIds.size()) {
            throw new RuntimeException("Requested seats not available");
        }
    }

}
