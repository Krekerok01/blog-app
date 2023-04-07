package com.krekerok.blogapp.controller;

import com.krekerok.blogapp.dto.requests.AppUserCreateDto;
import com.krekerok.blogapp.service.RedisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.validation.Valid;
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


    @Operation(summary = "User registration", description = "Create user and save in Redis database. The user's activation code is returned")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Create user",
            content = @Content),
        @ApiResponse(responseCode = "400", description = "Error: Username already exists or Error: Email already exists or any validation errors",
            content = @Content)})
    @PostMapping
    public ResponseEntity<String> registerUser(@Valid @RequestBody AppUserCreateDto appUserCreateDto) {
        return new ResponseEntity<>(redisService.registerUser(appUserCreateDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Verify user email")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful activation"),
        @ApiResponse(responseCode = "404", description = "Activation link is outdated",
            content = @Content)})
    @PostMapping("/verify/{activationCode}")
    public ResponseEntity<?> verifyUser(@PathVariable
    @Parameter(description = "Activation code from the email link") String activationCode, @RequestParam String email) {
        redisService.verifyUser(email, activationCode);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
