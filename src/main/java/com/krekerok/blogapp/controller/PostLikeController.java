package com.krekerok.blogapp.controller;

import com.krekerok.blogapp.service.PostLikeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{postId}")
    public ResponseEntity<Boolean> likeOrDislikePost(@PathVariable Long postId, HttpServletRequest request) {
        return new ResponseEntity<>(postLikeService.likeOrDislikePost(postId, request.getHeader("Authorization").substring(7)), HttpStatus.OK);
    }

}
