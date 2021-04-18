package com.moviebooking.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
public class BookingController {
    
    @PostMapping
    public ResponseEntity bookSeats(){
        try {
            
        } catch (Exception e) {
            //TODO: handle exception
        }
        return null;
    }

}
