package com.krekerok.blogapp.controller;

import com.krekerok.blogapp.service.PostService;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/{blogId}")
    public ResponseEntity<Long> createPost(@PathVariable Long blogId, @RequestParam("imageFile") MultipartFile file,
        @NotBlank @Size(max = 60) @RequestParam("header") String header,
        @NotBlank @Size(max = 2500) @RequestParam("text") String text) {
        return new ResponseEntity<>(postService.createPost(blogId, file, header, text), HttpStatus.CREATED);
    }

}
