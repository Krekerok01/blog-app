package com.krekerok.blogapp.service;

import com.krekerok.blogapp.dto.request.BlogRequestDto;
import com.krekerok.blogapp.dto.response.BlogAndPostsResponseDto;
import com.krekerok.blogapp.dto.response.BlogResponseDto;
import com.krekerok.blogapp.entity.Blog;

public interface BlogService {

    BlogResponseDto createBlog(Long appUserId, BlogRequestDto blogRequestDto);

    Blog findBlogById(Long blogId);

    void deleteBlogById(long id, String jwt);

    BlogResponseDto updateBlog(long blogId, BlogRequestDto blogRequestDto, String jwt);

    BlogAndPostsResponseDto getBlogWithPosts(long blogId);
}
