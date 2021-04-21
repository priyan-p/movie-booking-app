package com.moviebooking.web.repository;

import com.moviebooking.web.model.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

}
