package com.moviebooking.web.controller;

import java.util.List;

import com.moviebooking.web.model.Show;
import com.moviebooking.web.service.ShowService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shows")
public class ShowController {

    @Autowired
    private ShowService showService;

    @GetMapping
    public List<Show> listShows() {
        return showService.listAllShows();
    }

}
