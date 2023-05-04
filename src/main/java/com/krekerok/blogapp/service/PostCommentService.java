package com.krekerok.blogapp.service;

import com.krekerok.blogapp.dto.requests.CommentRequestDto;
import com.krekerok.blogapp.dto.responses.CommentResponseDto;
import com.krekerok.blogapp.entity.PostComment;
import org.springframework.security.core.parameters.P;

public interface PostCommentService {

    CommentResponseDto createComment(Long postId, CommentRequestDto commentRequestDto, String jwt);

    void deleteComment(Long commentId, String jwt);

    PostComment findPostCommentByCommentId(Long commentId);
}
