package com.moviebooking.web.controller;

import java.util.List;

import com.moviebooking.web.model.SeatAvailability;
import com.moviebooking.web.service.ShowSeatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/seats")
@Tag(name = "List Seats for a Show")
public class SeatController {

    @Autowired
    private ShowSeatService showSeatService;

    @GetMapping("/show/{showId}")
    public List<SeatAvailability> getSeatAvailability(@PathVariable int showId) {
        return showSeatService.getSeatAvailability(showId);
    }

}
