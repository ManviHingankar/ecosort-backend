package com.ecosort.controller;

import com.ecosort.model.User;
import com.ecosort.repository.UserRepository;
import com.ecosort.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    // REGISTER
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already registered!";
        }

        userRepository.save(user);

        // SEND EMAIL
        emailService.sendRegistrationEmail(user.getEmail(), user.getName());

        return "User Registered Successfully";
    }
}
