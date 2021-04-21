package com.moviebooking.web.controller;

import com.moviebooking.web.model.Booking;
import com.moviebooking.web.model.BookingRequest;
import com.moviebooking.web.model.ErrorResponse;
import com.moviebooking.web.service.BookingRequestService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/booking")
@Tag(name = "Book Seats")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingRequestService bookingRequestService;

    @PostMapping("/reserve")
    public ResponseEntity<?> bookSeats(@RequestBody Booking booking) {
        BookingRequest requestMessage = null;
        try {
            requestMessage = bookingRequestService.addRequest(booking);
            boolean canProcess = bookingRequestService
                    .validateConcurrentRequests(requestMessage.getCorrelationId().toString());
            bookingRequestService.updateProcessStatus(requestMessage.getCorrelationId());
            if (!canProcess) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorResponse("Requested seats are blocked"));
            }
            return bookingRequestService.process(booking);
        } catch (Exception e) {
            logger.error("Error reserving seats", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Unexpected server error"));
        } finally {

        }
    }

}
