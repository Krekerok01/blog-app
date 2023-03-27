package com.krekerok.blogapp.service;

import com.krekerok.blogapp.dto.requests.AppUserLoginDto;
import com.krekerok.blogapp.dto.responses.AppUserLoginReadDto;
import com.krekerok.blogapp.entity.AppUser;
import com.krekerok.blogapp.entity.RedisUser;

public interface AppUserService {

    void checkingForExistenceInTheDatabase(String username, String email);

    void createUser(RedisUser redisUser);

    AppUserLoginReadDto loginUser(AppUserLoginDto appUserLoginDto);

    AppUser findAppUserByAppUserId(Long appUserId);

    AppUser saveBlogToTheAppUser(AppUser appUser);
}
