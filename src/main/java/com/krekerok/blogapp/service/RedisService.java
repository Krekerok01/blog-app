package com.krekerok.blogapp.service;

import com.krekerok.blogapp.dto.requests.AppUserCreateDto;

public interface RedisService {

    String registerUser(AppUserCreateDto userCreateDto);

    void verifyUser(String email, String activationCode);

}
