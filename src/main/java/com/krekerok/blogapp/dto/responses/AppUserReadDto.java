package com.krekerok.blogapp.dto.responses;

import java.time.Instant;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AppUserReadDto {

    long id;
    String username;
    String email;
    Instant createdAt;
}
