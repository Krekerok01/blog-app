package com.krekerok.blogapp.service;

import com.krekerok.blogapp.dto.requests.PostRequestDto;


public interface PostService {

    Long createPost(Long blogId, PostRequestDto postRequestDto);

    void deletePost(Long postId);
}
