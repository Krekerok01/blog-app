package com.krekerok.blogapp.service;

import com.krekerok.blogapp.dto.requests.BlogCreateDto;
import com.krekerok.blogapp.dto.responses.BlogReadDto;

public interface BlogService {

    BlogReadDto createBlog(Long appUserId, BlogCreateDto blogCreateDto);
}
