package com.krekerok.blogapp.controller;

import static org.springframework.http.ResponseEntity.ok;

import com.krekerok.blogapp.dto.requests.AppUserLoginDto;
import com.krekerok.blogapp.dto.responses.AppUserLoginReadDto;
import com.krekerok.blogapp.service.AppUserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/authentication")
public class AuthenticationController {

    @Autowired
    private AppUserService appUserService;

    @PostMapping("/login")
    public ResponseEntity<AppUserLoginReadDto> loginUser(@Valid @RequestBody AppUserLoginDto appUserLoginDto) {
        AppUserLoginReadDto appUserAccountReadDto = appUserService.loginUser(appUserLoginDto);
        return ok(appUserAccountReadDto);
    }
}