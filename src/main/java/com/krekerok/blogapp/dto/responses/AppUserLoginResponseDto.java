package com.krekerok.blogapp.dto.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
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
