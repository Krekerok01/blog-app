package com.krekerok.blogapp.controller;

import com.krekerok.blogapp.dto.requests.AppUserCreateDto;
import com.krekerok.blogapp.service.RedisService;
import javax.validation.Valid;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/registration")
public class RegistrationController {

    @Autowired
    private RedisService redisService;


    @PostMapping
    public ResponseEntity<String> registerUser(@Valid @RequestBody AppUserCreateDto appUserCreateDto) {
        return new ResponseEntity<>(redisService.registerUser(appUserCreateDto), HttpStatus.CREATED);
    }

    @PostMapping("/verify/{activationCode}")
    public ResponseEntity<?> verifyUser(@PathVariable String activationCode, @RequestParam String email) {
        redisService.verifyUser(email, activationCode);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
