package com.example.ordernotificationsystem.controller;

import com.example.ordernotificationsystem.dto.UserDTO;
import com.example.ordernotificationsystem.event.UserRegisteredEvent;
import com.example.ordernotificationsystem.producer.UserProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserProducerService userProducerService;

    @PostMapping(path = "/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        UserRegisteredEvent event = UserRegisteredEvent.create(userDTO.email());
        userProducerService.publishUserRegistrationEvent(event);
        return ResponseEntity.ok().body("User registered successfully");
    }
}
