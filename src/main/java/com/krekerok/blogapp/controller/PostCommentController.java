package com.krekerok.blogapp.controller;

import com.krekerok.blogapp.dto.requests.CommentRequestDto;
import com.krekerok.blogapp.dto.responses.CommentResponseDto;
import com.krekerok.blogapp.service.PostCommentService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/comments")
public class PostCommentController {

    @Autowired
    private PostCommentService postCommentService;

    @Operation(summary = "Creating a comment on the post", description = "Leaving a comment on the post by postId")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successful request: The post was commented",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CommentResponseDto.class))
            }),
        @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Error: Post or User wasn't found in the database",
            content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{postId}")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long postId,
        @Valid @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
        return new ResponseEntity<>(postCommentService
                .createComment(postId, commentRequestDto, request.getHeader("Authorization").substring(7)),
            HttpStatus.CREATED);
    }
}
