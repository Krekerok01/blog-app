package com.krekerok.blogapp.dto.responses;


import java.time.Instant;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PostResponseDto {

    Long postId;
    String header;
    String text;
    String imageURL;
    Instant createdAt;
    Long blogId;
}
