package com.moviebooking.web.controller;

import java.util.List;

import com.moviebooking.web.model.Show;
import com.moviebooking.web.service.ShowService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/shows")
@Tag(name = "List Shows")
public class ShowController {

    @Autowired
    private ShowService showService;

    @GetMapping
    public List<Show> listShows(@RequestParam(required = false) String movieName,
            @RequestParam(required = false) String movieHallName) {
        return showService.listAllShowsByMovieOrHall(movieName, movieHallName);
    }

}
