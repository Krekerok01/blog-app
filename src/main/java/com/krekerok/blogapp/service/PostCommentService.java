package com.krekerok.blogapp.service;

import com.krekerok.blogapp.dto.request.CommentRequestDto;
import com.krekerok.blogapp.dto.response.CommentResponseDto;
import com.krekerok.blogapp.entity.PostComment;

public interface PostCommentService {

    CommentResponseDto createComment(Long postId, CommentRequestDto commentRequestDto, String jwt);

    void deleteComment(Long commentId, String jwt);

    PostComment findPostCommentByCommentId(Long commentId);
}
