package com.ecosort.controller;

import com.ecosort.entity.LoginRequest;
import com.ecosort.model.User;
import com.ecosort.repository.UserRepository;
import com.ecosort.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        }

        userRepository.save(user);

        // SEND EMAIL
        emailService.sendRegistrationEmail(user.getEmail(), user.getName());

        return "User Registered Successfully";
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody LoginRequest loginRequest) {

        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }

        User user = optionalUser.get();

        if (user.getPassword() == null || !user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "Incorrect password"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("role", user.getRole() == null ? "USER" : user.getRole().toUpperCase());
        response.put("greenPoints", 0);

        return ResponseEntity.ok(response);
    }
}
