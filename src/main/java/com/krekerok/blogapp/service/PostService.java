package com.krekerok.blogapp.service;

import com.krekerok.blogapp.dto.requests.PostRequestDto;
import com.krekerok.blogapp.dto.responses.PostResponseDto;


public interface PostService {

    PostResponseDto createPost(Long blogId, PostRequestDto postRequestDto);

    void deletePost(Long postId);

    PostResponseDto getPost(Long postId);
}
