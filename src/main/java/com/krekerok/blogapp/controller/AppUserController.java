package com.krekerok.blogapp.controller;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

import com.krekerok.blogapp.dto.responses.AppUserResponseDto;
import com.krekerok.blogapp.dto.responses.AppUserRolesResponseDto;
import com.krekerok.blogapp.dto.responses.PostResponseDto;
import com.krekerok.blogapp.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @Operation(summary = "Getting all users", description = "Getting all users from the database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful request",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Error: There are no users in the database",
            content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<AppUserResponseDto>> getAllAppUsers(){
        return new ResponseEntity<>(appUserService.findAll(), HttpStatus.OK);
    }

    @Operation(summary = "Getting a user", description = "Getting a user by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful request",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AppUserResponseDto.class))
            }),
        @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Error: User wasn't found in the database",
            content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<AppUserResponseDto> getUser(@PathVariable Long userId) {
        return new ResponseEntity<>(appUserService.getUser(userId), HttpStatus.OK);
    }

    @Operation(summary = "Deleting a user", description = "Deleting a user from the database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "No content if user was deleted from the database",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Error: User wasn't found in the database",
            content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppUser(@PathVariable long id) {
        return appUserService.deleteAppUserById(id)
            ? noContent().build()
            : notFound().build();
    }

    @Operation(summary = "Adding an Admin role to the User", description = "Adding an Admin role to the User by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful request",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AppUserRolesResponseDto.class))
            }),
        @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Error: User wasn't found in the database",
            content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{userId}/admin")
    public ResponseEntity<AppUserRolesResponseDto> addAdminRoleToTheAppUser(@PathVariable Long userId){
        return new ResponseEntity<>(appUserService.addAdminRoleToTheAppUserByUserId(userId), HttpStatus.OK);
    }
}
