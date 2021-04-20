package com.moviebooking.web.consumer;

import com.moviebooking.web.model.Booking;
import com.moviebooking.web.service.BookingService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingConsumer {

    @Autowired
    private BookingService bookingService;

    @RabbitListener(queues = "reservation-queue", concurrency = "5")
    public String receive(Booking booking) {
        try {
            return bookingService.bookSeats(booking);
        } catch (Exception e) {
            return "failed";
        }
    }

}