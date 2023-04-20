package com.krekerok.blogapp.service;

import com.krekerok.blogapp.dto.requests.PostRequestDto;
import com.krekerok.blogapp.dto.requests.PostUpdateRequestDto;
import com.krekerok.blogapp.dto.responses.PostResponseDto;
import com.krekerok.blogapp.entity.Blog;
import java.util.List;


public interface PostService {

    PostResponseDto createPost(Long blogId, PostRequestDto postRequestDto);

    void deletePost(Long postId);

    PostResponseDto getPost(Long postId);

    List<PostResponseDto> getAllPostsByBlog(Blog blog);

    PostResponseDto updatePostTextInfo(Long postId, PostUpdateRequestDto postUpdateRequestDto, String jwt);
}
