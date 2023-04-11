package com.krekerok.blogapp.mapper;

import com.krekerok.blogapp.dto.responses.AppUserResponseDto;
import com.krekerok.blogapp.entity.AppUser;
import com.krekerok.blogapp.entity.RedisUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "blogId", source = "appUser.blog.blogId")
    AppUserResponseDto toAppUserResponseDto(AppUser appUser);

    AppUser toAppUser(RedisUser redisUser);
}
