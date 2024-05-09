package com.example.vtravelwebapp.controller;

import com.example.vtravelwebapp.model.UserInfo;
import com.example.vtravelwebapp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Endpoint to store user details in session attributes
    @PostMapping("/api/user")
    public ResponseEntity<String> storeUserInSession(@RequestBody UserInfo userDetails, HttpSession session) {
        try {
            // Store user details in session attributes
            session.setAttribute("userDetails", userDetails);
            return ResponseEntity.ok("User details stored in session successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error storing user details in session");
        }
    }
}
