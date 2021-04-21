package com.moviebooking.consumer.service;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public boolean pay(double amount) {
        // call to third party payement api
        return true;
    }

}
