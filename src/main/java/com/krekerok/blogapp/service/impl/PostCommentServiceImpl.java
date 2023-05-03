package com.krekerok.blogapp.service.impl;

import com.krekerok.blogapp.dto.requests.CommentRequestDto;
import com.krekerok.blogapp.dto.responses.CommentResponseDto;
import com.krekerok.blogapp.service.PostCommentService;
import org.springframework.stereotype.Service;

@Service
public class PostCommentServiceImpl implements PostCommentService {

    @Override
    public CommentResponseDto createComment(Long postId, CommentRequestDto commentRequestDto,
        String jwt) {
        return CommentResponseDto.builder().build();
    }
}
