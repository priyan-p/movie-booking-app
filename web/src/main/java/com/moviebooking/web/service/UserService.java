package com.moviebooking.web.service;

import java.util.Optional;

import com.moviebooking.web.model.User;
import com.moviebooking.web.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DefaultOAuth2User userDetails = (DefaultOAuth2User) authentication.getPrincipal();
        User user = new User(userDetails.getAttribute("id"), userDetails.getAttribute("name"));
        user.setActiveStatus(true);
        Optional<User> optionalUser = userRepository.findById(userDetails.getAttribute("id"));
        if (!optionalUser.isPresent())
            return userRepository.save(user);
        return optionalUser.get();
    }

}
