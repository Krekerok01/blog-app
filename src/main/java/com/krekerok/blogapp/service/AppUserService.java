package com.krekerok.blogapp.service;

import com.krekerok.blogapp.dto.request.AppUserLoginRequestDto;
import com.krekerok.blogapp.dto.response.AppUserLoginResponseDto;
import com.krekerok.blogapp.dto.response.AppUserResponseDto;
import com.krekerok.blogapp.dto.response.AppUserRolesResponseDto;
import com.krekerok.blogapp.entity.AppUser;
import com.krekerok.blogapp.entity.Blog;
import com.krekerok.blogapp.entity.RedisUser;
import java.util.List;

public interface AppUserService {

    void checkingForExistenceInTheDatabase(String username, String email);

    void createUser(RedisUser redisUser);

    AppUserLoginResponseDto loginUser(AppUserLoginRequestDto appUserLoginRequestDto);

    AppUser findAppUserByAppUserId(Long appUserId);

    AppUser saveAppUser(AppUser appUser);

    List<AppUserResponseDto> findAll();

    boolean deleteAppUserById(long id);

    boolean checkingForDataCompliance(Long blogId, String jwt);

    void deleteLinkToTheBlog(Blog blog);

    AppUserResponseDto getUser(Long userId);

    AppUserRolesResponseDto addAdminRoleToTheAppUserByUserId(Long userId);

    AppUser findAppUserByUsernameFromJWT(String jwt);

}
