package com.moviebooking.consumer.service;

import java.util.List;
import java.util.stream.Collectors;

import com.moviebooking.consumer.model.BlockedSeat;
import com.moviebooking.consumer.model.BookedSeat;
import com.moviebooking.consumer.model.Booking;
import com.moviebooking.consumer.model.ShowSeat;
import com.moviebooking.consumer.model.User;
import com.moviebooking.consumer.repository.BlockedSeatRepository;
import com.moviebooking.consumer.repository.BookedSeatRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

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
    public String bookSeats(Booking bookingRequest) {
        List<BlockedSeat> seatsToBlock = null;
        try {
            int userId = 1;
            seatsToBlock = bookingRequest.getShowSeatIds().stream().map(id -> {
                BlockedSeat blockedSeat = new BlockedSeat(new User(userId), new ShowSeat(id));
                return blockedSeat;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting blocked seats", e);
            return "Seat booking operation failed";
        }
        blockSeats(seatsToBlock);
        double amount = seatsToBlock.stream().mapToDouble(s -> s.getShowSeat().getPrice()).sum();
        return paymentService.pay(amount) ? moveToBooked(seatsToBlock) : "Payment failed";
    }

    /**
     * To block seats
     * 
     * @param blockedSeats
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    private String blockSeats(List<BlockedSeat> blockedSeats) {
        try {
            blockedSeatRepository.saveAll(blockedSeats);
            return null;
        } catch (DataIntegrityViolationException die) {
            logger.error("Error inserting to blocked seats", die);
            return "Requested seats are not available";
        } catch (Exception e) {
            logger.error("Error inserting to blocked seats", e);
            return "Error blocking seats";
        }
    }

    /**
     * To handle the payment
     * 
     * @param blockedSeats
     * @return sucess or error string
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    private String moveToBooked(List<BlockedSeat> blockedSeats) {
        try {
            bookedSeatRepository.saveAll(blockedSeats.stream().map(b -> {
                return new BookedSeat(b.getUser(), b.getShowSeat());
            }).collect(Collectors.toList()));
            return "ok";
        } catch (DataIntegrityViolationException die) {
            logger.error("Error moving to booked seats", die);
            return "Requested seats are not available";
        } catch (Exception e) {
            logger.error("Error moving to booked", e);
            return "Error booking seats";
        } finally {
            blockedSeatRepository.deleteAll(blockedSeats);
        }
    }

}
