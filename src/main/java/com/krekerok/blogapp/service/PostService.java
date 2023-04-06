package com.krekerok.blogapp.service;

import org.springframework.web.multipart.MultipartFile;

public interface PostService {

    Long createPost(Long blogId, MultipartFile file, String header, String text);
}
