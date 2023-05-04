package com.krekerok.blogapp.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AppUserLoginResponseDto {

    String token;
    String type = "Bearer";
    Long userId;
    Long blogId;
    String username;
    String email;
}
