package com.krekerok.blogapp.controller;

import com.krekerok.blogapp.dto.requests.PostRequestDto;
import com.krekerok.blogapp.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;


    @Operation(summary = "Post creating", description = "Create post and save it to the database. The post ID will be returned.")
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
    public ResponseEntity<Long> createPost(@PathVariable Long blogId,
        @Valid PostRequestDto postRequestDto) {
        return new ResponseEntity<>(postService.createPost(blogId, postRequestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Deleting the post", description = "Deleting a post from the database")
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
