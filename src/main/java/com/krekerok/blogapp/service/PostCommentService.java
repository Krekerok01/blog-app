package com.krekerok.blogapp.service;

import com.krekerok.blogapp.dto.requests.CommentRequestDto;
import com.krekerok.blogapp.dto.responses.CommentResponseDto;

public interface PostCommentService {

    CommentResponseDto createComment(Long postId, CommentRequestDto commentRequestDto, String jwt);
}
