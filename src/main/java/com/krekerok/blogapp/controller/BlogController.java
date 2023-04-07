package com.krekerok.blogapp.controller;

import com.krekerok.blogapp.dto.requests.BlogCreateDto;
import com.krekerok.blogapp.dto.responses.AppUserLoginReadDto;
import com.krekerok.blogapp.dto.responses.BlogReadDto;
import com.krekerok.blogapp.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Operation(summary = "Blog creating", description = "Create blog and save it to the database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Blog created successfully",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BlogReadDto.class))
            }),
        @ApiResponse(responseCode = "400", description = "Error: Validation errors or creating more than one blog",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Error: User not found",
            content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{appUserId}")
    public ResponseEntity<BlogReadDto> createBlog(@PathVariable Long appUserId,
        @Valid @RequestBody BlogCreateDto blogCreateDto) {
        return new ResponseEntity<>(blogService.createBlog(appUserId, blogCreateDto), HttpStatus.CREATED);
    }
}
