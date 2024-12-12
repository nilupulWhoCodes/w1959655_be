package com.oop.ticket_backend.controller;

import com.oop.ticket_backend.dto.LoginRequest;
import com.oop.ticket_backend.dto.UserRequest;
import com.oop.ticket_backend.exception.ResourceNotFoundException;
import com.oop.ticket_backend.service.AuthenticationService;
import com.oop.ticket_backend.model.User;
import com.oop.ticket_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
        boolean isAuthenticated = authenticationService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

        if (isAuthenticated) {
            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }


    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(userRequest.getRole());
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }
}
