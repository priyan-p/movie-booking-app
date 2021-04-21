package com.moviebooking.consumer.runner;


import com.moviebooking.consumer.model.Booking;
import com.moviebooking.consumer.service.BookingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingConsumer {

    private static final Logger logger = LoggerFactory.getLogger(BookingConsumer.class);

    @Autowired
    private BookingService bookingService;

    @RabbitListener(queues = "reservation-queue", concurrency = "5")
    public String receive(Booking booking) {
        try {
            return bookingService.bookSeats(booking);
        } catch (Exception e) {
            logger.error("Error booking seats", e);
            return "Error booking seats";
        }
    }

}