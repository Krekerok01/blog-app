package com.krekerok.blogapp.service;

import com.krekerok.blogapp.dto.request.PostRequestDto;
import com.krekerok.blogapp.dto.request.PostUpdateRequestDto;
import com.krekerok.blogapp.dto.response.PostResponseDto;
import com.krekerok.blogapp.entity.Blog;
import com.krekerok.blogapp.entity.Post;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;


public interface PostService {

    PostResponseDto createPost(Long blogId, PostRequestDto postRequestDto);

    void deletePost(Long postId, String jwt);

    PostResponseDto getPost(Long postId);

    List<PostResponseDto> getAllPostsByBlog(Blog blog);

    PostResponseDto updatePostTextInfo(Long postId, PostUpdateRequestDto postUpdateRequestDto, String jwt);

    PostResponseDto updatePostImage(Long postId, MultipartFile imageFile, String jwt);

    Post findPostByPostId(Long postId);

    boolean existsByPostId(Long postId);
}
