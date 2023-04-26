package com.krekerok.blogapp.dto.responses;

import java.time.Instant;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AppUserResponseDto {

    Long userId;
    String username;
    String email;
    Instant createdAt;
    Instant modifiedAt;
    Long blogId;
}
