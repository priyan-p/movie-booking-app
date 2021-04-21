package com.moviebooking.web.service;

import java.util.List;
import java.util.Optional;

import com.moviebooking.web.model.Show;
import com.moviebooking.web.repository.ShowRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    public List<Show> listAllShows() {
        return (List<Show>) showRepository.findAll();
    }

    public List<Show> listAllShowsByMovieOrHall(String movieName, String movieHallName) {
        return showRepository.findAllShowById(movieName, movieHallName);
    }

    public Optional<Show> findById(int showId) {
        return showRepository.findById(showId);
    }

}
