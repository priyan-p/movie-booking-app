package com.moviebooking.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moviebooking.web.model.Booking;
import com.moviebooking.web.model.BookingRequest;
import com.moviebooking.web.service.BookingRequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingRequestService bookingRequestService;

    @PostMapping("/reserve")
    public ResponseEntity<?> bookSeats(@RequestBody Booking booking) {
        BookingRequest requestMessage = null;
        try {
            requestMessage = bookingRequestService.addRequest(booking);
            boolean canProcess = bookingRequestService
                    .validateConcurrentRequests(requestMessage.getCorrelationId().toString());
            if (!canProcess) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Request declined due to conflict");
            }
            return bookingRequestService.process(booking);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown Error");
        } finally {

        }
    }

}
