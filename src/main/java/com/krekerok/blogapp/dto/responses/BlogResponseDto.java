package com.krekerok.blogapp.dto.responses;

import java.time.Instant;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BlogResponseDto {

    private Long blogId;
    private String blogName;
    private Instant createdAt;
}
