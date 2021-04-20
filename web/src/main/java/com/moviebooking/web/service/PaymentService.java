package com.moviebooking.web.service;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public boolean pay(double amount) {
        // call to third party payement api
        return true;
    }

}
