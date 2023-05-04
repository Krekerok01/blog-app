package com.krekerok.blogapp.controller;

import com.krekerok.blogapp.dto.request.AppUserRequestDto;
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


    @Operation(summary = "User registration",
        description = "Create a user and save to the Redis database. The user's activation code is returned")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registration",
            content = @Content),
        @ApiResponse(responseCode = "400", description = "Error: Username already exists or Error: Email already exists or any validation errors",
            content = @Content)})
    @PostMapping
    public ResponseEntity<String> registerUser(@Valid @RequestBody AppUserRequestDto appUserRequestDto) {
        return new ResponseEntity<>(redisService.registerUser(appUserRequestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Verification of user's mail")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful activation"),
        @ApiResponse(responseCode = "404", description = "Error: Activation link is outdated",
            content = @Content)})
    @PostMapping("/verify/{activationCode}")
    public ResponseEntity<?> verifyUser(@PathVariable
    @Parameter(description = "Activation code from the email link") String activationCode, @RequestParam String email) {
        redisService.verifyUser(email, activationCode);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
