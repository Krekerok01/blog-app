package com.krekerok.blogapp.controller;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import com.krekerok.blogapp.service.PostLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/likes")
public class PostLikeController {

    @Autowired
    private PostLikeService postLikeService;

    @Operation(summary = "Putting a like on a post", description = "Putting a like on a post by postId")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful request: The post was liked",
            content = @Content),
        @ApiResponse(responseCode = "204", description = "No content: The like was removed from the post",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Error: Post or User wasn't found in the database",
            content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{postId}")
    public ResponseEntity<?> likePost(@PathVariable Long postId, HttpServletRequest request) {
        return postLikeService.putLikeOrRemoveItFromThePost(postId, request.getHeader("Authorization").substring(7))
            ? ok().build()
            : noContent().build();
    }
}
