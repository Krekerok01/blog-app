package com.krekerok.blogapp.controller;

import com.krekerok.blogapp.dto.requests.PostRequestDto;
import com.krekerok.blogapp.dto.responses.BlogResponseDto;
import com.krekerok.blogapp.dto.responses.PostResponseDto;
import com.krekerok.blogapp.service.PostService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;


    @Operation(summary = "Creating a post", description = "Create a post and save it to the database. The post ID will be returned.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Post created successfully",
            content = @Content),
        @ApiResponse(responseCode = "400", description = "Error: Validation errors",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Error: Blog not found",
            content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(path = "/{blogId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponseDto> createPost(@PathVariable Long blogId,
        @Valid PostRequestDto postRequestDto) {
        return new ResponseEntity<>(postService.createPost(blogId, postRequestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Getting a post", description = "Getting a post by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful request",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PostResponseDto.class))
            }),
        @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "There are no post",
            content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        return new ResponseEntity<>(postService.getPost(postId), HttpStatus.OK);
    }

    @Operation(summary = "Deleting a post", description = "Deleting a post from the database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "No content if post was deleted from database",
            content = @Content),
        @ApiResponse(responseCode = "400", description = "Error: Problems with deletion file from the Cloudinary",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Error: Post wasn't found in the database",
            content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
