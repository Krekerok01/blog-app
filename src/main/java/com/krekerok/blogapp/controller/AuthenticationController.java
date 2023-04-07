package com.krekerok.blogapp.controller;

import static org.springframework.http.ResponseEntity.ok;

import com.krekerok.blogapp.dto.requests.AppUserLoginDto;
import com.krekerok.blogapp.dto.responses.AppUserLoginReadDto;
import com.krekerok.blogapp.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "User login")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login user",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AppUserLoginReadDto.class))
            }),
        @ApiResponse(responseCode = "401", description = "If incorrect login or password.",
            content = @Content)})
    @PostMapping("/login")
    public ResponseEntity<AppUserLoginReadDto> loginUser(@Valid @RequestBody AppUserLoginDto appUserLoginDto) {
        AppUserLoginReadDto appUserLoginReadDto = appUserService.loginUser(appUserLoginDto);
        return ok(appUserLoginReadDto);
    }
}