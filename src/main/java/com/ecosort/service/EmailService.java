package com.ecosort.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    @Value("${spring.mail.from:}")
    private String mailFrom;

    public void sendRegistrationEmail(String toEmail, String name) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("EcoSort Registration Successful");

        message.setText(
                "Hello " + name + ",\n\n" +
                        "Welcome to EcoSort E-Waste Management System 🌱\n\n" +
                        "Your account has been successfully created.\n\n" +
                        "You can now login and schedule your e-waste pickup.\n\n" +
                        "Thank you for helping the environment ♻\n\n" +
                        "Regards,\nEcoSort Team"
        );

        message.setFrom(getSenderEmail());

        mailSender.send(message);
    }

    public void sendPickupEmail(String toEmail, String wasteType, String address, String date) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Pickup Request Confirmed");

        message.setText(
                "Your pickup request has been registered.\n\n" +
                        "Waste Type: " + wasteType + "\n" +
                        "Address: " + address + "\n" +
                        "Pickup Date: " + date + "\n\n" +
                        "Status: PENDING\n\n" +
                        "Recycler will contact you soon.\n\n" +
                        "EcoSort Team ♻"
        );

        message.setFrom(getSenderEmail());

        mailSender.send(message);
    }

    private String getSenderEmail() {
        return mailFrom == null || mailFrom.isBlank() ? mailUsername : mailFrom;
    }
}