package com.krekerok.blogapp.dto.response;

import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BlogAndPostsResponseDto {

    Long blogId;
    String blogName;
    Instant createdAt;
    Instant modifiedAt;
    List<PostResponseDto> posts;
}
