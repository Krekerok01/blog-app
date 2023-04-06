package com.krekerok.blogapp.controller;

import com.krekerok.blogapp.dto.responses.AppUserReadDto;
import com.krekerok.blogapp.service.AppUserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @GetMapping("/all")
    public ResponseEntity<List<AppUserReadDto>> getAllAppUsers(){
        return new ResponseEntity<>(appUserService.findAll(), HttpStatus.OK);
    }
}
