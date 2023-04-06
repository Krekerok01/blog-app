package com.krekerok.blogapp.service;

import com.krekerok.blogapp.dto.requests.BlogCreateDto;
import com.krekerok.blogapp.dto.responses.BlogReadDto;
import com.krekerok.blogapp.entity.Blog;

public interface BlogService {

    BlogReadDto createBlog(Long appUserId, BlogCreateDto blogCreateDto);

    Blog findBlogById(Long blogId);
}
