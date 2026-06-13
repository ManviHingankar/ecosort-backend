package com.ecosort.controller;

import com.ecosort.entity.PickupRequest;
import com.ecosort.entity.PickupStatus;
import com.ecosort.model.User;
import com.ecosort.repository.PickupRequestRepository;
import com.ecosort.repository.UserRepository;
import com.ecosort.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/pickup")
@CrossOrigin(origins = "*")
public class PickupController {

    @Autowired
    private PickupRequestRepository pickupRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    // SUBMIT PICKUP REQUEST
    @PostMapping("/request")
    public String createPickupRequest(@RequestBody PickupRequest request) {

        String email = request.getEmail();

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()){
            return "User not found!";
        }

        User user = optionalUser.get();

        request.setUser(user);
        request.setStatus(PickupStatus.PENDING);

        pickupRequestRepository.save(request);

        // SEND EMAIL
        emailService.sendPickupEmail(
                email,
                request.getWasteType(),
                request.getAddress(),
                ""
        );

        return "Pickup Request Submitted Successfully";
    }
}
