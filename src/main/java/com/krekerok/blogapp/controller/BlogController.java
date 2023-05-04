package com.krekerok.blogapp.controller;

import com.krekerok.blogapp.dto.request.BlogRequestDto;
import com.krekerok.blogapp.dto.response.BlogAndPostsResponseDto;
import com.krekerok.blogapp.dto.response.BlogResponseDto;
import com.krekerok.blogapp.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Operation(summary = "Creating a blog", description = "Create a blog and save it to the database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Blog created successfully",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BlogResponseDto.class))
            }),
        @ApiResponse(responseCode = "400", description = "Error: Validation errors or creating more than one blog",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Error: User wasn't found in the database",
            content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{appUserId}")
    public ResponseEntity<BlogResponseDto> createBlog(@PathVariable Long appUserId,
        @Valid @RequestBody BlogRequestDto blogRequestDto) {
        return new ResponseEntity<>(blogService.createBlog(appUserId, blogRequestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Deleting a blog", description = "Deleting a blog from the database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "No content if blog was deleted from database",
            content = @Content),
        @ApiResponse(responseCode = "400", description = "Error: Invalid blog ID",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Error: Blog wasn't found in the database",
            content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlog(@PathVariable long id, HttpServletRequest request) {
        blogService.deleteBlogById(id, request.getHeader("Authorization").substring(7));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Updating a blog", description = "Updating(changing) a blog name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BlogResponseDto.class))
            }),
        @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Error: User or blog wasn't found in the database",
            content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{blogId}")
    public ResponseEntity<BlogResponseDto> updateBlog(@PathVariable long blogId,
        @Valid @RequestBody BlogRequestDto blogRequestDto, HttpServletRequest request){
        return new ResponseEntity<>(blogService
            .updateBlog(blogId, blogRequestDto, request.getHeader("Authorization").substring(7)), HttpStatus.OK);
    }

    @Operation(summary = "Getting a blog", description = "Getting all information about the blog")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful request",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BlogAndPostsResponseDto.class))
            }),
        @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Error: Blog wasn't found in the database",
            content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{blogId}")
    public ResponseEntity<BlogAndPostsResponseDto> getBlog(@PathVariable Long blogId) {
        return new ResponseEntity<>(blogService.getBlogWithPosts(blogId), HttpStatus.OK);
    }
}
