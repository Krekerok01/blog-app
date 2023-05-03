package com.krekerok.blogapp.dto.responses;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CommentResponseDto {

    Long commentId;
    String authorUsername;
    Long postId;
    String message;
}
