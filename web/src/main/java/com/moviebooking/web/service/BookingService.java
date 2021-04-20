package com.moviebooking.web.service;

import java.util.List;
import java.util.stream.Collectors;

import com.moviebooking.web.model.BlockedSeat;
import com.moviebooking.web.model.BookedSeat;
import com.moviebooking.web.model.Booking;
import com.moviebooking.web.model.ShowSeat;
import com.moviebooking.web.model.User;
import com.moviebooking.web.repository.BlockedSeatRepository;
import com.moviebooking.web.repository.BookedSeatRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    @Autowired
    private BlockedSeatRepository blockedSeatRepository;

    @Autowired
    private BookedSeatRepository bookedSeatRepository;

    @Autowired
    private PaymentService paymentService;

    /**
     * To book the seats
     * 
     * @param bookingRequest
     * @return success or failed string
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public String bookSeats(Booking bookingRequest) {
        try {
            int userId = 0;
            List<BlockedSeat> blockedSeats = bookingRequest.getShowSeatIds().stream().map(id -> {
                BlockedSeat blockedSeat = new BlockedSeat(new User(userId), new ShowSeat(id));
                return blockedSeat;
            }).collect(Collectors.toList());
            blockedSeatRepository.saveAll(blockedSeats);
            handlePayment(blockedSeats);
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "seat booking operation failed";
        }
    }

    /**
     * To handle the payment
     * 
     * @param blockedSeats
     * @return sucess or error string
     */
    private String handlePayment(List<BlockedSeat> blockedSeats) {
        double amount = blockedSeats.stream().mapToDouble(s -> s.getShowSeat().getPrice()).sum();
        try {
            boolean paid = paymentService.pay(amount);
            if (paid) {
                bookedSeatRepository.saveAll(blockedSeats.stream().map(b -> {
                    return new BookedSeat(b.getUser(), b.getShowSeat());
                }).collect(Collectors.toList()));
            }
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "payment failed";
        } finally {
            blockedSeatRepository.deleteAll(blockedSeats);
        }
    }

}
