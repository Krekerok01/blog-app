package com.krekerok.blogapp.dto.response;

import com.krekerok.blogapp.entity.Role;
import java.util.Set;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AppUserRolesResponseDto {

    Long userId;
    Set<Role> roles;
}
