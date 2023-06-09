package com.krekerok.blogapp.dto.response;

import java.time.Instant;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BlogResponseDto {

    Long blogId;
    String blogName;
    Instant createdAt;
    Instant modifiedAt;
}
