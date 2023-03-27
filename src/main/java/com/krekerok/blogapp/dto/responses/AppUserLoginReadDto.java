package com.krekerok.blogapp.dto.responses;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AppUserLoginReadDto {

    String token;
    String type = "Bearer";
    long userId;
    String username;
    String email;
}
