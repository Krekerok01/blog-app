package com.krekerok.blogapp.controller;

import com.krekerok.blogapp.dto.requests.BlogCreateDto;
import com.krekerok.blogapp.dto.responses.BlogReadDto;
import com.krekerok.blogapp.service.BlogService;
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

    @PostMapping("/{appUserId}")
    public ResponseEntity<BlogReadDto> createBlog(@PathVariable Long appUserId,  @RequestBody BlogCreateDto blogCreateDto) {
        return new ResponseEntity<>(blogService.createBlog(appUserId, blogCreateDto), HttpStatus.CREATED);

    }
}
