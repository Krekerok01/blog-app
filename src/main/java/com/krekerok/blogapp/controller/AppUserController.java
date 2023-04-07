package com.krekerok.blogapp.controller;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

import com.krekerok.blogapp.dto.responses.AppUserReadDto;
import com.krekerok.blogapp.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @Operation(summary = "Get all users", description = "Get all users from database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful request",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "There are no users in the database",
            content = @Content)})
    @GetMapping("/all")
    public ResponseEntity<List<AppUserReadDto>> getAllAppUsers(){
        return new ResponseEntity<>(appUserService.findAll(), HttpStatus.OK);
    }

    @Operation(summary = "Delete user", description = "Delete user from database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "No content if user was deleted from database",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Not found if user wasn't found in database",
            content = @Content)})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppUser(@PathVariable long id) {
        return appUserService.deleteAppUserById(id)
            ? noContent().build()
            : notFound().build();
    }
}
