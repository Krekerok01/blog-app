package com.krekerok.blogapp.controller;

import static org.springframework.http.ResponseEntity.ok;

import com.krekerok.blogapp.dto.requests.AppUserLoginRequestDto;
import com.krekerok.blogapp.dto.responses.AppUserLoginResponseDto;
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

    @Operation(summary = "User authorization")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User authorization",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AppUserLoginResponseDto.class))
            }),
        @ApiResponse(responseCode = "401", description = "Error: Incorrect login or password.",
            content = @Content)})
    @PostMapping("/login")
    public ResponseEntity<AppUserLoginResponseDto> loginUser(@Valid @RequestBody AppUserLoginRequestDto appUserLoginRequestDto) {
        AppUserLoginResponseDto appUserLoginResponseDto = appUserService.loginUser(
            appUserLoginRequestDto);
        return ok(appUserLoginResponseDto);
    }
}